package com.example.user.reminder_firebstore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


// Declares a public class named MainActivity that extends AppCompatActivity.
// This activity interacts with Firebase Firestore to save and load reminder data.
public class MainActivity extends AppCompatActivity {
    // Defines a constant String TAG for logging, useful for filtering logs in Logcat.
    private static final String TAG = "FireLog";
    // Declares a FirebaseFirestore instance for interacting with the Firestore database.
    private FirebaseFirestore firebaseFirestore;
    // Declares Button variables for saving and loading data.
    Button btnSave, btnLoad;
    // Declares an EditText variable for user input.
    EditText editText1;
    // Declares a TextView variable for displaying loaded data.
    TextView textView1;

    @Override
    // This method is called when the activity is first created.
    protected void onCreate(Bundle savedInstanceState) {
        // Calls the superclass's onCreate method to perform default initialization.
        super.onCreate(savedInstanceState);
        // Sets the user interface layout for this activity from R.layout.activity_main XML file.
        setContentView(R.layout.activity_main);

        // Initializes the textView1 by finding the view with ID R.id.textView1 in the layout.
        textView1 = findViewById(R.id.textView1);
        // Initializes the editText1 by finding the view with ID R.id.editText1 in the layout.
        editText1 = findViewById(R.id.editText1);
        // Initializes the btnSave by finding the view with ID R.id.btnSave in the layout.
        btnSave = findViewById(R.id.btnSave);
        // Initializes the btnLoad by finding the view with ID R.id.btnLoad in the layout.
        btnLoad = findViewById(R.id.btnLoad);
        // Gets an instance of FirebaseFirestore.
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Sets an OnClickListener for the btnSave button.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnSave button is clicked.
            public void onClick(View v) {

                // Gets the text entered by the user in editText1.
                String MainText = editText1.getText().toString();

                // Creates a new HashMap to store the data to be added to Firestore.
                Map<String, String> dataToAdd = new HashMap<>();
                // Puts the user's text into the map with the key "item".
                dataToAdd.put("item", MainText);
                // Accesses the "user" collection in Firestore, then the "Reminder" document, and sets its data.
                // This will create the document if it doesn't exist, or overwrite it if it does.
                firebaseFirestore.collection("user").document("Reminder").set(dataToAdd);
                // Shows a toast message indicating that data was added to Firebase.
                Toast.makeText(MainActivity.this, "Add data to Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // Sets an OnClickListener for the btnLoad button.
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            // This method is called when the btnLoad button is clicked.
            public void onClick(View v) {

                // Accesses the "user" collection, then the "Reminder" document, and attempts to get its data.
                firebaseFirestore.collection("user").document("Reminder").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    // This method is called when the get() operation is complete.
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Checks if the task was successful.
                        if(task.isSuccessful()){
                            // Gets the DocumentSnapshot result from the task.
                            DocumentSnapshot documentSnapshot = task.getResult();
                                // Checks if the document exists and is not null.
                                if(documentSnapshot.exists() && documentSnapshot != null){
                                    // Retrieves the string value associated with the key "item" from the document.
                                    String reminder = documentSnapshot.getString("item");
                                    // Sets the text of textView1 to display the loaded reminder.
                                    textView1.setText(reminder);
                                    // Shows a toast message indicating that data was loaded from Firebase.
                                    Toast.makeText(MainActivity.this, "Load data from Firebase", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
            }
        });
    }
}
