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


// Declares a public class named MainActivity that extends AppCompatActivity.
// This is the main entry point of the application.
public class MainActivity extends AppCompatActivity{

    // Defines a constant String TAG for logging, 
    // making it easier to filter logs in Logcat for this specific activity.
    private static final String TAG = "MainActivity";
    // Initializes a Calendar instance to the current date and time.
    // It will be used to set the alarm time.
    final Calendar calendar = Calendar.getInstance();

    // Declares a String variable to hold data, likely for sending over a socket.
    String data;
    // Declares an EditText variable for user input.
    EditText editText1;
    // Declares an AlarmManager variable, used to schedule alarms.
    AlarmManager alarmManager;
    // Declares a TimePicker variable, allowing the user to select a time for the alarm.
    TimePicker alarmTimePicker;
    // Declares a PendingIntent variable, which will be triggered when the alarm goes off.
    PendingIntent pending_intent;
    // Declares a DatabaseHelper variable, an instance of the custom class for database operations.
    DatabaseHelper DatabaseHelper;
    // Declares TextView variables to display text to the user.
    TextView textView1, alarmTextView;
    // Declares Button variables for user interactions.
    Button btnAdd, btnViewData, start_alarm, stop_alarm;

    // Declares an instance of MainActivity, likely for use in inner classes or callbacks.
    MainActivity inst;
    // Declares a Context variable, holding the context of this activity.
    Context context;

