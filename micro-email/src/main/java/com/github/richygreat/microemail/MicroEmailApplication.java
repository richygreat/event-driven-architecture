package com.github.richygreat.microemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.github.richygreat.microemail.stream.Processor;

@SpringBootApplication
@EnableBinding(Processor.class)
public class MicroEmailApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroEmailApplication.class, args);
	}
}
