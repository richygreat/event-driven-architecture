package com.github.richygreat.microtransaction.transaction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.richygreat.microtransaction.transaction.model.TransactionDTO;
import com.github.richygreat.microtransaction.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionRestController {
	private final TransactionService transactionService;

	@GetMapping("/createtransaction")
	public TransactionDTO createTransaction(@RequestParam("userid") String userId,
			@RequestParam("amount") Double amount) {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setUserId(userId);
		transactionDTO.setAmount(amount);
		transactionService.createTransaction(transactionDTO);
		log.info("createTransaction: Streamed transactionDTO: {}", transactionDTO);
		return transactionDTO;
	}

	@GetMapping("/transactions/{id}")
	public TransactionDTO getTransaction(@PathVariable("id") String id) {
		log.info("getTransaction: Entering id: {}", id);
		return transactionService.getTransaction(id);
	}
}
