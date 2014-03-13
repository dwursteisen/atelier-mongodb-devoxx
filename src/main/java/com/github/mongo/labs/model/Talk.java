package com.github.mongo.labs.model;

/**
 * Created with IntelliJ IDEA.
 * User: david.wursteisen
 * Date: 13/03/14
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class Talk {

    private String title;

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

    @Override
    public String toString() {
        return "Talk{" +
                "title='" + title + '\'' +
                '}';
    }
}
