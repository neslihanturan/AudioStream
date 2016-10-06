package nes.com.audiostreamer.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import nes.com.audiostreamer.service.BackgroundService;

/**
 * Created by nesli on 06.10.2016.
 */

public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        if (action.equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            // signal your service to stop playback
        }else{
            if(Constant.ACTION_PLAY.equals(action)) {
                Log.v("i","Pressed Play");
            } else if(Constant.ACTION_STOP.equals(action)) {
                Log.v("i","Pressed Stop");
            } else if(Constant.ACTION_CLOSE.equals(action)) {
                Log.v("i","Pressed Close");
            }
        }
    }

}

