package com.javatechie.movie.service;

import com.javatechie.movie.domain.Movie;
import reactor.core.publisher.Mono;


public interface MovieService {
    public Mono<Movie> searchMovieByTitle(String apiKey, String title);
    public Mono<Movie> searchMovieById(String apiKey, String imdbId);
    public boolean controlloId(String utenteIdent);
}
