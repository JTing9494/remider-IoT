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


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FireLog";
    private FirebaseFirestore firebaseFirestore;
    Button btnSave, btnLoad;
    EditText editText1;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        editText1 = findViewById(R.id.editText1);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MainText = editText1.getText().toString();

                Map<String, String> dataToAdd = new HashMap<>();
                dataToAdd.put("item", MainText);
                firebaseFirestore.collection("user").document("Reminder").set(dataToAdd);
                Toast.makeText(MainActivity.this, "Add data to Firebase", Toast.LENGTH_SHORT).show();

                //firebaseFirestore.collection("user").document("Reminder").set(dataToAdd).addOnFailureListener(new OnFailureListener() {
                    //@Override
                    //public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    //}
                //});
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("user").document("Reminder").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists() && documentSnapshot != null){
                                    String reminder = documentSnapshot.getString("item");
                                    textView1.setText(reminder);
                                    Toast.makeText(MainActivity.this, "Load data from Firebase", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
            }
        });
    }
}
