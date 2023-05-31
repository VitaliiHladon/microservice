package com.example.Resource_processor;

import com.example.Resource_processor.models.SongMetaData;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProcessorService {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public ProcessorService(WebClient.Builder loadBalancedWebClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.lbFunction = lbFunction;
    }

    public Mono<SongMetaData> postMetaData(SongMetaData metaData) {

        return loadBalancedWebClientBuilder
                .filter(lbFunction)
                .build()
                .post().uri("http://song_service/songs")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(metaData), SongMetaData.class)
                .retrieve()
                .bodyToMono(SongMetaData.class)
                .retry(3);
    }

    public Mono<SongMetaData> getMetaDataAsync(String id) {
        WebClient client = WebClient.create("http://localhost:8080");
        return loadBalancedWebClientBuilder
                .filter(lbFunction)
                .build()
                .get().uri("http://song_service/metaData/" + id)
                .retrieve()
                .bodyToMono(SongMetaData.class)
                .retry(3);
    }
}
