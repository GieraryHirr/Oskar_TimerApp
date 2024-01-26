package com.example.timerapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Timer implements Runnable {
    boolean isPaused = false;
    boolean isCleared = false;
    private final Context context;
    private final TextView newTimerTextView;
    private final long timeInMillis;

    public Timer(Context context,TextView newTimerTextView, long timeInMillis) {
        this.context = context;
        this.newTimerTextView = newTimerTextView;
        this.timeInMillis = timeInMillis;
    }
    @Override
    public void run() {
        countDown(timeInMillis);
    }

    private void countDown(long millis) {
        try {
            long timeInMillis = millis;
            while (timeInMillis >= 0 && !isCleared) {
                if(!isPaused) {
                    NumberFormat f = new DecimalFormat("00");
                    long hour = timeInMillis / 3600000;
                    long min = (timeInMillis / 60000) % 60;
                    long sec = (timeInMillis / 1000) % 60;
                    newTimerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    Thread.sleep(1000);
                    timeInMillis -= 1000;
                } else Thread.sleep(1000);

                if(timeInMillis == 0 ) {
                    playAlarm();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean pause() {
        isPaused = !isPaused;
        return isPaused;
    }

    public void clear() {
        isCleared = true;
    }

    private void playAlarm() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.start();
    }
}