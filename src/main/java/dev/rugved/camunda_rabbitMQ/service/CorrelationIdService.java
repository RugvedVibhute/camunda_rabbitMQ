package dev.rugved.camunda_rabbitMQ.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.CountDownLatch;

@Service
public class CorrelationIdService {

    private int correlationId;
    private String rabbitmqResponse;
    private final CountDownLatch latch = new CountDownLatch(1);

    public void setCorrelationId(int id) {
        this.correlationId = id;
    }

    public int getCorrelationId() {
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
}
