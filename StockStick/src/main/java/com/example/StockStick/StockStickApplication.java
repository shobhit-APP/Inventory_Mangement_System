package com.example.StockStick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StockStickApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockStickApplication.class, args);
	}

}
