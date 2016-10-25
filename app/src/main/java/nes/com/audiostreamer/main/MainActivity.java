package nes.com.audiostreamer.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import nes.com.audiostreamer.MediaPlayerCallback;
import nes.com.audiostreamer.R;
import nes.com.audiostreamer.model.SingleMediaPlayer;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.model.gson.MwJsonPage;
import nes.com.audiostreamer.server.MwAPIInterface;
import nes.com.audiostreamer.server.RetrofitServiceCache;
import nes.com.audiostreamer.service.BackgroundService;
import nes.com.audiostreamer.util.MediaPlayerUtil;
import nes.com.audiostreamer.view.ProgressView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MediaPlayerCallback{
    private List<String> cateroryList;
    private boolean isPlaying = false;
    private boolean isFirstSong = true;
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    //private ProgressView progressView;
    public static SeekBar seekBar;
    private Intent serviceIntent;
    private String songUrl = "https://upload.wikimedia.org/wikipedia/commons/d/d9/Angelien_Eijsink_-_voice_-_nl_-_long.flac";   //placeholder url
    private Context context;
    private MwAPIInterface mwAPIService;
    @NonNull
    private Call<MwJsonObject> queryResponse ;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deneme);
        context = this;
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        nextButton = (FloatingActionButton)findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        cateroryList = getIntent().getStringArrayListExtra("category_list");
        //getNextSong();
        MediaPlayerUtil.delegate = this;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceToast("clicked");
                    playOrPauseSong();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextSong();
            }
        });

//Make sure you update Seekbar on UI thread

        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SingleMediaPlayer.getInstance(songUrl).seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

    }

    /*public void prepareSeekBar(){
        seekBar.setMax(SingleMediaPlayer.getInstance(songUrl).getDuration());
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(SingleMediaPlayer.getInstance(songUrl) != null){
                    int mCurrentPosition = SingleMediaPlayer.getInstance(songUrl).getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }
    */


    public void playOrPauseSong(){
        if(isPlaying) {     //pause
            MediaPlayerUtil.pause(SingleMediaPlayer.getInstance(songUrl));
        }else{              //continue or recreate
            lockPlayer();
            serviceIntent.putExtra("songUrl", songUrl);     //pass url to service
            this.startService(serviceIntent);        //play service
            MediaPlayerUtil.play(SingleMediaPlayer.getInstance(songUrl));
        }

    }
    public void getNextSong(){
        lockPlayer();
        MediaPlayerUtil.stop(SingleMediaPlayer.getInstance(songUrl));
        songUrl = getRandomAudio(getRandomCategory());
        //playOrPauseSong();
    }


    public void lockPlayer(){
        playButton.setClickable(false);
        nextButton.setClickable(false);
    }
    public void unlockPlayer(){
        playButton.setClickable(true);
        nextButton.setClickable(true);
    }

    public String getRandomCategory(){
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(cateroryList.size());
        return cateroryList.get(index);
    }

    public String getRandomAudio(final String categoryTitle){
        mwAPIService = RetrofitServiceCache.getService();
        queryResponse = mwAPIService.getRandomAudio(categoryTitle);
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d("categoryTitle",categoryTitle);
                Log.d("body",response.body().toString());
                if(response.body().getQuery()==null){       //remove category from list
                    cateroryList.remove(categoryTitle);
                    songUrl = getRandomAudio(getRandomCategory());
                }else{
                    //Log.d("error",response.errorBody().toString());
                    Random generator = new Random();
                    Object[] values = response.body().getQuery().getPages().values().toArray();
                    Object randomValue = values[generator.nextInt(values.length)];
                    replaceToast(((MwJsonPage)randomValue).getImageinfo()[0].toString());
                    Log.d("i",((MwJsonPage)randomValue).getImageinfo()[0].toString());
                    MainActivity.this.songUrl = ((MwJsonPage)randomValue).getImageinfo()[0].getUrl();
                    playOrPauseSong();
                }

            }

            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                replaceToast(Constant.NETWORK_FAILURE_MESSAGE);
                MainActivity.this.songUrl = "";
            }
        });
        return songUrl;
    }

    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void mediaPlayerPlaying() {
        //prepareSeekBar();
        isPlaying = true;
        unlockPlayer();
        setPlayingView();
    }

    @Override
    public void mediaPlayerStopped() {
        isPlaying = false;
        getNextSong();
        //setPausedView();
    }

    @Override
    public void mediaPlayerEndOfSong() {
        isPlaying = false;
        getNextSong();
        //setPausedView();
    }

    @Override
    public void mediaPlayerPaused() {
        isPlaying = false;
        setPausedView();
    }
    public void setPlayingView(){
       // playButton.setText("||");
    }
    public void setPausedView(){
       // playButton.setText(">");
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

        /*
    *Does not work on emulator
    *
    public boolean isNetworkConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }*/

    /*
    @Override
    public void mediaPlayerPrepared() {
        playButton.setText("||");
    }
    */

    /*
    @Override
    public void audioProcessFinish(ImageInfo output) {
        unlockPlayer();
        songUrl = output.getUrl();
        Log.d("i","title: " +output.getCanonicaltitle()
                +"\nmediatype: "+output.getMediatype()
                +"\nmimetype: "+output.getMime()
                +"\nuser: "+output.getUser()
                +"\nurl: "+output.getUrl());
        Toast.makeText(getBaseContext(), "title: " +output.getCanonicaltitle()
                +"\nmediatype: "+output.getMediatype()
                +"\nmimetype: "+output.getMime()
                +"\nuser: "+output.getUser()
                +"\nurl: "+output.getUrl(),
                Toast.LENGTH_LONG).show();
    }*/

}
