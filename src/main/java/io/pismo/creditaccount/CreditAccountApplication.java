package io.pismo.creditaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CreditAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditAccountApplication.class, args);
	}

}
