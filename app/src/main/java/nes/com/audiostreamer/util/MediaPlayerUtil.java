package nes.com.audiostreamer.util;

import android.media.MediaPlayer;

import nes.com.audiostreamer.MediaPlayerCallback;
import nes.com.audiostreamer.model.SingleMediaPlayer;

/**
 * Created by nesli on 07.10.2016.
 */


public final class MediaPlayerUtil{
    public static boolean isMediaPlayerReady = false;
    public static MediaPlayerCallback delegate = null;

    //can be called for resume playback and play to play
    public static void play(MediaPlayer mediaPlayer){
        if (isMediaPlayerReady && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setVolume(1.0f, 1.0f);
            delegate.mediaPlayerPlaying();
        }
    }

    public static void pause(MediaPlayer mediaPlayer){
        if (isMediaPlayerReady && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            delegate.mediaPlayerPaused();
        }
    }

    public static void stop(MediaPlayer mediaPlayer){
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                delegate.mediaPlayerStopped();
            }

            mediaPlayer.release();
            SingleMediaPlayer.nullifySingleMediaPlayer();
            isMediaPlayerReady = false;
    }

    public static void endOfSong(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            delegate.mediaPlayerEndOfSong();
        }
        mediaPlayer.release();
        SingleMediaPlayer.nullifySingleMediaPlayer();
        isMediaPlayerReady = false;
    }

    public static void turnVolumeToLow(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume(0.1f, 0.1f);
        }
    }
}
