package com.ditagis.hcm.docsotanhoa.utities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

/**
 * Created by ThanLe on 11/28/2017.
 */

public class MyAlertByHardware extends Service {
    private static MyAlertByHardware instance;
    private Context context;
    private Ringtone r;
    private Vibrator vb;
    private RingtoneManager ringMan;

    private MyAlertByHardware(Context context) {
        vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        ringMan = new RingtoneManager(context);
        this.context = context;
    }

    public static MyAlertByHardware getInstance(Context context) {
        if (instance == null)
            instance = new MyAlertByHardware(context);
        return instance;
    }

    public void vibrate(boolean isLong) {
        int duration;
        if (isLong)
            duration = 2000;
        else
            duration = 200;
        vb.vibrate(duration);
    }


    public void playSound() {
//        MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.alert);
//        mPlayer.setVolume(70, 70);
//
////        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////        mPlayer.setLooping(false);
//        mPlayer.start();


        this.r.play();
    }

    public void stopSound() {
        if (this.r.isPlaying()) {
            this.r.stop();

        }

        ringMan.stopPreviousRingtone();
        Intent stopIntent = new Intent(context, MyAlertByHardware.class);
        context.stopService(stopIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
