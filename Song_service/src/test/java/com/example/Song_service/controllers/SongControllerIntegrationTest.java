package com.example.Song_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.models.SongMetaData;
import com.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SongRepository repository;
    @Container
    static PostgreSQLContainer postgres=new PostgreSQLContainer("postgres:14.3")
            .withDatabaseName("postgres");


    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.driver-class-name",postgres::getDriverClassName);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void appShouldSaveEntity() throws Exception{
        SongMetaData entity=new SongMetaData();
        entity.setName("test");
        mvc.perform(post("/songs")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
       var result =repository.findById(1);
       assertTrue(result.isPresent());
    }
    @Test
    public void appShouldDeleteEntity()throws Exception{
        SongMetaData entity=new SongMetaData();
        entity.setName("test");
        entity.setSongId(2);
        repository.save(entity);
        mvc.perform(delete("/songs").param("ids","2")).andExpect(status().isOk());
        assertTrue(repository.findById(2).isEmpty());

    }
    @Test
    public void appShouldReturnEntity()throws Exception{
        SongMetaData entity=new SongMetaData();
        entity.setName("test");
        entity.setSongId(3);
        repository.save(entity);
        mvc.perform(get("/songs/3").contentType("application/json"))
                .andExpect(status().isOk());
    }

}
