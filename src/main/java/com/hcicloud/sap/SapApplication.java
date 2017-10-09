package com.hcicloud.sap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
//@ComponentScan(basePackages = {"com.*.*"})
public class SapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapApplication.class, args);
	}
}
