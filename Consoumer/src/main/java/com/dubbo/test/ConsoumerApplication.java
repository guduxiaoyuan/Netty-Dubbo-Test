package com.dubbo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.boot.Banner;

@SpringBootApplication
// @EnableDubboConfiguration
@ImportResource({ "classpath:consumer.xml" })
public class ConsoumerApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(ConsoumerApplication.class);
		// 设置banner的模式为隐藏
		springApplication.setBannerMode(Banner.Mode.OFF);
		// 启动springboot应用程序
		springApplication.run(args);
	}
}
