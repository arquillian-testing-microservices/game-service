package org.arquillian.microservices.gameservice.boundary;

import org.arquillian.microservices.gameservice.controller.GamesController;
import org.arquillian.microservices.gameservice.entity.Game;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/")
public class GamesResource {

    @Inject
    GamesController gamesController;

    @GET
    @Path("title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameByTitle(@PathParam("title") String title) {

        Optional<Game> game = gamesController.findGameAndCheckPegi(16, title);

        return game
                .map(g -> Response.ok(g.toJsonObject()).build())
                .orElse(Response.status(
                        Response.Status.NOT_FOUND)
                        .build());

    }

}
