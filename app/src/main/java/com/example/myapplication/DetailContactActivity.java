package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;

public class DetailContactActivity extends AppCompatActivity {

    EditText edtId, edtName, edtPhone, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_contact);
        // Only set window insets if the view with id 'main' exists
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        addViews();
        getContactDetail();
    }

    private void addViews() {
        edtId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
    }

    private void getContactDetail() {
        Intent intent = getIntent();
        final String key = intent.getStringExtra("KEY");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        Object value = dataSnapshot.getValue();
                        if (value instanceof HashMap) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) value;
                            edtId.setText(key);
                            edtName.setText(hashMap.get("name") != null ? hashMap.get("name").toString() : "");
                            edtEmail.setText(hashMap.get("email") != null ? hashMap.get("email").toString() : "");
                            edtPhone.setText(hashMap.get("phone") != null ? hashMap.get("phone").toString() : "");
                        }
                    }
                } catch (Exception ex) {
                    Log.e("MY_ERROR", ex.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MY_ERROR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void processUpdateContact(View view) {
        String key = edtId.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        if (key.isEmpty() || phone.isEmpty() || name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        // Update as a Contact object for consistency
        Contact contact = new Contact(key, name, phone, email);
        myRef.child(key).setValue(contact);
        finish();
    }

    public void processDeleteContact(View view) {
        String key = edtId.getText().toString().trim();
        if (key.isEmpty()) {
            Toast.makeText(this, "Contact ID is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).removeValue();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailContactActivity.this, LearningFirebaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}