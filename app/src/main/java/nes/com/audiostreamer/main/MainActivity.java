package nes.com.audiostreamer.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import nes.com.audiostreamer.MediaPlayerCallback;
import nes.com.audiostreamer.R;
import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.mediaplayer.MediaPlayerObserver;
import nes.com.audiostreamer.server.callback.RandomAudioCallback;
import nes.com.audiostreamer.service.BackgroundService;
import nes.com.audiostreamer.util.MediaPlayerController;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;

public class MainActivity extends AppCompatActivity implements MediaPlayerCallback, RandomAudioCallback{
    private List<String> categoryList;
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    private TextView textView;
    private AudioFile audioFile =  null;
    private Intent serviceIntent;
    private Context context;
    private Toast toast;
    private boolean isPlaying = false;

    public static SeekBar seekBar;
    public static Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deneme);
        context = this;
        handler = new Handler();
        serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        categoryList = getIntent().getStringArrayListExtra("category_list");
        MediaPlayerController.delegate = this;
        initViews();
        setListeners();
    }

    public void initViews(){
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        nextButton = (FloatingActionButton)findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView);
    }
    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPauseSong();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });
        MediaPlayerObserver mediaPlayerObserver = new MediaPlayerObserver();
        handler.postDelayed(mediaPlayerObserver, 100);
    }

    public void playOrPauseSong(){
        try {
            if(isPlaying) {     //pause
                MediaPlayerController.pause();
            }else{              //continue or recreate
                if(audioFile==null){
                    playNextSong();
                    return;
                }
                lockPlayer();
                serviceIntent.putExtra("songUrl", audioFile.getUrl());     //pass url to service
                this.startService(serviceIntent);        //play service TODO: do same in next song too
                MediaPlayerController.play(audioFile.getUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playNextSong(){
        lockPlayer();
        WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this);
        //rest part of operation is onSuccessCommonsCategoryData of getRandomAudio method
    }
    public void lockPlayer(){
        playButton.setClickable(false);
        nextButton.setClickable(false);
    }
    public void unlockPlayer(){
        playButton.setClickable(true);
        nextButton.setClickable(true);
    }

    @Override
    public void mediaPlayerPlaying() {
        isPlaying = true;
        unlockPlayer();
        setPlayingView();
    }
    @Override
    public void mediaPlayerPaused() {
        unlockPlayer();
        setPausedView();
    }
    @Override
    public void mediaPlayerEndOfSong() {
        playNextSong();
    }
    @Override
    public void mediaPlayerStopped() {
        playNextSong();
    }

    public void setPlayingView(){
        playButton.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
        textView.setText("Now Playing: "+audioFile.getTitle());
    }
    public void setPausedView(){
        isPlaying = false;
        playButton.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
        textView.setText("Now Paused: "+audioFile.getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();

        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }

    public String getRandomCategory(){
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(categoryList.size());
        return categoryList.get(index);
    }

    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onSuccessCommonsAudioData(AudioFile audioFile, boolean isCategoryNonEmpty) {
        try {                               //TODO: clean empty categories
            if(isCategoryNonEmpty){
                this.audioFile = audioFile;
            }else {
                WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this);     //if category is empty, find new category
            }
            MediaPlayerController.changeSong(this.audioFile.getUrl());
            MediaPlayerController.play(this.audioFile.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorCommonsAudioData() {
        replaceToast(Constant.NETWORK_FAILURE_MESSAGE);
    }

}
