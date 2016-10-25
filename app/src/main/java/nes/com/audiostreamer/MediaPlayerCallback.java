package nes.com.audiostreamer;

/**
 * Created by nesli on 24.10.2016.
 */

public interface MediaPlayerCallback {
    //public void mediaPlayerPrepared();
    public void mediaPlayerPlaying();
    public void mediaPlayerStopped();
    public void mediaPlayerPaused();
    public void mediaPlayerEndOfSong();
}
