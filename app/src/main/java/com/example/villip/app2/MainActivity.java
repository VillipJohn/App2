package com.example.villip.app2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView digital_clock;
    Typeface tf;
    private Timer myTimer;
    int mCurrentPeriod = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        digital_clock = (TextView) findViewById(R.id.tv_digital_clock);
        tf = Typeface.createFromAsset(getAssets(), "DS-DIGI.TTF");
        digital_clock.setTypeface(tf);

        StartTimer();
    }

    public void StartTimer() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 1000);
    }

    private void TimerMethod() {
// This method is called directly by the timer
// and runs in the same thread as the timer.

// We call the method that will work with the UI
// through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            mCurrentPeriod--;
            if(mCurrentPeriod > 0){
                String temp = (new SimpleDateFormat("mm:ss")).format(new Date(
                        mCurrentPeriod * 1000));
                digital_clock.setText(temp);
            } else {
                finish();
            }

        }
    };
}
