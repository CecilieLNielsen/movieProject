package rest;

import DTO.MovieDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.EMF_Creator;
import facades.MovieFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    
    private static final MovieFacade FACADE =  MovieFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("/movieId/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMovieById(@PathParam("id") long id) {
        return Response.ok().entity(GSON.toJson(FACADE.getMovieById(id))).build();
    }
    
    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllMovies() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllMovies())).build();
    }
    
    @Path("/all/byYear/{year}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllMovies(@PathParam("year") int year) {
        return Response.ok().entity(GSON.toJson(FACADE.getMoviesByYear(year))).build();
    }
    
    // TODO: This is wrong, needs to be a POST and use POST body to retrieve year, title and actors
    // @Path("/create/year/{year}/title/{title}/actors/{actors}")
    // @GET
    // @Produces({MediaType.APPLICATION_JSON})
    // public Response getAllMovies(@PathParam("year") int year, @PathParam("title") String title, @PathParam("actors") String[] actors) {
    //     MovieDTO movieDTO = new MovieDTO(year, title, actors);
    //     return Response.ok().entity(GSON.toJson(FACADE.createMovie(movieDTO))).build();
    // }
    
    @Path("/delete/byId/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteMovieById(@PathParam("id") long id) {
        FACADE.deleteMovieById(id);
        return Response.ok().build();
    }
}
