package com.github.mongo.labs.model;

/**
 * Created with IntelliJ IDEA. User: david.wursteisen Date: 13/03/14 Time: 13:46 To change this template use File | Settings | File
 * Templates.
 */
public class Talk {

    private String title;
    private Iterable<Speaker> speakers;

    public Talk(final String title) {
        this.title = title;
    }

    public Talk() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Iterable<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(final Iterable<Speaker> speakers) {
        this.speakers = speakers;
    }

}
