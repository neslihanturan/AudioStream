package nes.com.audiostreamer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import nes.com.audiostreamer.main.MainActivity;
import nes.com.audiostreamer.util.MediaPlayerUtil;


/**
 * Created by nesli on 07.10.2016.
 */

public class SingleMediaPlayer extends MediaPlayer {
    private static SingleMediaPlayer mInstance = null;
    private Runnable onEverySecond=new Runnable() {

        @Override
        public void run() {
            if(mInstance!=null){
                if(MainActivity.seekBar != null) {
                    MainActivity.seekBar.setProgress(mInstance.getCurrentPosition());
                }
                if(mInstance.isPlaying()) {
                    MainActivity.seekBar.postDelayed(onEverySecond, 1000);
                }
            }

        }
    };

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
                        //mediaPlayer.start();
                        MainActivity.seekBar.setMax(mediaPlayer.getDuration());
                        MainActivity.seekBar.postDelayed(onEverySecond, 1000);
                        MediaPlayerUtil.isMediaPlayerReady = true;
                        MediaPlayerUtil.play(mInstance);
                    }
                    //delegate.mediaPlayerIsReady();
                }
            });
            this.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    MediaPlayerUtil.endOfSong(mInstance);
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
    public static void playNewSong(String songUrl) throws IOException {
        mInstance.reset();
        mInstance.setDataSource(songUrl);
        mInstance.prepareAsync();
        mInstance.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("i","music is playing");
                if(!mediaPlayer.isPlaying()) {
                    //mediaPlayer.start();
                    MediaPlayerUtil.isMediaPlayerReady = true;
                    MediaPlayerUtil.play(mInstance);
                }
                //delegate.mediaPlayerIsReady();
            }
        });
        mInstance.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MediaPlayerUtil.endOfSong(mInstance);
            }
        });


    }

    public String getSong(String song){
        //TODO
        return "";
    }

}
