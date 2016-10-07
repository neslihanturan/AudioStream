package nes.com.audiostreamer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;


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
                public void onPrepared(MediaPlayer mp) {
                    Log.d("i","music is playing");
                    if(!mp.isPlaying()) {
                        mp.start();
                    }
                }
            });
            this.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.release();
                    mp = null;
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

    public void setSong(String song){
        //later
    }

    public String getSong(String song){
        //later
        return "";
    }
}
