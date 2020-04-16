package com.example.guitartuner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        text = findViewById(R.id.musicnoteTextView);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("" + convertHrzToNote(pitchInHz));
                    }
                });
            }


        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }



    public String convertHrzToNote(float input){


        if(80 < input && input <84){
            return "lowE";
        }
        if(108 < input && input <112){
            return "A";
        }
        if(144 < input && input <148){
            return "D";
        }
        if(194 < input && input <197){
            return "G";
        }
        if(244 < input && input <248){
            return "B";
        }
        if(327 < input && input <331){
            return "highE ";
        }

        return "nada";
    }

}
