package nes.com.audiostreamer.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.model.SingleMediaPlayer;
import nes.com.audiostreamer.model.Song;
import nes.com.audiostreamer.model.SongAdapter;
import nes.com.audiostreamer.service.BackgroundService;
import nes.com.audiostreamer.util.MediaPlayerUtil;

public class MainActivity extends AppCompatActivity {
    SingleMediaPlayer mediaPlayer;
    String songUrl = "https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg";
    private List<Song> songList;
    boolean isPlaying = false;
    boolean isNewSong = false;
    public static int position = -1;
    public static int oldPosition = -1;
    ListView songListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////////////////////////////
        songList = new ArrayList<Song>();
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        /////////////////////////////////////////////////
        final Button playButton = (Button)findViewById(R.id.playButton);
        startService(new Intent(MainActivity.this, BackgroundService.class));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mediaPlayer = SingleMediaPlayer.getInstance(songUrl);
                if(isNewSong) {         //stop to recreate
                    MediaPlayerUtil.stop(SingleMediaPlayer.getInstance(songUrl)); //stop exsisting media player obj
                    songUrl = songList.get(position).getUrl();      //change current songUrl
                    //songListView.setItemChecked(position, true);  //select listview item
                    isPlaying = false;      //not playing yet
                    isNewSong = false;      //not set until detect a new song

                }
                if(isPlaying) {     //pause
                    MediaPlayerUtil.pause(SingleMediaPlayer.getInstance(songUrl));
                    playButton.setText(">");
                }else{              //continue or recreate
                    startService(new Intent(MainActivity.this, BackgroundService.class));
                    MediaPlayerUtil.start(SingleMediaPlayer.getInstance(songUrl));
                    playButton.setText("||");
                }
                isPlaying=!isPlaying;
            }
        });


        songListView = (ListView) findViewById(R.id.listView);
        final SongAdapter adapter = new SongAdapter(this, songList);
        songListView.setAdapter(adapter);
        // set zeroth element selected default
        //songListView.performItemClick( songListView.getAdapter().getView(0, null, null), 0, songListView.getAdapter().getItemId(0));
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
                songChangeCheck(position);
                //changeListItemStatus((TextView) view.findViewById(R.id.playing));
                playButton.performClick();
            }
        });

    }

    public void songChangeCheck(int position){
        if( MainActivity.position != position) {
            MainActivity.oldPosition = MainActivity.position;
            MainActivity.position = position;
            isNewSong = true;           //we need to recreate mediaplayer singletone with new song
        }
    }

    /*public void changeListItemStatus(TextView textView) {
        if(isNewSong || !isPlaying) {   //new song but not yet playing, or paused song
            textView.setText("||");
        }else{
            textView.setText(">");
        }
    }*/
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

}
