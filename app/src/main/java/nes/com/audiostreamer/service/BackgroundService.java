package nes.com.audiostreamer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.main.Constant;

public class BackgroundService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{
    MediaPlayer mediaPlayer = null;
    String songUrl = "https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg";
    //String songName= "Black in Black";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepareAsync(); //to make it on separate thread
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Play intent
        Intent playIntent = new Intent();
        playIntent.setAction(Constant.ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 12345, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Stop intent
        Intent stopIntent = new Intent();
        stopIntent.setAction(Constant.ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 12345, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Close intent
        Intent closeIntent = new Intent();
        closeIntent.setAction(Constant.ACTION_CLOSE);
        PendingIntent closePendingIntent = PendingIntent.getBroadcast(this, 12345, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Music Player")
                .setTicker("Music Player")
                .setContentText("Black in Black")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_pause,
                        "Stop", stopPendingIntent)
                .addAction(android.R.drawable.ic_media_play,
                        "Play", playPendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        "Close", closePendingIntent)
                .build();

        startForeground(Constant.NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("i","music is playing");
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.release();
        mp = null;
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
