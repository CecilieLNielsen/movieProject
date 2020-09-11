package rest;

import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie r1,r2;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Movie m").executeUpdate();
        em.createNativeQuery("ALTER TABLE `MOVIE` AUTO_INCREMENT = 1").executeUpdate(); // Resetter auto increment tilbage til 1
        em.getTransaction().commit();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
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
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/movie").then().statusCode(200);
    }
   
    //This test assumes the database contains two rows
    @Test
    public void testDemoMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Hello World"));   
    }
    
    @Test
    public void testCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/count").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode());
        //.body("4", equalTo(4));   
    }
    
    @Test
    public void testGetMovieAll() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/all").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("movieInformation", hasSize(4));   
    }
    
    @Test
    public void testGetTitle() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/title/Buda's crawl").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("title", equalTo("Buda's crawl"));   
    }
    
     
    /*@Test //MANGLER
    public void testGetTitleNotFound() throws Exception { 
        given()
        .contentType("application/json")
        .get("/movie/title/{title}").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("title", equalTo("Koalaen"));   
    }
    */
   
    @Test
    public void testGetMovieById() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/1").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("id", equalTo(1));   
    }

    
}
