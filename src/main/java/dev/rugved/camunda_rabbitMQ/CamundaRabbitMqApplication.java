package dev.rugved.camunda_rabbitMQ;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamundaRabbitMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaRabbitMqApplication.class, args);
	}

}
