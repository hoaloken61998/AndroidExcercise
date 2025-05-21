package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Customer;

public class CustomerDetailActivity extends AppCompatActivity {

    EditText edtCustomerId;
    EditText edtCustomerName;
    EditText edtEmail;
    EditText edtPhone;
    EditText edtUserName;
    EditText edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_detail);

        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void addViews() {
        edtCustomerId = findViewById(R.id.edt_customer_id);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        display_infor();

    }

    private void display_infor() {
        Intent intent = getIntent();
        Customer selected = (Customer) intent.getSerializableExtra("SELECTED_CUSTOMER");

        // Add null check before accessing Customer object
        if (selected != null) {
            edtCustomerId.setText(String.valueOf(selected.getId()));
            edtCustomerName.setText(selected.getName());
            edtEmail.setText(selected.getEmail());
            edtPhone.setText(selected.getPhone());
            edtUserName.setText(selected.getUsername());
        } else {
            // Handle the case when no customer data is received
            finish(); // Or show an error message
            // Toast.makeText(this, "No customer data received", Toast.LENGTH_SHORT).show();
        }
    }
}