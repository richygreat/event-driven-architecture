package com.github.richygreat.microbankbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.github.richygreat.microbankbff.stream.Processor;

@SpringBootApplication
@EnableBinding(Processor.class)
public class MicroBankBffApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroBankBffApplication.class, args);
	}
}
