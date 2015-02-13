package com.urlshort;

/**
 * @author SoftwareSpot
 */
public class UrlShort {
    private final int id;

    private final String shortId;

    private final String url;

    private final String added;

    public UrlShort(int id, String shortId, String url, String added) {
        this.added = added;
        this.id = id;
        this.shortId = shortId;
        this.url = url;
    }

    public final String getAdded() {
        return added;
    }

    public final int getId() {
        return id;
    }

    public final String getShortId() {
        return shortId;
    }

    public final String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("%d: %s, %s, %s", id, url, shortId, added);
    }
}
