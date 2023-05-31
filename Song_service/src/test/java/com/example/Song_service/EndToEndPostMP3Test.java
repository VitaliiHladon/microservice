package com.example.Song_service;

import com.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

@SpringBootTest
public class EndToEndPostMP3Test {
    @Autowired
    SongRepository repository;

    @Test
    public void postSongToServiceShouldSaveMetaData() throws Exception{
        var songId = postSong().block();
        assertTrue(isInteger(songId), "After posting song we should receive response status 200");
        var id = Integer.parseInt(songId);

        Thread.sleep(5000); // wait for 5 seconds

        assertTrue(!repository.findById(id).isEmpty(),"Repository should store at least 1 record with id");
    }

    private Mono<String> postSong() {
        String url = "http://localhost:8080/resources";
        File file = new File("src/main/resources/static/Pufino - Thoughtful.mp3");
        WebClient webClient = WebClient.builder().baseUrl(url).build();
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class);
    }

    private boolean isInteger(String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}

