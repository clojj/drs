package de.fisp.skp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class DrsApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(DrsApplication.class);
		Properties properties = new Properties();
		properties.setProperty("spring.resources.staticLocations", "classpath:/static/elm/dist/");
		app.setDefaultProperties(properties);
		app.run(args);
	}
}
