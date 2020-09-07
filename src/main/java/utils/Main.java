/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.Movie;
import facades.MovieFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cecilie
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        //int year = 2020;
        //String title = "KOALAEN!";
        //String[] actors =  {"Jamen Brown", "Robert Hardley", "Bobo the Dodo"};
        //Movie movie = new Movie(year, title, actors);
        em.getTransaction().commit();
        
        System.out.println(MovieFacade.getFacadeExample(emf).getMovieById(1));
        

        em.close();
        emf.close();
    }
}
