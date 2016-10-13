package org.arquillian.microservices.gameservice.boundary;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.arquillian.microservices.gameservice.entity.Pegi;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AgeCheckerGateway {

    @Inject
    @ConfigProperty(name = "agechecker.url")
    String url;

    private Client client;

    @PostConstruct
    public void init() {
        client  = ClientBuilder.newClient();
    }

    public boolean canViewGameInformation(int userAge, Pegi gamePegi) {
        final Response response = client
                .target(url)
                .path("/checker/")
                .request(MediaType.TEXT_PLAIN)
                .post(
                        Entity.entity(Json
                                        .createObjectBuilder()
                                        .add("age", userAge)
                                        .add("pegi", gamePegi.name())
                                        .build()
                                , MediaType.APPLICATION_JSON_TYPE)
                );

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            switch(response.readEntity(String.class)) {
                case "OK" : return true;
                case "NO_OK" : return false;
                default: return false;
            }

        } else {
            throw new ClientErrorException("Error connecting to Age Checker", response.getStatus());
        }
    }

}
