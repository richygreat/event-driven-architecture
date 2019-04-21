package com.github.richygreat.microtransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.github.richygreat.microtransaction.stream.Processor;

@SpringBootApplication
@EnableBinding(Processor.class)
public class MicroTransactionApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroTransactionApplication.class, args);
	}
}