    @Override
    // This method is called when the activity is first created.
    protected void onCreate(Bundle savedInstanceState) {
        // Calls the superclass's onCreate method to perform default initialization.
        super.onCreate(savedInstanceState);
        // Sets the user interface layout for this activity from the R.layout.activity_main XML file.
        setContentView(R.layout.activity_main);
        // Initializes the editText1 by finding the view with ID R.id.editText1 in the layout.
        editText1 = findViewById(R.id.editText1);
        // Initializes the textView1 by finding the view with ID R.id.textView1 in the layout.
        textView1 = findViewById(R.id.textView1);
        // Initializes the btnAdd by finding the view with ID R.id.btnAdd in the layout.
        btnAdd = findViewById(R.id.btnAdd);
        // Initializes the btnViewData by finding the view with ID R.id.btnView in the layout.
        btnViewData = findViewById(R.id.btnView);
        // Initializes the start_alarm button by finding the view with ID R.id.start_alarm in the layout.
        start_alarm = findViewById(R.id.start_alarm);
        // Initializes the stop_alarm button by finding the view with ID R.id.stop_alarm in the layout.
        stop_alarm = findViewById(R.id.stop_alarm);
        // Initializes the DatabaseHelper, passing the current activity context.
        DatabaseHelper = new DatabaseHelper(this);

        // Assigns the current activity context to the 'context' variable.
        this.context = this;

        // Initializes the alarmTextView by finding the view with ID R.id.alarmText in the layout.
        alarmTextView = findViewById(R.id.alarmText);

        // Creates a new Intent that will be broadcast to the AlarmReceiver class when the alarm triggers.
        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        // Gets a handle to the system's AlarmManager service.
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Initializes the alarmTimePicker by finding the view with ID R.id.alarmTimePicker in the layout.
        alarmTimePicker = findViewById(R.id.alarmTimePicker);

        // Sets an OnClickListener for the btnAdd button.
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnAdd button is clicked.
            public void onClick(View v) {
                // Gets the text entered by the user in editText1.
                String newEntry = editText1.getText().toString();
                // Checks if the editText1 is not empty.
                if (editText1.length() != 0) {
                    // Calls the AddData method to save the new entry to the database.
                    AddData(newEntry);
                    // Clears the editText1 field.
                    editText1.setText("");
                } else {
                    // Shows a toast message prompting the user to enter text if the field is empty.
                    toastMessage("You must put something in the text field!");
                }
            }
        });

        // Sets an OnClickListener for the btnViewData button.
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnViewData button is clicked.
            public void onClick(View v) {
                // Creates an Intent to navigate to the ListDataActivity.
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                // Starts the ListDataActivity.
                startActivity(intent);
            }
        });

        // Sets an OnClickListener for the start_alarm button.
        start_alarm.setOnClickListener(new View.OnClickListener() {
            // Suppresses lint warnings for using APIs that require a specific Android version (M = Marshmallow, API 23).
            // This is relevant for methods like TimePicker.getHour() and TimePicker.getMinute().
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            // This method is called when the start_alarm button is clicked.
            public void onClick(View v) {

                // Gets the hour selected by the user from the alarmTimePicker.
                final int hour = alarmTimePicker.getHour();
                // Gets the minute selected by the user from the alarmTimePicker.
                final int minute = alarmTimePicker.getMinute();; // Extra semicolon here, can be removed.

                // Logs the selected hour and minute for debugging.
                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                // Calls setAlarmText to update the UI with the selected time (though this message is a bit redundant with the later one).
                setAlarmText("You clicked a " + hour + " and " + minute);

                // Sets the hour of the day for the calendar instance based on the TimePicker's selection.
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                // Sets the minute for the calendar instance based on the TimePicker's selection.
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                // Puts an extra string "yes" into the intent. This will be received by AlarmReceiver to indicate the alarm should start.
                intent.putExtra("extra", "yes");
                /*
                Creates or retrieves a PendingIntent that will perform a broadcast.
                MainActivity.this: The context.
                0: Request code for the PendingIntent
                intent: The Intent to be broadcast.
                PendingIntent.FLAG_UPDATE_CURRENT: If the described PendingIntent already exists, 
                then keep it but replace its extra data with what is in this new Intent.
                */
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                /*
                Sets a one-time alarm.
                AlarmManager.RTC: Alarm type that fires based on the real-time clock (wall clock time). It will not wake up the device if it's asleep.
                calendar.getTimeInMillis(): The time at which the alarm should go off, in milliseconds since the epoch.
                pending_intent: The PendingIntent to be executed when the alarm fires.
                */
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pending_intent);
                // Shows a toast message confirming that the alarm has been set.
                Toast.makeText(MainActivity.this, "Alarm is set", Toast.LENGTH_SHORT).show();
                // Updates the alarmTextView to show the time the alarm is set for.
                setAlarmText("Alarm set to " + hour + ":" + minute);
            }
        });


        // Sets an OnClickListener for the stop_alarm button.
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the stop_alarm button is clicked.
            public void onClick(View v) {

                    // Puts an extra string "no" into the intent. This will be received by AlarmReceiver to indicate the alarm (ringtone) should stop.
                    intent.putExtra("extra", "no");

                    // Broadcasts the intent immediately. This will be received by AlarmReceiver.
                    sendBroadcast(intent);

                    /*
                    Creates or retrieves a PendingIntent. Note: Using request code 1 here, different from the one used to set the alarm (0).
                    This is important for cancelling the correct alarm if multiple alarms were set with different request codes.
                    The flag 0 (no flags) means if the described PendingIntent already exists, then the old one is returned.
                    For cancelling, it's crucial that this PendingIntent matches the one used to set the alarm.
                    If the intent content (action, data, categories, components, flags) and request code match, it's considered the same.
                    Here, the intent's "extra" is different, but the core intent (action, component) might be the same.
                    It's generally safer to use FLAG_UPDATE_CURRENT or ensure all parameters match exactly for cancellation.
                    However, alarmManager.cancel() primarily matches on the core Intent fields and request code.
                    */
                    pending_intent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);

                    // Cancels any alarms previously set with a PendingIntent that matches the given one.
                    alarmManager.cancel(pending_intent);

                    // Updates the alarmTextView to indicate the alarm has been canceled.
                    setAlarmText("Alarm canceled");

                    // This loop will run indefinitely, continuously trying to send data.
                    while (true) {
                        // Creates a new instance of the 'send' AsyncTask.
                        send sendcode = new send();

                        // Gets the current text from editText1. This will be the same text on each iteration unless changed by another thread.
                        data = editText1.getText().toString();
                        
                        // Executes the AsyncTask. This will attempt to send data over a socket.
                        sendcode.execute();
                    }
            }
        });
    }

    // Public method to add a new entry (reminder string) to the database.
    public void AddData(String newEntry) {
        // Calls the addData method of DatabaseHelper to insert the new entry.
        // The result indicates whether the insertion was successful.
        boolean insertData = DatabaseHelper.addData(newEntry);

        // Checks if the data was inserted successfully.
        if (insertData) {
            // Shows a success toast message.
            toastMessage("Data Successfully Inserted!");
        } else {
            // Shows an error toast message.
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message The message to display in the toast.
     */

    // Private helper method to display a short Toast message.
    private void toastMessage(String message){
        // Creates and shows a Toast with the given message and short duration.
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    // Public method to set the text of the alarmTextView.
    public void setAlarmText(String alarmText) {
        // Sets the text of alarmTextView to the provided alarmText string.
        alarmTextView.setText(alarmText);
    }


    @Override
    // This method is called when the activity is becoming visible to the user.
    public void onStart() {
        // Calls the superclass's onStart method.
        super.onStart();
        // Assigns the current instance of MainActivity to the 'inst' variable.
        inst = this;
    }

    @Override
    // This method is called before the activity is destroyed.
    public void onDestroy() {
        // Calls the superclass's onDestroy method.
        super.onDestroy();

        // Logs a message indicating that the onDestroy method has been called.
        Log.e("MyActivity", "on Destroy");
    }
}

