package org.arquillian.microservices.gameservice.boundary;

import org.arquillian.microservices.gameservice.entity.Game;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class GamesGateway {

    private static Set<Game> games = new HashSet<>();

    @PostConstruct
    public void initDataset() {

        Game metalGearV = new Game();
        metalGearV.setId(1L);
        metalGearV.setDevelopers("Kojima Productions");
        metalGearV.setSeries("Metal Gear");
        metalGearV.setTitle("Metal Gear V: The Phantom Pain");
        metalGearV.setContentRating(18);

        Game uncharted4 = new Game();
        uncharted4.setId(2L);
        uncharted4.setDevelopers("Naughty Dog");
        uncharted4.setSeries("Uncharted");
        uncharted4.setTitle("Uncharted 4: A Thief's End");
        uncharted4.setContentRating(16);

        games.add(metalGearV);
        games.add(uncharted4);
    }

    public Optional<Game> findGameByTitle(String title) {
        return games.stream().filter(game -> game.getTitle().equalsIgnoreCase(title)).findFirst();
    }

}
