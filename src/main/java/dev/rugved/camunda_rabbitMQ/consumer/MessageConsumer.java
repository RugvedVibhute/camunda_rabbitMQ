package dev.rugved.camunda_rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rugved.camunda_rabbitMQ.service.CorrelationIdService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CorrelationIdService correlationIdService;

    @RabbitListener(queues = "Test_Queue")
    public void receiveMessage(Message message, Channel channel) throws IOException {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);
            int messageId = jsonNode.get("id").asInt();
            int correlationId = correlationIdService.getCorrelationId();

            if (messageId == correlationId) {
                // Process message
                System.out.println("Processing message: " + jsonNode.toString());

                // Acknowledge the message after successful processing
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                // Reject the message and requeue it
                System.out.println("ID does not match. Requeuing message: " + jsonNode.toString());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        } catch (IOException e) {
            // Handle JSON parsing exception and reject the message
            System.err.println("Failed to parse message. Requeuing: " + new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            e.printStackTrace();
        } catch (Exception e) {
            // Handle any other exceptions and reject the message
            System.err.println("Error processing message. Requeuing: " + new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            e.printStackTrace();
        }
    }
}