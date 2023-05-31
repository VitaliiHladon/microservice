package com.example.Resource_processor.configuration;


import com.example.Resource_processor.ProcessorService;
import com.example.Resource_processor.RabbitMQReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
    @Autowired
    ProcessorService service;

    @Bean
    public RabbitMQReceiver receiver() {
        return new RabbitMQReceiver(service);
    }
}
