package com.schultebraucks.lasse.countdowntimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 20 * 60 * 1000; // 20 minutes

    private Button mButtonStartStop;
    private Button mButtonSubtractTime;
    private Button mButtonReset;
    private TextView mEditTextTimer;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonStartStop = findViewById(R.id.buttonStartStop);
        mButtonSubtractTime = findViewById(R.id.buttonSubtractTime);
        mButtonReset = findViewById(R.id.buttonReset);
        mEditTextTimer = findViewById(R.id.textTimer);

        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonSubtractTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractTimeFromTimer();
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    private void startTimer()  {
        createTimer(mTimeLeftInMillis);
        mButtonStartStop.setText("Stop");
        mTimerRunning = true;
    }

    private void createTimer(long time) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimeLeftInMillis = time;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mEditTextTimer.setText("Time is up");
                mTimerRunning = false;
                mButtonStartStop.setText("Start");
            }
        }.start();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartStop.setText("Resume");
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mEditTextTimer.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mButtonStartStop.setText("Start");
        updateCountDownText();
    }

    private void subtractTimeFromTimer() {
        long oneMinute = 60 * 1000;
        mTimeLeftInMillis -= oneMinute;
        createTimer(mTimeLeftInMillis);
    }


}
