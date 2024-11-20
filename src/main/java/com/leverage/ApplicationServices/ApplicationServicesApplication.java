package com.leverage.ApplicationServices;

import com.leverage.ApplicationServices.configuration.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(RSAKeyRecord.class)
public class ApplicationServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationServicesApplication.class, args);
	}

}
