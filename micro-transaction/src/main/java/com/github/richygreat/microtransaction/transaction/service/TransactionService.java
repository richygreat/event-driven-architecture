package com.github.richygreat.microtransaction.transaction.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microtransaction.stream.KafkaChannel;
import com.github.richygreat.microtransaction.stream.KafkaEventConstants;
import com.github.richygreat.microtransaction.stream.KafkaMessageUtility;
import com.github.richygreat.microtransaction.stream.Source;
import com.github.richygreat.microtransaction.stream.exception.EventPushFailedException;
import com.github.richygreat.microtransaction.transaction.entity.TransactionEntity;
import com.github.richygreat.microtransaction.transaction.model.TransactionDTO;
import com.github.richygreat.microtransaction.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
	private final Source source;
	private final TransactionRepository transactionRepository;

	@Transactional
	@StreamListener(value = KafkaChannel.TRANSACTION_SINK_CHANNEL, condition = KafkaEventConstants.TRANSACTION_CREATION_REQUESTED_HEADER)
	public void handleTransactionCreationRequested(@Payload TransactionDTO transactionDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleTransactionCreationRequested: Entering: transactionDTO: {} partition: {}", transactionDTO,
				partition);
		Optional<TransactionEntity> optionalTransaction = transactionRepository.findById(transactionDTO.getId());
		if (optionalTransaction.isPresent()) {
			transactionDTO.setFailureReason("Duplicate found");
			boolean sent = source.transactionProducer()
					.send(KafkaMessageUtility.createMessage(transactionDTO,
							KafkaEventConstants.TRANSACTION_CREATION_FAILED, transactionDTO.getId(),
							transactionDTO.getUserId()));
			if (!sent) {
				throw new EventPushFailedException();
			}
			return;
		}
		TransactionEntity transaction = new TransactionEntity();
		transaction.setId(transactionDTO.getId());
		transaction.setUserId(transactionDTO.getUserId());
		transaction.setAmount(transactionDTO.getAmount());
		transactionRepository.save(transaction);
		boolean sent = source.transactionProducer().send(KafkaMessageUtility.createMessage(transactionDTO,
				KafkaEventConstants.TRANSACTION_CREATED, transactionDTO.getId(), transactionDTO.getUserId()));
		log.info("handleTransactionCreationRequested: Exiting transactionDTO: {} sent: {}", transactionDTO.getId(),
				sent);
		if (!sent) {
			throw new EventPushFailedException();
		}
	}
}
