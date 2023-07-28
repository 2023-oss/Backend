package com.project.easysign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EasysignApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasysignApplication.class, args);
	}

}
