package com.github.richygreat.microuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.github.richygreat.microuser.stream.Processor;

@SpringBootApplication
@EnableBinding(Processor.class)
public class MicroUserApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroUserApplication.class, args);
	}
}
