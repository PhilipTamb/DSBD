package com.javatechie.movie.service;

import com.javatechie.movie.controller.MovieController;
import com.javatechie.movie.domain.Movie;
import com.javatechie.saga.commons.dto.OrderRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@Service
public class MovieServiceImplementation implements MovieService{
    private static final String OMDB_MIME_TYPE = "application/json";
    private static final String OMDB_API_BASE_URL = "http://omdbapi.com";
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImplementation.class);

    @Autowired
    private RestTemplate template;

    private final WebClient webClient;

    public MovieServiceImplementation() {
        this.webClient = WebClient.builder()
                .baseUrl(OMDB_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, OMDB_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .build();
    }
    @Override
    public Mono<Movie> searchMovieByTitle(String apiKey, String title) {
        Mono<Movie> mov=  webClient.post()
                .uri("/?apikey="+apiKey+"&t="+title)
                .retrieve()
                .bodyToMono(Movie.class);
        mov.subscribe(s -> sendMovieToOrder(s));
        return mov;
    }

    @Override
    public Mono<Movie> searchMovieById(String apiKey, String imdbId) {
        Mono<Movie> mov=  webClient.post()
                .uri("/?apikey="+apiKey+"&t="+imdbId)
                .retrieve()
                .bodyToMono(Movie.class);
        mov.subscribe(s -> sendMovieToOrder(s));
        return mov;
    }


    public void sendMovieToOrder(Movie mov){
        System.out.println(MovieController.userID);
        template.postForObject("http://order-service:8081/order/userId", MovieController.userID, MovieController.userID.getClass());
        template.postForObject("http://order-service:8081/order/film", mov, Movie.class);
    }

    public boolean controlloId(String id) {
        String checkId;
        checkId= template.postForObject("http://payment-service:8082/payment/userId", id, id.getClass());
        if (checkId.equals("true")) {
            return true;
        }
        else {
            return false;
        }
    }



}
