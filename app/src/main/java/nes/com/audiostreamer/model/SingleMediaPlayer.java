package nes.com.audiostreamer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import nes.com.audiostreamer.service.BackgroundService;
import nes.com.audiostreamer.util.MediaPlayerUtil;


/**
 * Created by nesli on 07.10.2016.
 */

public class SingleMediaPlayer extends MediaPlayer {
    private static SingleMediaPlayer mInstance = null;

    private SingleMediaPlayer(String songUrl){
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.setDataSource(songUrl);
            this.prepareAsync(); //to make it on separate thread
            this.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.d("i","music is playing");
                    if(!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        MediaPlayerUtil.isMediaPlayerReady = true;
                    }
                }
            });
            this.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SingleMediaPlayer getInstance(String songUrl){
        if(mInstance == null)
        {
            mInstance = new SingleMediaPlayer(songUrl);
        }
        return mInstance;
    }

    public static void nullifySingleMediaPlayer() {
        mInstance = null;
    }

    public void setSong(String song){
        //TODO
    }

    public String getSong(String song){
        //TODO
        return "";
    }

}
