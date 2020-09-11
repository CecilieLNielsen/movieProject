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

        try {
            em.getTransaction().begin();
            String[] actors = {"Holger Hansen", "Yin Yung", "George Lodway"};
            em.persist(new Movie(2017, "Buda's quest for greatness", actors));
            em.persist(new Movie(2018, "Buda's crawl", actors));
            em.persist(new Movie(2019, "Buda's revenge", actors));
            em.persist(new Movie(2019, "Buda's last day", actors));
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        emf.close();
    }
}
