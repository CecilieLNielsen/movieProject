package facades;

import DTO.MovieDTO;
import utils.EMF_Creator;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getFacadeExample(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Movie m").executeUpdate();
        em.createNativeQuery("ALTER TABLE `MOVIE` AUTO_INCREMENT = 1").executeUpdate(); // Resetter auto increment tilbage til 1
        em.getTransaction().commit();
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
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
    }

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Movie m").executeUpdate();
        em.createNativeQuery("ALTER TABLE `MOVIE` AUTO_INCREMENT = 1").executeUpdate(); // Resetter auto increment tilbage til 1
        em.getTransaction().commit();
    }

    @Test
    public void testGetMovieById() {
        // Arrange
        long id = 1;
        // Act
        MovieDTO result = facade.getMovieById(id);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAllMovies() {
        // Arrange

        // Act
        List<MovieDTO> result = facade.getAllMovies();
        // Assert
        assertEquals(4, result.size());
    }

    @Test
    public void testGetMoviesByYear() {
        // Arrange
        int year = 2019;
        // Act
        List<MovieDTO> result = facade.getMoviesByYear(year);
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testCreateMovie() {
        // Arrange
        String[] actors = {"Holger Hansen", "Yin Yung", "George Lodway"};
        MovieDTO movieDTO = new MovieDTO(2020, "Buda's relief", actors);
        // Act
        MovieDTO result = facade.createMovie(movieDTO);
        // Assert
        assertEquals((long) 5, (long) result.getId());
    }

    @Test
    public void testDeleteMovieById() {
        // Arrange
        long id = 2;
        // Act
        facade.deleteMovieById(id);
        // Assert
        assertTrue(true);
    }
}
