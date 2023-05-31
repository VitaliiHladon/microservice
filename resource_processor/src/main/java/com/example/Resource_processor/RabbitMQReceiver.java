package com.example.Resource_processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "songs")
@Slf4j
public class RabbitMQReceiver {

    ProcessorService service;

    public RabbitMQReceiver(ProcessorService service) {
        this.service = service;
    }

    @RabbitHandler
    public void receive(String in) {
        var data=service.getMetaDataAsync(in).block();
        service.postMetaData(data).block();
    }
}
