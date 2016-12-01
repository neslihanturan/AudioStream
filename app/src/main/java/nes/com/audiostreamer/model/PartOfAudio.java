package nes.com.audiostreamer.model;

/**
 * Created by nesli on 26.11.2016.
 */

public class PartOfAudio extends AudioFile {
    private float startedByte;
    private float endByte;
    private float startedSecond;
    private float endSecond;

    public PartOfAudio(String category, String title, String url, float startedByte, float endByte, float startedSecond, float endSecond) {
        super(category, title, url);
        this.startedByte = startedByte;
        this.endByte = endByte;
        this.startedSecond = startedSecond;
        this.endSecond = endSecond;
    }

    public float getStartedByte() {
        return startedByte;
    }

    public void setStartedByte(float startedByte) {
        this.startedByte = startedByte;
    }

    public float getEndByte() {
        return endByte;
    }

    public void setEndByte(float endByte) {
        this.endByte = endByte;
    }

    public float getStartedSecond() {
        return startedSecond;
    }

    public void setStartedSecond(float startedSecond) {
        this.startedSecond = startedSecond;
    }

    public float getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(float endSecond) {
        this.endSecond = endSecond;
    }
}
