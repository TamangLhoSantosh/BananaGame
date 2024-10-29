/**
 * This is the entry point for the Banana Game.
 * This class contains main method to initialize the application.
 * The application is built with Spring Boot.
 *
 * @author Santosh Tamang
 * @date 2024-10-29
 */

package com.tamanglhosantosh.bananagame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The main application class for Banana Game.
 * Annotated with @SpringBootApplication to indicate it’s the primary
 * Spring component and configuration class.
 */
@SpringBootApplication
public class BananaGameApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BananaGameApplication.class, args);
	}

}
