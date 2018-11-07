package com.example.demo;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class ConsoumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsoumerApplication.class, args);
	}
}
