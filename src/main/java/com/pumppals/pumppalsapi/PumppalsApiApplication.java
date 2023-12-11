package com.pumppals.pumppalsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PumppalsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PumppalsApiApplication.class, args);
		System.out.println("\n\n\nHELLO!");
		System.out.println("Pump Pals is running on port 8080.");
		System.out.println("Navigate to http://localhost:8080/ to view the app.");
		System.out.println("If you are using Chrome, feel free to install the application to your desktop!\n\n\n");

	}

}
