package org.arquillian.microservices.gameservice.entity;

import javax.json.Json;
import javax.json.JsonObject;

public class Game {

    private Long id;
    private String title;
    private String developers;
    private String series;
    private Pegi pegi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Pegi getPegi() {
        return pegi;
    }

    public void setPegi(Pegi pegi) {
        this.pegi = pegi;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("title", getTitle())
                .add("developers", getDevelopers())
                .add("series", getSeries())
                .add("pegi", getPegi().name())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return title != null ? title.equals(game.title) : game.title == null;

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
