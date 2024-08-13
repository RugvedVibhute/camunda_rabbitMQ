package dev.rugved.camunda_rabbitMQ.worker;

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

    @JobWorker(type = "rabbitMQWorker")
    public void rabbitmqWorker(final JobClient client, final ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        int id = (int) variables.get("id");
        System.out.println("ID set in RabbitmqWorker: " + id);
        correlationIdService.setCorrelationId(id);

    }
}