package com.zuiyu.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SentinelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinelApplication.class, args);
	}

}
