package com.customCollectors;

import java.util.HashSet;
import java.util.Set;

public class Movie {
    private String title;
    private int releaseYear;

    private Set<Actor> actors = new HashSet<>();

    public Movie(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public Set<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return String.format("Movie{title=%s,releaseYear=%d,actors=%s}", this.title, this.releaseYear,
                this.actors.toString());
    }
}