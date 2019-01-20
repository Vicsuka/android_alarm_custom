package hu.bme.aut.myalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import hu.bme.aut.myalarmclock.Adapter.AlarmAdapter;
import hu.bme.aut.myalarmclock.Fragments.informationFragment;
import hu.bme.aut.myalarmclock.Receiver.AlarmReceiver;


public class MainActivity extends AppCompatActivity {
    public ArrayList<Calendar> allAlarms = new ArrayList<>();
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    public static final String mypreference = "mypref";
    public static final String Age = "ageKey";
    public static final String Snooze = "snoozeKey";
    public static final String Hardcore = "hardcoreKey";
    public static final String Deficit = "deficitKey";
    public static final String Snooze_amount = "snoozeamountKey";
    public static final String Alarm = "alarmKey";

    public static MainActivity Instance;

    boolean snooze;
    boolean hardcore;
    Integer age;
    double sleepDeficit;
    Integer snooze_amount;
    String ringtone;

    SharedPreferences sharedpreferences;

    boolean cancelCondition;
    boolean snoozeCondition;
    AlarmAdapter adapter;
    ListView listView;
    Button cancelBtn;
    Button snoozeBtn;

    TextView emptyText;

    TextView deficitValue;

    Intent myIntent;

    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public void onStart() {

        super.onStart();
        Bundle bundle = getIntent().getExtras();
        Calendar c1;
        allAlarms.clear();


        Instance = this;

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Age)) {
            System.out.println(sharedpreferences.getString(Age, ""));
            age = Integer.valueOf(sharedpreferences.getString(Age, ""));
        }
        if (sharedpreferences.contains(Snooze)) {
            System.out.println(sharedpreferences.getBoolean(Snooze, false));
            snooze = sharedpreferences.getBoolean(Snooze, false);
        }

        if (sharedpreferences.contains(Hardcore)) {
            System.out.println(sharedpreferences.getBoolean(Hardcore, false));
            hardcore = sharedpreferences.getBoolean(Hardcore, false);
        }

        if (sharedpreferences.contains(Deficit)) {
            System.out.println(sharedpreferences.getString(Deficit, ""));
            sleepDeficit = Double.valueOf(sharedpreferences.getString(Deficit, ""));
        }

        if (sharedpreferences.contains(Snooze_amount)) {
            System.out.println(sharedpreferences.getInt(Snooze_amount, 5));
            snooze_amount = sharedpreferences.getInt(Snooze_amount, 5);
        }



        deficitValue = (TextView) findViewById(R.id.deficit_value);

        if (bundle != null && !cancelCondition) {

            if (hardcore)
                cancelBtn.setVisibility(View.INVISIBLE);

            if (!snooze)
                snoozeBtn.setVisibility(View.INVISIBLE);

            if (bundle.containsKey("Alarm")) {
                c1 = (Calendar) bundle.get("Alarm");

                /*
                Here's how it works:
                The average sleep cycle is 90 minutes long.
                A typical night of sleep includes 5 full sleep cycles.
                90 x 5 = 450 minutes, or 7.5 hours.
                Starting at your wake time, work back 7.5 hours to find your bedtime.

                Newborns (0-3 months): Sleep range narrowed to 14-17 hours each day (previously it was 12-18)
                Infants (4-11 months): Sleep range widened two hours to 12-15 hours (previously it was 14-15)
                Toddlers (1-2 years): Sleep range widened by one hour to 11-14 hours (previously it was 12-14)
                Preschoolers (3-5): Sleep range widened by one hour to 10-13 hours (previously it was 11-13)
                School age children (6-13): Sleep range widened by one hour to 9-11 hours (previously it was 10-11)
                Teenagers (14-17): Sleep range widened by one hour to 8-10 hours (previously it was 8.5-9.5)
                Younger adults (18-25): Sleep range is 7-9 hours (new age category)
                Adults (26-64): Sleep range did not change and remains 7-9 hours
                Older adults (65+): Sleep range is 7-8 hours (new age category)

                Sleep debt or sleep deficit is the cumulative effect of not getting enough sleep. A large sleep debt may lead to mental or physical fatigue.
                */

                double multiplier = 1;
                int base = 8;
                if (age != null && !bundle.containsKey("Szundi")) {
                    if (age > 0) {
                        if (1 < age && age < 2) {
                            multiplier = 1.5;
                            base = 12;
                        }

                        if (2 < age && age < 5) {
                            multiplier = 1.4;
                            base = 11;
                        }
                        if (5 < age && age < 13) {
                            multiplier = 1.3;
                            base = 10;
                        }
                        if (13 < age && age < 17) {
                            multiplier = 1.2;
                            base = 9;
                        }

                        if (17 < age && age < 25) {
                            multiplier = 1.1;
                            base = 8;
                        }

                        if (25 < age && age < 64) {
                            multiplier = 1;
                            base = 8;
                        }

                        if (age > 64) {
                            multiplier = 1;
                            base = 7;
                        }
                    }

                    String strDt;

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.HOUR_OF_DAY, 1);
                    Date d = c.getTime();
                    Date d2 = c1.getTime();
                    SimpleDateFormat onlyHours = new SimpleDateFormat("HH");
                    strDt = onlyHours.format(d2.getTime() - d.getTime());

                    System.out.println("VISSZAMARADAS " + sleepDeficit);

                    System.out.println("KULONBSEG " + (base - Integer.valueOf(strDt)));

                    System.out.println("ALVASORASZARM " + Integer.valueOf(strDt));

                    if (Integer.valueOf(strDt) < base) {
                        sleepDeficit = sleepDeficit + (base - Integer.valueOf(strDt));
                    } else if (Integer.valueOf(strDt) > base) {
                        sleepDeficit = sleepDeficit - ((Integer.valueOf(strDt) - base )/2);
                    }

                    if (sleepDeficit > 24) {
                        c1.add(Calendar.MINUTE, (int) (30 * multiplier + ((int)sleepDeficit-24)));
                    }
                 }

                allAlarms.add(c1);
                if (cancelBtn != null && !hardcore)
                cancelBtn.setVisibility(View.VISIBLE);

                if ( allAlarms.size() > 0 && !hardcore) {
                    Calendar c4 = Calendar.getInstance();
                    Date d = c4.getTime();
                    Date d2 = allAlarms.get(0).getTime();
                    SimpleDateFormat onlyMinutes = new SimpleDateFormat("mm");
                    String strDt = onlyMinutes.format(d2.getTime() - d.getTime());

                    int diff = Integer.valueOf(strDt);
                    Log.d("DIFFERENCE", d2.getTime() + " - " + d.getTime() + " = " + diff);

                    if ((diff > -3) && snooze && !hardcore && d2.before(d)) {
                        snoozeBtn.setVisibility(View.VISIBLE);
                    } else {
                        snoozeBtn.setVisibility(View.INVISIBLE);
                    }

                }

            }

            if (bundle.containsKey("Myintent")) {
                myIntent =(Intent) bundle.get("Myintent");
            }

            if (bundle.containsKey("Intent") && allAlarms.size() != 0) {
                pendingIntent = (PendingIntent) bundle.get("Intent");
                alarmManager.set(AlarmManager.RTC_WAKEUP, allAlarms.get(0).getTimeInMillis(), pendingIntent);
            }





        }

        if (sleepDeficit != 0)
            deficitValue.setText(Double.toString(sleepDeficit));
        else
            deficitValue.setText("0");

        if (allAlarms.isEmpty()) {
            emptyText.setText(R.string.empty);
        } else {
            emptyText.setText("");
        }

        //System.out.println(allAlarms.size() + " darab ébresztő van");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Main activity beallitasa
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        cancelCondition = false;
        snoozeCondition = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context c = this;
        adapter = new AlarmAdapter(this,allAlarms);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        snoozeBtn = findViewById(R.id.alarm_snooze);

        emptyText = findViewById(R.id.emptyText);


        snoozeBtn.setVisibility(View.INVISIBLE);
        if (snoozeBtn != null)
            snoozeBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snoozeCondition = true;
                            myIntent.putExtra("extra", "off");
                            sendBroadcast(myIntent);
                            alarmManager.cancel(pendingIntent);

                            Calendar cal = Calendar.getInstance();
                            if (!(snooze_amount == null) && snooze_amount != 0) {
                                int s = snooze_amount;
                                Log.d("SNOOZE_AMOUNT", " amount: " + s);
                                cal.add(Calendar.MINUTE, s);
                            } else {
                                cal.add(Calendar.MINUTE, 5);
                            }

                            /*allAlarms.clear();
                            allAlarms.add(cal);*/

                            Context context = getApplicationContext();
                            CharSequence text = "Szundi beállítva!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            Intent profileIntent = new Intent(MainActivity.this, MainActivity.class);
                            profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            profileIntent.putExtra("Alarm", cal);
                            profileIntent.putExtra("Intent", pendingIntent);
                            profileIntent.putExtra("Myintent", myIntent);
                            profileIntent.putExtra("Snooze", snooze);

                            startActivity(profileIntent);

                            //alarmManager.set(AlarmManager.RTC, allAlarms.get(0).getTimeInMillis(), pendingIntent);


                        }
                    }
            );

        cancelBtn = findViewById(R.id.alarm_cancel);
        cancelBtn.setVisibility(View.INVISIBLE);
        if (cancelBtn != null)
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelCondition = true;
                        myIntent.putExtra("extra", "off");
                        sendBroadcast(myIntent);
                        alarmManager.cancel(pendingIntent);

                        Context context = getApplicationContext();
                        CharSequence text = "Ébresztés törölve!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        allAlarms.clear();
                        cancelBtn.setVisibility(View.INVISIBLE);
                        Intent profileIntent = new Intent(MainActivity.this ,MainActivity.class);
                        profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(profileIntent);


                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(profileIntent);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allAlarms.size() > 0) {
                    Log.d("alarm", "refresh");
                    Calendar c1 = allAlarms.get(0);
                    allAlarms.clear();
                    allAlarms.add(c1);
                    adapter = new AlarmAdapter(c,allAlarms);
                    listView.setAdapter(adapter);


                    if ( allAlarms.size() > 0) {
                        Calendar c4 = Calendar.getInstance();
                        Date d = c4.getTime();
                        Date d2 = allAlarms.get(0).getTime();
                        SimpleDateFormat onlyMinutes = new SimpleDateFormat("mm");
                        String strDt = onlyMinutes.format(d2.getTime() - d.getTime());

                        int diff = Integer.valueOf(strDt);
                        Log.d("DIFFERENCE", d2.getTime() + " - " + d.getTime() + " = " + diff);

                        if ((diff > -3) && snooze && !hardcore && d2.before(d)) {
                            snoozeBtn.setVisibility(View.VISIBLE);
                        } else {
                            snoozeBtn.setVisibility(View.INVISIBLE);
                        }
                    }

                    Context context = getApplicationContext();
                    CharSequence text = "Frissítve!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedpreferences != null ) {
                    if (Double.valueOf(sharedpreferences.getString(Deficit, "0")) == sleepDeficit) {
                        Context context = getApplicationContext();
                        CharSequence text = "Nincs mit menteni!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(Deficit, Double.toString(sleepDeficit));

                        editor.commit();

                        Context context = getApplicationContext();
                        CharSequence text = "Ébresztési statisztika elmentve!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }




                }
            }
        });


        FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new informationFragment().show(getSupportFragmentManager(), informationFragment.TAG);
                }
        });

        if (allAlarms.isEmpty()) {
            emptyText.setText(R.string.empty);
        } else {
            emptyText.setText("");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent profileIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(profileIntent);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}