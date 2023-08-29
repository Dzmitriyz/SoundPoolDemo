package com.example.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SoundPool sp;
    int iDdFX1 =-1;
    int iDdFX2 =-1;
    int iDdFX3 =-1;
    int nowPlaying = -1;
    float volume = .1f;
    int repeat = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonFX1 = findViewById(R.id.btnFX1);
        Button buttonFX2 = findViewById(R.id.btnFX2);
        Button buttonFX3 = findViewById(R.id.btnFX3);
        Button buttonStop = findViewById(R.id.btnStop);
        buttonFX1.setOnClickListener(this);
        buttonFX2.setOnClickListener(this);
        buttonFX3.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            sp = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        }else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        try {
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;
            descriptor = assetManager.openFd("fx1.ogg");
            iDdFX1 = sp.load(descriptor,0);
            descriptor = assetManager.openFd("fx2.ogg");
            iDdFX2 = sp.load(descriptor,0);
            descriptor = assetManager.openFd("fx3.ogg");
            iDdFX3 = sp.load(descriptor,0);
        }catch (IOException e){
            Log.e("error", "не удалось загрузить файл");
        }

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress/10f;
                sp.setVolume(nowPlaying,volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    final Spinner spinner = (Spinner) findViewById(R.id.spinner);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String temp = String.valueOf(spinner.getSelectedItem());
            repeat = Integer.valueOf(temp);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFX1:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(iDdFX1,volume,volume,0,repeat,1);
                break;
            case R.id.btnFX2:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(iDdFX2,volume,volume,0,repeat,1);
                break;
            case R.id.btnFX3:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(iDdFX3,volume,volume,0,repeat,1);
                break;
            case R.id.btnStop:
                sp.stop(nowPlaying);
                break;
        }
    }
}