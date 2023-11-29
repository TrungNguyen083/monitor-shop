package com.mshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableScheduling
@Configuration
@CrossOrigin
public class MonitorShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorShopApplication.class, args);
	}

}
