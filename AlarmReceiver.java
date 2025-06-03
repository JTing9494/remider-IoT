package com.example.user.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    // This method is called when the BroadcastReceiver receives an Intent broadcast.
    public void onReceive(Context context, Intent intent) {
        // Gets a handle to the system's NotificationManager service,
        // which is used to notify the user of events.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Creates an Intent that will be launched when the user taps the notification.
        // This intent will open the MainActivity.
        Intent notificationIntent = new Intent(context, MainActivity.class);
            // Creates a TaskStackBuilder, which helps in creating a proper back stack for the activity launched from the notification.
            // This ensures that navigating back from MainActivity will take the user to the app's previous state or home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            
            // Adds the MainActivity to the parent stack. 
            // This means if MainActivity has a parent activity defined in the manifest, it would be added here.
            stackBuilder.addParentStack(MainActivity.class);
            
            // Adds the notificationIntent (which points to MainActivity) as the next intent in the task stack.
            stackBuilder.addNextIntent(notificationIntent);
            
            // The PendingIntent will be used by the NotificationManager to launch the activity.
            // FLAG_UPDATE_CURRENT ensures that if a PendingIntent with the same request code already exists,
            // it will be updated with the new intent's extras.
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creates a Notification.Builder object, which is used to construct the notification.
        // The 'context' is passed to the builder.
        Notification.Builder builder = new Notification.Builder(context);

        // Builds the notification with various properties
        Notification notification = builder.setContentTitle("Remember to do something !!")
                .setContentText("Notification")
                .setTicker("Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        // Displays the notification.
        // The first parameter (0) is a unique ID for this notification. 
        // If you post another notification with the same ID, it will update the existing one.
        notificationManager.notify(0, notification);

        // Retrieves the string extra named "extra" from the received Intent.
        // This extra is expected to indicate whether the alarm should start or stop a service
        String state = intent.getExtras().getString("extra");

        // Logs a message to Logcat indicating that the receiver is active and shows the value of 'state'.
        Log.e("MyActivity", "In the receiver with " + state);

        // Creates an Intent to start the RingtonePlayingService.
        // This service is likely responsible for playing or stopping the alarm sound.
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        // Puts the 'state' (e.g., "yes" or "no") as an extra into the serviceIntent.
        // The RingtonePlayingService will use this extra to determine its action.
        serviceIntent.putExtra("extra", state);

        // Starts the RingtonePlayingService with the serviceIntent.
        // The 'context' is used to start the service.
        context.startService(serviceIntent);
    }
}

