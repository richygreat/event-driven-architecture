package com.github.richygreat.microemail.transaction.service;

import javax.transaction.Transactional;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microemail.stream.KafkaChannel;
import com.github.richygreat.microemail.stream.KafkaEventConstants;
import com.github.richygreat.microemail.transaction.entity.TransactionEntity;
import com.github.richygreat.microemail.transaction.model.TransactionDTO;
import com.github.richygreat.microemail.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
	private final TransactionRepository transactionRepository;

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
}
