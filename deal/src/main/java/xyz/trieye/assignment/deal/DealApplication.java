package xyz.trieye.assignment.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DealApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealApplication.class, args);
	}

}
