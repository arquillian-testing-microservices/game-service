package org.arquillian.microservices.gameservice.boundary;

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
    GamesGateway gamesGateway;

    @GET
    @Path("title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameByTitle(@PathParam("title") String title) {

        final Optional<Game> game = gamesGateway.findGameByTitle(title);

        if (game.isPresent()) {
            return Response.ok(
                        game.get().toJsonObject())
                    .build();
        } else {
            return Response.status(
                        Response.Status.NOT_FOUND)
                    .build();
        }

    }

}
