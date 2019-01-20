package hu.bme.aut.myalarmclock.Receiver;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import hu.bme.aut.myalarmclock.AddActivity;
import hu.bme.aut.myalarmclock.Service.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {


        Intent service_intent = new Intent(context, AlarmService.class);
        service_intent.putExtra("extra", (String)intent.getExtras().get("extra"));
        service_intent.putExtra("Alarmtext", (String)intent.getExtras().get("Alarmtext"));
        service_intent.putExtra("ringtone", (String)intent.getExtras().get("ringtone"));
        context.startService(service_intent);

        /*
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        Log.d("AlarmReceiver", "Ebreszto aktivalasa....");
        //this will send a notification message
        /*ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);*/

    }
}
