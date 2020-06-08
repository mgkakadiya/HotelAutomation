package com.hotelautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.hotelautomation")
public class HotelAutomationApplication {
	public static void main(String[] args) {
		SpringApplication.run(HotelAutomationApplication.class, args);
	}
}
