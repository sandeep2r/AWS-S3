package com.sandeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {
//	    ConfigurationPropertiesAutoConfiguration.class,
//	    LifecycleAutoConfiguration.class,
//	    PropertyPlaceholderAutoConfiguration.class,
//	    ApplicationAvailabilityAutoConfiguration.class,
//	    ProjectInfoAutoConfiguration.class
//	})
//@EnableSwagger2

@SpringBootApplication
public class DynamicpdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicpdfApplication.class, args);
	}

}
