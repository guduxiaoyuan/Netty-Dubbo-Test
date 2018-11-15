package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@PropertySource("classpath:dubbo/dubbo.properties")
@ImportResource({"classpath:dubbo.xml"})
public class ProvvvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvvvApplication.class, args);
	}
}
