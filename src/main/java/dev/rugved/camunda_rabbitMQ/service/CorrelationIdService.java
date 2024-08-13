package dev.rugved.camunda_rabbitMQ.service;

import org.springframework.stereotype.Service;

@Service
public class CorrelationIdService {

    private int correlationId;

    public void setCorrelationId(int id) {
        this.correlationId = id;
    }

    public int getCorrelationId() {
        return correlationId;
    }
}