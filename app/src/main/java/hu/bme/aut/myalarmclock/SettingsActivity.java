package hu.bme.aut.myalarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    SharedPreferences sharedpreferences;

    TextView age;
    Switch snooze;
    Switch hardcore;

    FloatingActionButton sendBtn;
    FloatingActionButton deleteBtn;

    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    int snooze_amount;

    Spinner sp;

    MediaPlayer media_song;
    private static Context context;
    boolean firstOpened;


    public static final String mypreference = "mypref";
    public static final String Age = "ageKey";
    public static final String Snooze = "snoozeKey";
    public static final String Hardcore = "hardcoreKey";
    public static final String Deficit = "deficitKey";
    public static final String Snooze_amount = "snoozeamountKey";
    public static final String Ringtone = "ringtoneKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.settings_main);

        age = (TextView) findViewById(R.id.editText);
        snooze = (Switch) findViewById(R.id.snooze);
        hardcore = (Switch) findViewById(R.id.hardcore);

        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (media_song != null) {
                    media_song.stop();
                    media_song.reset();
                }

                if (arg1) {
                    snooze_amount = 5;
                }
            }
        });


        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (media_song != null) {
                    media_song.stop();
                    media_song.reset();
                }
                if (arg1) {
                    snooze_amount = 10;
                }
            }
        });

        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (media_song != null) {
                    media_song.stop();
                    media_song.reset();
                }
                if (arg1) {
                    snooze_amount = 15;
                }
            }
        });

        sendBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (media_song != null) {
                    media_song.stop();
                    media_song.reset();
                }
                Save(0);
                Intent profileIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(profileIntent);
            }
        });

        deleteBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (media_song != null) {
                    media_song.stop();
                    media_song.reset();
                }
                Save(1);
                Intent profileIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(profileIntent);
            }
        });


       /* snooze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                       ...switch off..
                } else {
                       ...switch off..
                }
            }
        });*/

        sp = findViewById(R.id.spinner);
        String[] items = new String[]{"Random","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp.setAdapter(adapter);

        firstOpened = true;

        sp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (media_song != null) {
                            media_song.stop();
                            media_song.reset();
                        }
                        
                        if (position == 1) {
                            media_song = MediaPlayer.create(context, R.raw._1);
                            media_song.start();
                        }
                        else if (position == 2) {
                            media_song = MediaPlayer.create(context, R.raw._2);
                            media_song.start();
                        }
                        else if (position == 3) {
                            media_song = MediaPlayer.create(context, R.raw._3);
                            media_song.start();
                        }
                        else if (position == 4) {
                            media_song = MediaPlayer.create(context, R.raw._4);
                            media_song.start();
                        }
                        else if (position == 5) {
                            media_song = MediaPlayer.create(context, R.raw._5);
                            media_song.start();
                        }
                        else if (position == 6) {
                            media_song = MediaPlayer.create(context, R.raw._6);
                            media_song.start();
                        }
                        else if (position == 7) {
                            media_song = MediaPlayer.create(context, R.raw._7);
                            media_song.start();
                        }
                        else if (position == 8) {
                            media_song = MediaPlayer.create(context, R.raw._8);
                            media_song.start();
                        }
                        else if (position == 9) {
                            media_song = MediaPlayer.create(context, R.raw._9);
                            media_song.start();
                        }
                        else if (position == 10){
                            media_song = MediaPlayer.create(context, R.raw._10);
                            media_song.start();
                        } else {
                            if (media_song != null) {
                                media_song.stop();
                                media_song.reset();
                            }
                        }

                        if (firstOpened) {
                            if (media_song != null) {
                                media_song.stop();
                                media_song.reset();
                            }
                            firstOpened = false;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Semmi
                    }
                }
        );






        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Age)) {
            age.setText(sharedpreferences.getString(Age, ""));
        }

        if (sharedpreferences.contains(Snooze)) {
            snooze.setChecked(sharedpreferences.getBoolean(Snooze, true));
        } else {
            snooze.setChecked(true);
        }

        if (sharedpreferences.contains(Hardcore)) {
            hardcore.setChecked(sharedpreferences.getBoolean(Hardcore, false));
        }

        if (sharedpreferences.contains(Snooze_amount)) {
            switch (sharedpreferences.getInt(Snooze_amount, 5)) {
                case 5:
                    rb1.setChecked(true);
                    break;
                case 10:
                    rb2.setChecked(true);
                    break;
                case 15:
                    rb3.setChecked(true);
                    break;
            }
        }

        if (sharedpreferences.contains(Ringtone)) {
            sp.setSelection(adapter.getPosition(sharedpreferences.getString(Ringtone,"Random")));
        }
    }

    public void Save(int i) {
        String a = age.getText().toString();
        Boolean s = snooze.isChecked();
        Boolean h = hardcore.isChecked();
        int am = snooze_amount;
        String r = (String) sp.getSelectedItem();
        
        SharedPreferences.Editor editor = sharedpreferences.edit();

        if (!a.isEmpty())
            editor.putString(Age, a);
        editor.putBoolean(Snooze, s);
        editor.putBoolean(Hardcore, h);
        editor.putInt(Snooze_amount, am);
        if ( i == 1) {
            editor.putString(Deficit, "0");
        }
        if (!r.isEmpty())
            editor.putString(Ringtone, r);

        editor.apply();
    }

    public void clear(View view) {
        age = (TextView) findViewById(R.id.age);
        snooze = (Switch) findViewById(R.id.snooze);
        hardcore = (Switch) findViewById(R.id.hardcore);

        age.setText("");
        snooze.setChecked(false);
        hardcore.setChecked(false);

    }

    public void Get(View view) {
        age = (TextView) findViewById(R.id.age);
        snooze = (Switch) findViewById(R.id.snooze);
        hardcore = (Switch) findViewById(R.id.hardcore);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Age)) {
            age.setText(sharedpreferences.getString(Age, ""));
        }
        if (sharedpreferences.contains(Snooze)) {
            snooze.setChecked(sharedpreferences.getBoolean(Snooze, false));
        }

        if (sharedpreferences.contains(Hardcore)) {
            hardcore.setChecked(sharedpreferences.getBoolean(Hardcore, false));
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
}
