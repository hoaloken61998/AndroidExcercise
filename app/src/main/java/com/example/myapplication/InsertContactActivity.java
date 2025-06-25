package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertContactActivity extends AppCompatActivity {

    EditText edtId, edtName, edtPhone, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_contact);
        addViews();
    }

    private void addViews() {
        edtId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
    }

    public void processInsertContact(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("contacts");
            String contactId = edtId.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            if (contactId.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Store all contact info as a single object
            Contact contact = new Contact(contactId, name, phone, email);
            myRef.child(contactId).setValue(contact);
            finish();
        } catch (Exception ex) {
            Toast.makeText(this, "Error:" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
