package com.schultebraucks.lasse.countdowntimer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button mButtonStartStop;
    private Button mButtonSubtractTime;
    private Button mButtonReset;
    private EditText mEditTextMinutes;
    private EditText mEditTextSeconds;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private Ringtone alarmRingtone;

    private long mStartTime = 1000 * 60 * 20;

    private long mTimeLeftInMillis = mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonStartStop = findViewById(R.id.buttonStartStop);
        mButtonSubtractTime = findViewById(R.id.buttonSubtractTime);
        mButtonReset = findViewById(R.id.buttonReset);
        mEditTextMinutes = findViewById(R.id.editTextMinutes);
        mEditTextSeconds = findViewById(R.id.editTextSeconds);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.alarmRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);


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
                pauseTimer();
                resetTimer();
                alarmRingtone.stop();
                mButtonReset.setText("Reset");
            }
        });

        updateCountDownText();
    }

    private void startTimer()  {
        mTimeLeftInMillis = 0;
        mTimeLeftInMillis += Integer.parseInt(mEditTextMinutes.getText().toString()) * 1000 * 60;
        mTimeLeftInMillis += Integer.parseInt(mEditTextSeconds.getText().toString()) * 1000;

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
                alarmRingtone.play();
                Toast.makeText(getApplicationContext(), "Time is up", Toast.LENGTH_SHORT).show();
                mButtonReset.setText("Stop alarm & Reset");
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

        String minutesText = String.format(Locale.getDefault(), "%02d", minutes);
        String secondsText = String.format(Locale.getDefault(), "%02d", seconds);

        mEditTextMinutes.setText(minutesText);
        mEditTextSeconds.setText(secondsText);
    }

    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimeLeftInMillis = mStartTime;
        mButtonStartStop.setText("Start");
        updateCountDownText();
    }

    private void subtractTimeFromTimer() {
        long oneMinute = 60 * 1000;
        mTimeLeftInMillis -= oneMinute;
        if (mTimeLeftInMillis < 0) {
            mTimeLeftInMillis = 0;
            updateCountDownText();
        }
        createTimer(mTimeLeftInMillis);
    }


}
