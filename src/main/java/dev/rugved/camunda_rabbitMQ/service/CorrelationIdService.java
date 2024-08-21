package dev.rugved.camunda_rabbitMQ.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class CorrelationIdService {

    private Integer correlationId = null; // Use Integer for nullable type
    private String rabbitmqResponse = null;
    private CountDownLatch latch;

    public void setCorrelationId(int id) {
        this.correlationId = id;
        this.latch = new CountDownLatch(1);  // Reset latch whenever a new ID is set
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setRabbitmqResponse(String response) {
        this.rabbitmqResponse = response;
        latch.countDown();  // Signal that the response is ready
    }

    public String getRabbitmqResponse() throws InterruptedException {
        latch.await();  // Wait until the latch is counted down
        return rabbitmqResponse;
    }

    public String getRabbitmqResponseWithTimeout() throws InterruptedException {
        if (latch.await(30, TimeUnit.SECONDS)) { // Wait up to 30 seconds for response
            return rabbitmqResponse;
        } else {
            throw new InterruptedException("Timeout waiting for RabbitMQ response");
        }
    }

    public void reset() {
        this.correlationId = null;
        this.rabbitmqResponse = null;
        this.latch = null;
    }
}
