package com.github.richygreat.microbankbff.transaction.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microbankbff.stream.KafkaChannel;
import com.github.richygreat.microbankbff.stream.KafkaConstants;
import com.github.richygreat.microbankbff.stream.KafkaEventConstants;
import com.github.richygreat.microbankbff.stream.KafkaMessageUtility;
import com.github.richygreat.microbankbff.stream.Source;
import com.github.richygreat.microbankbff.stream.exception.EventPushFailedException;
import com.github.richygreat.microbankbff.transaction.entity.TransactionEntity;
import com.github.richygreat.microbankbff.transaction.model.TransactionDTO;
import com.github.richygreat.microbankbff.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
	private final Source source;
	private final TransactionRepository transactionRepository;
	private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
	private ReadOnlyKeyValueStore<String, TransactionDTO> transactionSnapshotStore;

	@Transactional
	public void createTransaction(TransactionDTO transactionDTO) {
		transactionDTO.setId(UUID.randomUUID().toString());
		boolean sent = source.transactionProducer()
				.send(KafkaMessageUtility.createMessage(transactionDTO,
						KafkaEventConstants.TRANSACTION_CREATION_REQUESTED, transactionDTO.getId(),
						transactionDTO.getUserId()));
		log.info("createTransaction: Exiting transactionDTO: {} sent: {}", transactionDTO.getId(), sent);
		if (!sent) {
			throw new EventPushFailedException();
		}
	}

	@Transactional
	@StreamListener(value = KafkaChannel.TRANSACTION_SINK_CHANNEL, condition = KafkaEventConstants.TRANSACTION_CREATED_HEADER)
	public void handleTransactionCreated(@Payload TransactionDTO transactionDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleTransactionCreated: Entering: transactionDTO: {} partition: {}", transactionDTO, partition);
		TransactionEntity transaction = new TransactionEntity();
		transaction.setId(transactionDTO.getId());
		transaction.setUserId(transactionDTO.getUserId());
		transaction.setAmount(transactionDTO.getAmount());
		transactionRepository.save(transaction);
	}

	@Transactional
	@StreamListener(value = KafkaChannel.TRANSACTION_SINK_CHANNEL, condition = KafkaEventConstants.TRANSACTION_CREATION_FAILED_HEADER)
	public void handleTransactionCreationFailed(@Payload TransactionDTO transactionDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleTransactionCreationFailed: Entering: transactionDTO: {} partition: {}", transactionDTO,
				partition);
	}

	public TransactionDTO getTransaction(String id) {
		Optional<TransactionEntity> optionalTransaction = transactionRepository.findById(id);
		if (!optionalTransaction.isPresent()) {
			initOnRun();
			return transactionSnapshotStore.get(id);
		}
		TransactionEntity transaction = optionalTransaction.get();
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setId(transaction.getId());
		transactionDTO.setUserId(transaction.getUserId());
		transactionDTO.setAmount(transaction.getAmount());
		transactionDTO.setFailureReason(transaction.getFailureReason());
		return transactionDTO;
	}

	public void initOnRun() {
		if (transactionSnapshotStore != null) {
			return;
		}
		transactionSnapshotStore = streamsBuilderFactoryBean.getKafkaStreams().store(KafkaConstants.TRANSACTION_STORE,
				QueryableStoreTypes.keyValueStore());
	}
}
