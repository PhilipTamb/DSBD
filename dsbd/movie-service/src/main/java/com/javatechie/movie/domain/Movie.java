package com.javatechie.movie.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    @JsonProperty("Title")
    private String movieTitle;
    @JsonProperty("Year")
    private String releaseYear;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("imdbID")
    private String imdbID;
    // /getter and setters

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImdbID() { return imdbID; }

    public void setImdbID(String imdbID) { this.imdbID= imdbID; }

}
