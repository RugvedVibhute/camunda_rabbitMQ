package dev.rugved.camunda_rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "camunda_queue")
    public void receiveMessage(Message message, Channel channel) throws IOException {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);
            int id = jsonNode.get("id").asInt();

            if (id == 1222) {
                // Process message
                System.out.println("Processing message: " + jsonNode.toString());

                // Acknowledge the message after successful processing
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                // Reject the message and requeue it
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        } catch (Exception e) {
            // Handle exception and reject the message
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            e.printStackTrace();
        }
    }
}

