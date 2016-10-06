package nes.com.audiostreamer.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.service.BackgroundService;

public class MainActivity extends AppCompatActivity {
    boolean isPlaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //android.os.Debug.waitForDebugger();
        setContentView(R.layout.activity_main);
        Button playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                if(isPlaying){
                    startService(new Intent(MainActivity.this, BackgroundService.class));
                }else{

                }
            }
        });
    }
}
