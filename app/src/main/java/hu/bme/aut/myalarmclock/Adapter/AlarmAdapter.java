package hu.bme.aut.myalarmclock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.bme.aut.myalarmclock.R;

public class AlarmAdapter extends ArrayAdapter<Calendar> {
    public AlarmAdapter(Context context, ArrayList<Calendar> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Calendar alarm = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_alarm_item, parent, false);
        }
        TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_time);
        TextView alarmRemaining = (TextView) convertView.findViewById(R.id.alarm_remaining_time);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("HH:mm:ss");
        String strDt = simpleDate.format(alarm.getTime());

        alarmTime.setText("Következő ébresztő: "+strDt);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        Date d = c.getTime();
        Date d2 = alarm.getTime();
        strDt = simpleDate.format(d2.getTime() - d.getTime());
        alarmRemaining.setText( " Hátralévő idő: "+ strDt);
        return convertView;
    }

}
