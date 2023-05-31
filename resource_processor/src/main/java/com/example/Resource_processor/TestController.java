package com.example.Resource_processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    ProcessorService processorService;

    @GetMapping("resources/{id}")
    public ResponseEntity<Object> test(@PathVariable Integer id){
        processorService.getMetaDataAsync(String.valueOf(id)).doOnNext(System.out::println).block();
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
