package dev.rugved.camunda_rabbitMQ.worker;

import dev.rugved.camunda_rabbitMQ.consumer.MessageConsumer;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.rugved.camunda_rabbitMQ.service.CorrelationIdService;

import java.util.Map;

@Component
public class RabbitmqWorker {

    @Autowired
    private CorrelationIdService correlationIdService;

    @Autowired
    private MessageConsumer messageConsumer;

    @JobWorker(type = "rabbitMQWorker")
    public void rabbitmqWorker(final JobClient client, final ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        int id = (int) variables.get("id");
        System.out.println("ID set in RabbitmqWorker: " + id);
        correlationIdService.setCorrelationId(id);

        try {
            // Get the rabbitmqResponse from CorrelationIdService
            String rabbitmqResponse = correlationIdService.getRabbitmqResponse();

            // Complete the job with rabbitmqResponse
            client.newCompleteCommand(job.getKey())
                    .variables(rabbitmqResponse)
                    .send()
                    .join();

            // Reset CorrelationIdService after job completion
            correlationIdService.reset();

        } catch (InterruptedException e) {
            System.err.println("Job completion interrupted: " + e.getMessage());
        }
    }
}
