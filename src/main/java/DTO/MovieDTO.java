/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import entities.Movie;
import java.io.Serializable;
/**
 *
 * @author cecilie
 */
public class MovieDTO implements Serializable {
  
    private Long id;
    private int year;
    private String title;
    private String[] actors;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.year = movie.getYear();
        this.title = movie.getTitle();
        this.actors = movie.getActors();
    }

    public MovieDTO(int year, String title, String[] actors) {
        this.year = year;
        this.title = title;
        this.actors = actors;
    }

    public Long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String[] getActors() {
        return actors;
    }
    
    
    
}
