package nes.com.audiostreamer.model;

/**
 * Created by nesli on 26.10.2016.
 */

public class AudioFile {
    public String category;
    public String title;
    public String url;

    public AudioFile(){

    }

    public AudioFile(String category, String title, String url) {
        this.category = category;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
