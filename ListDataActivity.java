package com.example.user.reminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


// Declares a public class named ListDataActivity that extends AppCompatActivity.
// This activity is responsible for displaying a list of reminder items from the database.
public class ListDataActivity extends AppCompatActivity {

    // Defines a constant String TAG for logging, making it easier to filter logs in Logcat for this specific activity.
    private static final String TAG = "ListDataActivity";

    // Declares a DatabaseHelper variable, an instance of the custom class for database operations.
    DatabaseHelper DatabaseHelper;

    // Declares a ListView variable, which will be used to display the list of data.
    private ListView ListView;

    @Override
    // This method is called when the activity is first created.
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Calls the superclass's onCreate method to perform default initialization.
        super.onCreate(savedInstanceState);
        // Sets the user interface layout for this activity from the R.layout.list_layout XML file.
        setContentView(R.layout.list_layout);
        // Initializes the ListView by finding the view with the ID R.id.listView in the layout.
        ListView = findViewById(R.id.listView);
        // Initializes the DatabaseHelper, passing the current activity context.
        DatabaseHelper = new DatabaseHelper(this);

        // Calls the method to populate the ListView with data from the database.
        populateListView();
    }

    // Private helper method to fetch data from the database and display it in the ListView.
    private void populateListView() {
        // Logs a message to Logcat indicating that the method has started.
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        // Calls the getData() method of DatabaseHelper to retrieve all reminder items from the database.
        Cursor data = DatabaseHelper.getData();
        // Creates a new ArrayList to store the reminder strings.
        ArrayList<String> listData = new ArrayList<>();
        // Iterates through the Cursor (the result set from the database).
        while(data.moveToNext()){
            //get the value from the database in column 1 (which is the 'name' column, index 1)
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        // Create the list adapter and set the adapter
        // Creates a new ArrayAdapter to bind the listData to the ListView.
        // android.R.layout.simple_list_item_1 is a built-in Android layout for a single text item.
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        // Sets the adapter for the ListView, which populates it with the data.
        ListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        // Sets an OnItemClickListener for the ListView to handle item clicks.
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // This method is called when an item in the ListView is clicked.
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Gets the string value of the clicked item from the adapterView at the given position 'i'.
                String name = adapterView.getItemAtPosition(i).toString();
                // Logs the name of the clicked item.
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                // Calls the getItemID() method of DatabaseHelper to get the ID associated with the clicked item's name.
                Cursor data = DatabaseHelper.getItemID(name); //get the id associated with that name
                // Initializes itemID to -1 (a default value indicating ID not found).
                int itemID = -1;
                // Iterates through the Cursor (which should contain one row if the name is unique).
                while(data.moveToNext()){
                    // Gets the integer value from the first column (index 0), which is the ID.
                    itemID = data.getInt(0);
                }
                // Checks if a valid ID was found (greater than -1).
                if(itemID > -1){
                    // Logs the found ID.
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    // Creates an Intent to start the EditDataActivity.
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    // Puts the itemID as an extra in the Intent, to be passed to EditDataActivity.
                    editScreenIntent.putExtra("id",itemID);
                    // Puts the item name as an extra in the Intent.
                    editScreenIntent.putExtra("name",name);
                    // Starts the EditDataActivity.
                    startActivity(editScreenIntent);
                }
                // If no ID was found for the name.
                else{
                    // Shows a toast message indicating that no ID is associated with that name.
                    toastMessage("No ID associated with that name");
                }
            }
        });
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
}
