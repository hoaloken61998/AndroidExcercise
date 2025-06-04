package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // Added for potential error messages

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
    Button btnNew;
    Button btnSave;
    Button btnRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_detail);

        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming btnNew is for creating a new customer entry,
                // you might want to clear fields or set defaults here.
                // For now, let's make it behave like saving a new customer.
                process_save_customer();
            } // Added missing closing brace
        });


        // Add OnClickListener for btnRemove if needed
        // btnRemove.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         // Handle remove customer logic
        //     }
        // });
    }

    private void process_save_customer() {
        //Lấy dữ liệu và mô hình hóa hướng đối tượng
        Customer c = new Customer();
        try {
            // It's good practice to handle potential NumberFormatException
            String customerIdText = edtCustomerId.getText().toString();
            if (!customerIdText.isEmpty()) {
                c.setId(Integer.parseInt(customerIdText));
            } else {
                // Handle case where ID might be empty (e.g., for a new customer)
                // Or show an error if ID is mandatory
                Toast.makeText(this, "Customer ID cannot be empty", Toast.LENGTH_SHORT).show();
                return; // Exit if ID is required and empty
            }

            c.setName(edtCustomerName.getText().toString());
            c.setEmail(edtEmail.getText().toString());
            c.setPhone(edtPhone.getText().toString());
            c.setUsername(edtUserName.getText().toString());
            c.setPassword(edtPassword.getText().toString()); // Make sure you intend to send password back

            //Đóng gói dữ liệu và intent:
            Intent resultIntent = new Intent(); // Create a new Intent for the result
            resultIntent.putExtra("NEW_CUSTOMER", c); // Make sure "NEW_CUSTOMER" is the key the calling activity expects
            setResult(500, resultIntent); // Use the new resultIntent
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Customer ID format", Toast.LENGTH_SHORT).show();
            // Log the error or handle it more gracefully
            e.printStackTrace();
        }
    }


    private void addViews() {
        edtCustomerId = findViewById(R.id.edt_customer_id);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);

        btnNew = findViewById(R.id.btnNew);
        btnSave = findViewById(R.id.btnSave);
        btnRemove = findViewById(R.id.btnRemove);

        display_info(); // Corrected method name to match declaration
    }

    private void display_info() { // Renamed to follow Java naming conventions (camelCase)
        Intent intent = getIntent();
        // Ensure that Customer class implements Serializable
        Customer selected = null;
        if (intent.hasExtra("SELECTED_CUSTOMER")) {
            selected = (Customer) intent.getSerializableExtra("SELECTED_CUSTOMER");
        }


        // Add null check before accessing Customer object
        if (selected != null) {
            edtCustomerId.setText(String.valueOf(selected.getId()));
            edtCustomerName.setText(selected.getName());
            edtEmail.setText(selected.getEmail());
            edtPhone.setText(selected.getPhone());
            edtUserName.setText(selected.getUsername());
            // edtPassword.setText(selected.getPassword()); // Decide if you want to display the existing password
        } else {
            // This case might be for creating a NEW customer.
            // You might want to clear the fields or set default values.
            // For example, if it's a "new customer" flow, you might leave fields blank.
            // If it's an error (expected data but not received), then finishing or showing a toast is appropriate.

            // Toast.makeText(this, "No customer data received for display. Assuming new customer.", Toast.LENGTH_SHORT).show();
            // finish(); // Only finish if not receiving data is truly an unrecoverable error for this screen's purpose.
        }
    }
}