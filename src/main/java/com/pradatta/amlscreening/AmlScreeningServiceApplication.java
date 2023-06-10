package com.pradatta.amlscreening;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) //Disabled Security for testing
public class AmlScreeningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmlScreeningServiceApplication.class, args);
	}

}
