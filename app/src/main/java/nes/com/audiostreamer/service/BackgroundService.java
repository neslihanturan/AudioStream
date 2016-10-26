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

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.main.Constant;

public class BackgroundService extends Service implements AudioManager.OnAudioFocusChangeListener{
    private MediaPlayer mediaPlayer = null;
    private Notification notification;
    private String songUrl;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForegroundNotification();
        songUrl = getSongUrl(intent);
        return START_STICKY;
    }

    public void startForegroundNotification(){
        //Play intent
        Intent playIntent = new Intent();
        playIntent.setAction(Constant.ACTION_PLAY);
        playIntent.putExtra("songUrl",songUrl);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 12345, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Stop intent
        Intent stopIntent = new Intent();
        stopIntent.setAction(Constant.ACTION_STOP);
        stopIntent.putExtra("songUrl",songUrl);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 12345, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Close intent
        Intent closeIntent = new Intent();
        closeIntent.setAction(Constant.ACTION_CLOSE);
        closeIntent.putExtra("songUrl",songUrl);
        PendingIntent closePendingIntent = PendingIntent.getBroadcast(this, 12345, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(this)
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
    }

   /* @Override
    public void onDestroy() {
        MediaPlayerController.stop(songUrl);
    }*/


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                //MediaPlayerController.play(SingleMediaPlayer.getInstance(songUrl));
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
               // MediaPlayerController.stop(SingleMediaPlayer.getInstance(songUrl));
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
               // MediaPlayerController.pause(SingleMediaPlayer.getInstance(songUrl));
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
               // MediaPlayerController.turnVolumeToLow(mediaPlayer);
                break;
        }

    }

    public String getSongUrl(Intent intent){
        return intent.getStringExtra("songUrl");
    }

}
