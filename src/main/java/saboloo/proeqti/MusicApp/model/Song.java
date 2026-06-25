package saboloo.proeqti.MusicApp.model;

public class Song {
    private final int id;
    private final String title;
    private final String artist;
    private final String streamUrl;

    public Song(int id, String title, String artist, String streamUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.streamUrl = streamUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    @Override
    public String toString() {
        return title + "  -  " + artist;
    }
}