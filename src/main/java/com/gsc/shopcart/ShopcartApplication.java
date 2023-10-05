package com.gsc.shopcart;

import com.gsc.shopcart.config.environment.EnvironmentConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopcartApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShopcartApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(ShopcartApplication.class, args);
	}

	@Bean
	CommandLineRunner loadDataSources(EnvironmentConfig environment) {
		return args -> {
			environment.loadDataSources();
		};
	}
}
