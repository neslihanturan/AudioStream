package nes.com.audiostreamer.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import nes.com.audiostreamer.model.mediaplayer.SingleMediaPlayer;
import nes.com.audiostreamer.service.BackgroundService;
import nes.com.audiostreamer.util.MediaPlayerController;

/**
 * Created by nesli on 06.10.2016.
 */

public class MusicIntentReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer = null;
    String songUrl;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();

        songUrl = getExtra(intent);
        mediaPlayer = SingleMediaPlayer.getInstance();

        if (action.equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
        }else{
            if(Constant.ACTION_PLAY.equals(action)) {
                try {
                    MediaPlayerController.play(songUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("i","Pressed Play");
            } else if(Constant.ACTION_STOP.equals(action)) {
                try {
                    MediaPlayerController.pause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("i","Pressed Stop");
            } else if(Constant.ACTION_CLOSE.equals(action)) {
                ctx.stopService(new Intent(ctx, BackgroundService.class));
                Log.v("i","Pressed Close");
            }
        }
    }

    private String getExtra(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras == null) {
           return null;
        } else {
            return extras.getString("songUrl");
        }
    }

}

