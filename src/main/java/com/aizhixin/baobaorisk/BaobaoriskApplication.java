package com.aizhixin.baobaorisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BaobaoriskApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaobaoriskApplication.class, args);
	}

}

