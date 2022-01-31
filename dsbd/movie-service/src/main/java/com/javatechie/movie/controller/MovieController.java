package com.javatechie.movie.controller;

import com.javatechie.movie.domain.Movie;
import com.javatechie.movie.service.MovieService;
import org.springframework.core.env.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private MovieService movieService;
    private Environment env;
    static public String userID;

    @Autowired
    public MovieController(MovieService movieService, Environment env){
        this.movieService=movieService;
        this.env=env;
    }


    @GetMapping("/user/{id}")
    public String saveUserId(@PathVariable String id) {
        if(movieService.controlloId(id) == true) {
            userID = id;
            return id;
        }
        else {
            return "L'id utente non esiste, per favore inserisci un id valido";
        }
    }

    @GetMapping("/movies/title/{name}")
    public Mono<Movie> getMovieByTitle(@PathVariable String name) {
        Movie film= new Movie();
        if (userID == null) {
            film.setMovieTitle("Devi inserire l'ID utente prima di poter utilizzare i servizi");
            Mono<Movie> mov= Mono.just(film);
            return mov;
        }
        String apiKey = env.getProperty("app.api.key");
        return movieService.searchMovieByTitle(apiKey, name);
    }


    @GetMapping("/movies/id/{imdbId}")
    public Mono<Movie> getMovieById(@PathVariable String imdbId) {
        Movie film= new Movie();
        if (userID == null) {
            film.setMovieTitle("Devi inserire l'ID utente prima di poter utilizzare i servizi");
            Mono<Movie> mov= Mono.just(film);
            return mov;
        }
        return movieService.searchMovieById(env.getProperty("app.api.key"), imdbId);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
