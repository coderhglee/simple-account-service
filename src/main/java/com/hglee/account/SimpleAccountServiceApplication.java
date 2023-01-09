package com.hglee.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SimpleAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleAccountServiceApplication.class, args);
	}

}
