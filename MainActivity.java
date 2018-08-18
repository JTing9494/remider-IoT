package com.example.user.reminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    // set the alarm to the time that you picked
    final Calendar calendar = Calendar.getInstance();

    String data;
    EditText editText1;
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    PendingIntent pending_intent;
    DatabaseHelper DatabaseHelper;
    TextView textView1, alarmTextView;
    Button btnAdd, btnViewData, start_alarm, stop_alarm;

    MainActivity inst;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.editText1);
        textView1 = findViewById(R.id.textView1);
        btnAdd = findViewById(R.id.btnAdd);
        btnViewData = findViewById(R.id.btnView);
        start_alarm = findViewById(R.id.start_alarm);
        stop_alarm = findViewById(R.id.stop_alarm);
        DatabaseHelper = new DatabaseHelper(this);

        this.context = this;

        alarmTextView = findViewById(R.id.alarmText);

        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmTimePicker = findViewById(R.id.alarmTimePicker);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText1.getText().toString();
                if (editText1.length() != 0) {
                    AddData(newEntry);
                    editText1.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                final int hour = alarmTimePicker.getHour();
                final int minute = alarmTimePicker.getMinute();;

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                setAlarmText("You clicked a " + hour + " and " + minute);


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                intent.putExtra("extra", "yes");
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pending_intent);
                Toast.makeText(MainActivity.this, "Alarm is set", Toast.LENGTH_SHORT).show();
                setAlarmText("Alarm set to " + hour + ":" + minute);
            }
        });

        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    intent.putExtra("extra", "no");

                    sendBroadcast(intent);

                    pending_intent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);

                    alarmManager.cancel(pending_intent);

                    setAlarmText("Alarm canceled");

                    while (true) {
                        send sendcode = new send();

                        data = editText1.getText().toString();

                        sendcode.execute();
                    }
            }
        });
    }

    public void AddData(String newEntry) {
        boolean insertData = DatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void setAlarmText(String alarmText) {

        alarmTextView.setText(alarmText);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }
}

