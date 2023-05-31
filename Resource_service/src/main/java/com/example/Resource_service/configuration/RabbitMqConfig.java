package com.example.Resource_service.configuration;



import com.example.Resource_service.service.RabbitMQSender;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMqConfig {

    @Value("rabbitmq.queue.songs")
    String queueName;
    @Bean
    public Queue hello() {
        return new Queue("songs");
    }

    @Bean
    public RabbitMQSender sender() {
        return new RabbitMQSender();
    }
}
