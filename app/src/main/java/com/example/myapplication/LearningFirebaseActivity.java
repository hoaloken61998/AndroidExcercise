package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.ContactAdapter;

public class LearningFirebaseActivity extends AppCompatActivity {

    ListView lvContact;
    Button btnInsertContact;
    ContactAdapter contactAdapter;
    ArrayList<String> contactList;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning_firebase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        addEvents();
    }

    private void addViews() {
        lvContact = findViewById(R.id.lvContact);
        btnInsertContact = findViewById(R.id.btnInsertContact);
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(LearningFirebaseActivity.this, contactList);
        lvContact.setAdapter(contactAdapter);
        loadData();
    }

    private void addEvents() {
        btnInsertContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningFirebaseActivity.this, InsertContactActivity.class);
                startActivity(intent);
            }
        });
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object dataObj = contactAdapter.getItem(position);
                String data = dataObj != null ? dataObj.toString() : "";
                String[] lines = data.split("\n");
                if (lines.length > 0 && !lines[0].isEmpty()) {
                    String key = lines[0];
                    Intent intent = new Intent(LearningFirebaseActivity.this, DetailContactActivity.class);
                    intent.putExtra("KEY", key);
                    startActivity(intent);
                } else {
                    Toast.makeText(LearningFirebaseActivity.this, "No contact ID found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactAdapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Object valueObj = data.getValue();
                    String display = "";
                    if (valueObj instanceof HashMap) {
                        HashMap hashMap = (HashMap) valueObj;
                        String id = hashMap.get("id") != null ? hashMap.get("id").toString() : "";
                        String name = hashMap.get("name") != null ? hashMap.get("name").toString() : "";
                        String phone = hashMap.get("phone") != null ? hashMap.get("phone").toString() : "";
                        String email = hashMap.get("email") != null ? hashMap.get("email").toString() : "";
                        display = id + "\n" + name + "\n" + phone + "\n" + email;
                    } else if (valueObj != null) {
                        display = valueObj.toString();
                    }
                    contactAdapter.add(display);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadData:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
