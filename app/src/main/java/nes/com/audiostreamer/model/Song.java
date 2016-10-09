package nes.com.audiostreamer.model;

/**
 * Created by nesli on 09.10.2016.
 */

public class Song {
    private String title;
    private String artist;
    private String url;
    private int duration;

    public Song(String title, String artist, String url, int duration){
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.duration = duration;
    }

    public String getTitle(){ return title; }
    public String getArtist(){ return artist; }
    public String getUrl(){ return url; }
    public int getDuration(){ return duration; }

}
