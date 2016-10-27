package nes.com.audiostreamer.model.mediaplayer;

import java.util.concurrent.atomic.AtomicBoolean;

import nes.com.audiostreamer.main.MainActivity;

/**
 * Created by nesli on 26.10.2016.
 */

public class MediaPlayerObserver implements Runnable {
    private AtomicBoolean stop = new AtomicBoolean(false);

    public void stop() {
        stop.set(true);
    }

    @Override
    public void run() {
        if(SingleMediaPlayer.getInstance().state>= MediaPlayerState.STATE_PREPARED){
            MainActivity.seekBar.setMax(SingleMediaPlayer.getInstance().getDuration());
            MainActivity.seekBar.setProgress(SingleMediaPlayer.getInstance().getCurrentPosition());
        }
        MainActivity.handler.postDelayed(this, 100);
        /*while (!stop.get()) {
            try {
                MainActivity.seekBar.setProgress(SingleMediaPlayer.getInstance().getCurrentPosition());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
