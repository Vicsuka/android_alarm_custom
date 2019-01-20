package hu.bme.aut.myalarmclock.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

import hu.bme.aut.myalarmclock.AddActivity;
import hu.bme.aut.myalarmclock.MainActivity;
import hu.bme.aut.myalarmclock.R;

public class AlarmService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    String ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    public void onHandleIntent(Intent intent) {
        Log.d("service", "start command");

        sendNotification("Ébresztő!");
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "start command");
        final NotificationManager alarmNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.Instance.getClass());
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);

        Notification alarmNotificationStart  = new Notification.Builder(this).setContentTitle("Ébresztő!").setContentText(intent.getExtras().getString("Alarmtext")).setSmallIcon(R.drawable.notification).setContentIntent(pIntent).setAutoCancel(true).build();

        ringtone = intent.getExtras().getString("ringtone");



        String state = intent.getExtras().getString("extra");
        assert state != null;
        switch (state) {
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        if(!this.isRunning && startId == 1) {
            int min = 1;
            int max = 9;

            Random r = new Random();
            int random_number = r.nextInt(max - min + 1) + min;

            if (!(ringtone.equals("") || ringtone.equals("0") || ringtone.equals("Random"))) {
                random_number = Integer.parseInt(ringtone);
            }

            if (random_number == 1) {
                media_song = MediaPlayer.create(this, R.raw._1);
            }
            else if (random_number == 2) {
                media_song = MediaPlayer.create(this, R.raw._2);
            }
            else if (random_number == 3) {
                media_song = MediaPlayer.create(this, R.raw._3);
            }
            else if (random_number == 4) {
                media_song = MediaPlayer.create(this, R.raw._4);
            }
            else if (random_number == 5) {
                media_song = MediaPlayer.create(this, R.raw._5);
            }
            else if (random_number == 6) {
                media_song = MediaPlayer.create(this, R.raw._6);
            }
            else if (random_number == 7) {
                media_song = MediaPlayer.create(this, R.raw._7);
            }
            else if (random_number == 8) {
                media_song = MediaPlayer.create(this, R.raw._8);
            }
            else if (random_number == 9) {
                media_song = MediaPlayer.create(this, R.raw._9);
            }
            else {
                media_song = MediaPlayer.create(this, R.raw._10);
            }

            Log.d("ALARM", "wake up");
            media_song.start();


            alarmNotification.notify(1, alarmNotificationStart);

            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 0;

        }
        else {
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Destroyed", "destroyed");
        this.isRunning = false;
    }

    /*private void sendNotification(String msg) {
        //debug
        Log.d("AlarmService", "Ertesites kuldese...: " + msg);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AddActivity.class), 0);

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        //debug
        Log.d("AlarmService", "Ertesites elkuldve.");
    }*/
}
