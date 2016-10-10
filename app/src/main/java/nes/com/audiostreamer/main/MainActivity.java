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
    public static int position = 0;

    private List<Song> songList;
    private boolean isPlaying = false;
    private boolean isNewSong = false;

    private Button playButton;
    private Button prevButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSongManual();        // TODO: get real data
        playButton = (Button)findViewById(R.id.playButton);
        prevButton = (Button)findViewById(R.id.prevButton);
        nextButton = (Button)findViewById(R.id.nextButton);

        ListView songListView = (ListView) findViewById(R.id.listView);
        final SongAdapter adapter = new SongAdapter(this, songList);
        songListView.setAdapter(adapter);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prepareToNewSong(adapter, position);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPauseSong();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ((MainActivity.position-1 < 0) ? songList.size()-1 : MainActivity.position-1);  //update new position to next, if EOL turn to end
                prepareToNewSong(adapter, position);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (MainActivity.position+1)%songList.size();       //update new position to next, if EOL turn to beginning
                prepareToNewSong(adapter, position);
            }
        });
    }

    public void prepareToNewSong(SongAdapter adapter, int position){
        adapter.setSelectedIndex(position);
        adapter.notifyDataSetChanged();
        songChangeCheck(position);
        playButton.performClick();
    }

    public void songChangeCheck(int position){      //checks if the song changed
        if( MainActivity.position != position) {
            MainActivity.position = position;
            isNewSong = true;           //we need to recreate mediaplayer singletone with new song
        }
    }

    public void playOrPauseSong(){
        String songUrl = songList.get(position).getUrl();
        if(isNewSong) {         //stop to recreate
            MediaPlayerUtil.stop(SingleMediaPlayer.getInstance(songUrl)); //stop exsisting media player obj
            isPlaying = false;      //not playing yet
            isNewSong = false;      //not set until detect a new song
        }
        if(isPlaying) {     //pause
            MediaPlayerUtil.pause(SingleMediaPlayer.getInstance(songUrl));
            playButton.setText(">");        //update button look
        }else{              //continue or recreate
            startService(new Intent(MainActivity.this, BackgroundService.class));
            MediaPlayerUtil.start(SingleMediaPlayer.getInstance(songUrl));
            playButton.setText("||");       //update button look
        }
        isPlaying=!isPlaying;
    }

    public void setSongManual(){
        songList = new ArrayList<>();
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("Lucky One","George Woods","https://i.cloudup.com/kitGU79aWK.mp3",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
        songList.add(new Song("title","artist","https://upload.wikimedia.org/wikipedia/en/4/45/ACDC_-_Back_In_Black-sample.ogg",10));
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

}
