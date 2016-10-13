package org.arquillian.microservices.gameservice.controller;

import org.arquillian.microservices.gameservice.boundary.AgeCheckerGateway;
import org.arquillian.microservices.gameservice.boundary.GamesGateway;
import org.arquillian.microservices.gameservice.entity.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GamesController {

    @Inject
    GamesGateway gamesGateway;

    @Inject
    AgeCheckerGateway ageCheckerGateway;

    public Optional<Game> findGameAndCheckPegi(final int userAge, final String title) {
        final Optional<Game> game = gamesGateway.findGameByTitle(title);

        return game.map(g -> {
                            if (ageCheckerGateway.canViewGameInformation(userAge, g.getPegi())) {
                                return game;
                            } else {
                                Optional<Game> forbidden = Optional.empty();
                                return forbidden;
                            }
                        })
                .orElse(game);
    }
}
