package nes.com.audiostreamer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import nes.com.audiostreamer.util.MediaPlayerController;


/**
 * Created by nesli on 07.10.2016.
 */

public class SingleMediaPlayer extends MediaPlayer {
    private static SingleMediaPlayer mInstance = null;
    public int state;
    //public static MediaPlayerObserver mediaPlayerObserver = null;


    public static SingleMediaPlayer getInstance(){
        try {
            if(mInstance == null)
            {
                mInstance = new SingleMediaPlayer();
            }
        } catch (IOException e) {
                e.printStackTrace();
            }
        return mInstance;
    }
    private SingleMediaPlayer() throws IOException {
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.state = MediaPlayerState.STATE_IDLE;
    }
    public static void setDataMediaPlayer(String songUrl) throws IOException {
        mInstance.setDataSource(songUrl);
        mInstance.state = MediaPlayerState.STATE_INITIALIZED;
        //mInstance.mediaPlayerObserver = new MediaPlayerObserver();
    }
    public static void prepareMediaPlayer(final String songUrl) throws IOException {
        mInstance.prepareAsync();
        mInstance.state = MediaPlayerState.STATE_PREPARING;
        mInstance.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mInstance.state = MediaPlayerState.STATE_PREPARED;
                    MediaPlayerController.handlePendingAction(songUrl);
                   // new Thread(mInstance.mediaPlayerObserver).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mInstance.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mInstance.state = MediaPlayerState.STATE_ERROR;
                //mInstance.mediaPlayerObserver.stop();
                return false;
            }
        });
        mInstance.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MediaPlayerController.delegate.mediaPlayerEndOfSong();
                mInstance.state = MediaPlayerState.STATE_PLAYBACK_COMPLETED;
               // mInstance.mediaPlayerObserver.stop();
            }
        });
    }
    public void playMediaPlayer(){
        mInstance.start();
        mInstance.state = MediaPlayerState.STATE_STARTED;
    }
    public void pauseMediaPlayer(){
        mInstance.pause();
        mInstance.state = MediaPlayerState.STATE_PAUSED;
    }
    public void stopMediaPlayer(){
        mInstance.stop();
        mInstance.state = MediaPlayerState.STATE_STOPPED;
    }
    public void releaseMediaPlayer(){
        mInstance.release();
        mInstance.state = MediaPlayerState.STATE_RELEASED;
    }
    public void resetMediaPlayer(){
        mInstance.reset();
        mInstance.state = MediaPlayerState.STATE_IDLE;
    }


    /*public static void nullifySingleMediaPlayer() {
        mInstance = null;
    }
    */

/*
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
                    MediaPlayerController.isMediaPlayerReady = true;
                    MediaPlayerController.play(mInstance);
                }
                //delegate.mediaPlayerIsReady();
            }
        });
        mInstance.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MediaPlayerController.endOfSong(mInstance);
            }
        });


    }
    */

    public String getSong(String song){
        //TODO
        return "";
    }

}
