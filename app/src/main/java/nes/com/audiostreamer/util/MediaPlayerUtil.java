package nes.com.audiostreamer.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import nes.com.audiostreamer.model.SingleMediaPlayer;
import nes.com.audiostreamer.service.BackgroundService;

/**
 * Created by nesli on 07.10.2016.
 */


public final class MediaPlayerUtil{

    //can be called for resume playback and start to play
    public static void start(MediaPlayer mediaPlayer, String songUrl){
        if (mediaPlayer == null){
            mediaPlayer = SingleMediaPlayer.getInstance(songUrl);
        }
        else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        mediaPlayer.setVolume(1.0f, 1.0f);
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
        mediaPlayer = null;
    }

    public static void turnVolumeToLow(MediaPlayer mediaPlayer){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume(0.1f, 0.1f);
        }
    }


}
