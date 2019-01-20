package hu.bme.aut.myalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import hu.bme.aut.myalarmclock.Receiver.AlarmReceiver;

public class AddActivity extends Activity {
    
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AddActivity insta;

    public static final String mypreference = "mypref";
    public static final String Ringtone = "ringtoneKey";

    private String ringtone;
    private SharedPreferences sharedpreferences;

    private TextView alarmText;


    public static AddActivity instance() {
        return insta;
    }

    @Override
    public void onStart() {
        super.onStart();
        insta = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmText = (TextView) findViewById(R.id.alarmText);
        Button alarmToggle = (Button) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmToggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                        if (calendar.getTimeInMillis() <= System.currentTimeMillis())
                            calendar.add(Calendar.HOUR_OF_DAY, 24);

                        /*int hour = alarmTimePicker.getCurrentHour();
                        int minute = alarmTimePicker.getCurrentMinute();

                        String hour_string = String.valueOf(hour);
                        String minute_string = String.valueOf(minute);

                        if (minute < 10) {
                            minute_string = "0"+ String.valueOf(minute);
                            }*/


                        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                        if (sharedpreferences.contains(Ringtone)) {
                            System.out.println(sharedpreferences.getString(Ringtone, ""));
                            ringtone = sharedpreferences.getString(Ringtone, "");
                        } else {
                            ringtone = "0";
                        }



                        Intent myIntent = new Intent(AddActivity.this, AlarmReceiver.class);
                        myIntent.putExtra("extra", "on");
                        myIntent.putExtra("Alarmtext", alarmText.getText().toString());
                        myIntent.putExtra("Alarm", calendar);
                        myIntent.putExtra("ringtone", ringtone);
                        pendingIntent = PendingIntent.getBroadcast(AddActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        //tovabbadjuk a mainacitivitynek
                        Intent profileIntent = new Intent(AddActivity.this, MainActivity.Instance.getClass());
                        profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        profileIntent.putExtra("Alarm", calendar);
                        profileIntent.putExtra("Intent", pendingIntent);
                        profileIntent.putExtra("Myintent", myIntent);
                        startActivity(profileIntent);

                        //alarm betervezese
                        //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                        //text
                        //setAlarmText("Ébresztés beállítva: " + alarmTimePicker.getCurrentHour() + " : " + alarmTimePicker.getCurrentMinute());
                    }
                }
        );
    }

    //Ha megnyomja az ebresztest allito gombot
    public void onToggleClicked(View view) {
        //ha bekapcsolva van
        if (((ToggleButton) view).isChecked()) {
            //ido leolvasasa, betaplalas
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            if (calendar.getTimeInMillis() <= System.currentTimeMillis())
                calendar.add(Calendar.HOUR_OF_DAY, 24);

            /*int hour = alarmTimePicker.getCurrentHour();
            int minute = alarmTimePicker.getCurrentMinute();

            String hour_string = String.valueOf(hour);
            String minute_string = String.valueOf(minute);

            if (minute < 10) {
                minute_string = "0"+ String.valueOf(minute);
            }*/



            Intent myIntent = new Intent(AddActivity.this, AlarmReceiver.class);
            myIntent.putExtra("extra", "on");
            pendingIntent = PendingIntent.getBroadcast(AddActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //tovabbadjuk a mainacitivitynek
            Intent profileIntent = new Intent(AddActivity.this, MainActivity.Instance.getClass());
            profileIntent.putExtra("Alarm", calendar);
            profileIntent.putExtra("Intent", pendingIntent);
            profileIntent.putExtra("Myintent", myIntent);
            startActivity(profileIntent);

            //alarm betervezese
            //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            //text
            //setAlarmText("Ébresztés beállítva: " + alarmTimePicker.getCurrentHour() + " : " + alarmTimePicker.getCurrentMinute());





        } else {
            //kiszedjuk az ebresztest NEM HASZNALT
            alarmManager.cancel(pendingIntent);
        }
    }
}
