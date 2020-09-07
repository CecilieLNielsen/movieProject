package facades;

import DTO.MovieDTO;
import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public MovieDTO getMovieById(long id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class).setParameter("id", id);
            return new MovieDTO(query.getSingleResult());
        } finally {
            em.close();
        }
    }

    public List<MovieDTO> getAllMovies() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
            List<MovieDTO> result = new ArrayList();
            for (Movie movie : query.getResultList()) {
                result.add(new MovieDTO(movie));
            }
            return result;
        } finally {
            em.close();
        }
    }

    public List<MovieDTO> getMoviesByYear(int year) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.year = :year", Movie.class).setParameter("year", year);
            List<MovieDTO> result = new ArrayList();
            for (Movie movie : query.getResultList()) {
                result.add(new MovieDTO(movie));
            }
            return result;
        } finally {
            em.close();
        }
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getYear(), movieDTO.getTitle(), movieDTO.getActors());
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return new MovieDTO(movie);
        } finally {
            em.close();
        }
    }

    public void deleteMovieById(long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie m WHERE m.id = :id").setParameter("id", id).executeUpdate();
            //           em.remove(em.find(Movie.class, id));
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

}
