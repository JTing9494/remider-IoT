package com.example.user.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Declares a public class named EditDataActivity that extends AppCompatActivity.
// This activity is likely used to edit or delete existing reminder entries.
public class EditDataActivity extends AppCompatActivity {
    // Defines a constant String TAG for logging, 
    // making it easier to filter logs in Logcat for this specific activity.
    private static final String TAG = "EditDataActivity";

    // Declares Button variables for the update and delete buttons.
    Button btnUpdate,btnDelete;
    // Declares a TextView variable, likely used to display some information.
    TextView textView1;
    // Declares an EditText variable, used for editing the reminder text.
    EditText editView1;
    // Declares a DatabaseHelper variable, an instance of the custom class for database operations.
    DatabaseHelper DatabaseHelper;


    // Declares a private String variable to store the name of the selected reminder item.
    private String selectedName;
    // Declares a private integer variable to store the ID of the selected reminder item.
    private int selectedID;


    @Override
    // This method is called when the activity is first created.
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Calls the superclass's onCreate method to perform default initialization.
        super.onCreate(savedInstanceState);
        // Sets the user interface layout for this activity from the R.layout.edit_data_layout XML file.
        setContentView(R.layout.edit_data_layout);
        // Initializes the btnUpdate Button by finding the view with the ID R.id.btnUpdate in the layout.
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        // Initializes the btnDelete Button by finding the view with the ID R.id.btnDelete in the layout.
        btnDelete = (Button) findViewById(R.id.btnDelete);
        // Initializes the textView1 TextView by finding the view with the ID R.id.textView1 in the layout.
        textView1 = (TextView)findViewById(R.id.textView1);
        // Initializes the editView1 EditText by finding the view with the ID R.id.editView1 in the layout.
        editView1 = (EditText) findViewById(R.id.editView1);
        // Initializes the DatabaseHelper, passing the current activity context.
        DatabaseHelper = new DatabaseHelper(this);


        // Get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        // Now get the itemID we passed as an extra
        // NOTE: -1 is just the default value
        selectedID = receivedIntent.getIntExtra("id", -1); 

        // Now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        // Set the text to show the current selected name
        editView1.setText(selectedName);

        // Sets an OnClickListener for the btnUpdate button.
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnUpdate button is clicked.
            public void onClick(View view) {
                // Gets the current text from the editView1 EditText.
                String item = editView1.getText().toString();
                // Checks if the EditText is not empty.
                if(!item.equals("")){
                    // Calls the updateName method of the DatabaseHelper to update the item in the database.
                    DatabaseHelper.updateName(item,selectedID,selectedName);
                    // Creates an Intent to navigate back to ListDataActivity.
                    Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                    // Starts the ListDataActivity.
                    startActivity(intent);
                    // Shows a toast message indicating the item was updated.
                    toastMessage("Updated");
                }else{
                    // Shows a toast message prompting the user to enter a name if the EditText is empty.
                    toastMessage("You must enter a name");
                }
            }
        });

        // Sets an OnClickListener for the btnDelete button.
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnDelete button is clicked.
            public void onClick(View view) {
                // Calls the deleteName method of the DatabaseHelper to remove the item from the database.
                DatabaseHelper.deleteName(selectedID,selectedName);
                // Clears the text in the editView1 EditText.
                editView1.setText("");
                // Creates an Intent to navigate back to ListDataActivity.
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                // Starts the ListDataActivity.
                startActivity(intent);
                // Shows a toast message indicating the item was removed.
                toastMessage("Removed From Database");
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
