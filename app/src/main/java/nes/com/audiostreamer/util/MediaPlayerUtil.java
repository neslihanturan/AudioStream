package nes.com.audiostreamer.util;

import android.media.MediaPlayer;

import nes.com.audiostreamer.model.SingleMediaPlayer;

/**
 * Created by nesli on 07.10.2016.
 */


public final class MediaPlayerUtil{
    //public static boolean isMediaPlayerReady = false;

    //can be called for resume playback and start to play
    public static void start(MediaPlayer mediaPlayer){
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    public static void pause(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void stop(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        SingleMediaPlayer.nullifySingleMediaPlayer();
        //isMediaPlayerReady = false;
    }

    public static void turnVolumeToLow(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume(0.1f, 0.1f);
        }
    }


}
