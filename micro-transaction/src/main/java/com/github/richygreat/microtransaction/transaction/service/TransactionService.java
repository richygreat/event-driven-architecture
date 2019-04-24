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
import com.github.richygreat.microtransaction.user.entity.UserEntity;
import com.github.richygreat.microtransaction.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
	private final Source source;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;

	@Transactional
	@StreamListener(value = KafkaChannel.TRANSACTION_SINK_CHANNEL, condition = KafkaEventConstants.TRANSACTION_CREATION_REQUESTED_HEADER)
	public void handleTransactionCreationRequested(@Payload TransactionDTO transactionDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleTransactionCreationRequested: Entering: transactionDTO: {} partition: {}", transactionDTO,
				partition);
		Optional<UserEntity> optionalUser = userRepository.findById(transactionDTO.getUserId());
		if (!optionalUser.isPresent()) {
			failTransaction(transactionDTO, "User Not found");
		}
		Optional<TransactionEntity> optionalTransaction = transactionRepository.findById(transactionDTO.getId());
		if (optionalTransaction.isPresent()) {
			failTransaction(transactionDTO, "Duplicate found");
			return;
		}
		TransactionEntity transaction = new TransactionEntity();
		transaction.setId(transactionDTO.getId());
		transaction.setUserId(transactionDTO.getUserId());
		transaction.setAmount(transactionDTO.getAmount());
		transactionRepository.save(transaction);

		UserEntity user = optionalUser.get();
		user.setBalance(user.getBalance() + transactionDTO.getAmount());
		userRepository.save(user);
		log.info("handleTransactionCreationRequested: User: {} balance: {}", user.getId(), user.getBalance());

		boolean sent = source.transactionProducer().send(KafkaMessageUtility.createMessage(transactionDTO,
				KafkaEventConstants.TRANSACTION_CREATED, transactionDTO.getId(), transactionDTO.getUserId()));
		log.info("handleTransactionCreationRequested: Exiting transactionDTO: {} sent: {}", transactionDTO.getId(),
				sent);
		if (!sent) {
			throw new EventPushFailedException();
		}
	}

	private void failTransaction(TransactionDTO transactionDTO, String reason) {
		transactionDTO.setFailureReason(reason);
		boolean sent = source.transactionProducer().send(KafkaMessageUtility.createMessage(transactionDTO,
				KafkaEventConstants.TRANSACTION_CREATION_FAILED, transactionDTO.getId(), transactionDTO.getUserId()));
		if (!sent) {
			throw new EventPushFailedException();
		}
	}
}
