package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private boolean isUpdateMode = false; // Flag to determine if opened for update or new

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_detail);

        addViews(); // display_info() is called here, which sets isUpdateMode
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtPercent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_save_customer();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process_remove_customer();
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear input fields.
                // If in update mode, the customer ID field (edtCustomerId) is disabled and should not be cleared or enabled.
                // If in new customer mode, customer ID field is cleared and ensured to be enabled.
                if (!isUpdateMode) {
                    edtCustomerId.setText("");
                    edtCustomerId.setEnabled(true); // Ensure ID field is editable for a new customer
                }
                // Clear other fields regardless of mode
                edtCustomerName.setText("");
                edtEmail.setText("");
                edtPhone.setText("");
                edtUserName.setText("");
                edtPassword.setText("");
                // DO NOT change isUpdateMode here. It reflects how the activity was launched.
                Toast.makeText(CustomerDetailActivity.this, "Fields cleared for data entry.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void process_remove_customer() {
        Intent resultIntent = new Intent(); // Create a new Intent for the result
        String id = edtCustomerId.getText().toString();
        resultIntent.putExtra("REMOVED CUSTOMER ID", id);
        setResult(600, resultIntent); // resultCode 600 for remove
        finish();
    }

    private void process_save_customer() {
        Customer c = new Customer();
        try {
            String customerIdText = edtCustomerId.getText().toString().trim();

            // isUpdateMode is determined by how the activity was launched (in display_info)
            if (isUpdateMode) {
                // In update mode, ID should be present and comes from the disabled edtCustomerId
                if (customerIdText.isEmpty()) {
                    // This case should ideally not happen if edtCustomerId is disabled and pre-filled
                    Toast.makeText(this, "Customer ID is missing for update.", Toast.LENGTH_SHORT).show();
                    return;
                }
                c.setId(Integer.parseInt(customerIdText));
            } else { // New customer mode
                if (!customerIdText.isEmpty()) { // If ID is entered for a new customer
                    c.setId(Integer.parseInt(customerIdText));
                } else {
                    // If ID is empty for a new customer, it's assumed DB might auto-generate it,
                    // or it's not strictly required before insertion.
                    // If ID is mandatory for new customers even before DB, add a Toast and return.
                    // Toast.makeText(this, "Customer ID cannot be empty for a new customer", Toast.LENGTH_SHORT).show();
                    // return;
                }
            }

            c.setName(edtCustomerName.getText().toString());
            c.setEmail(edtEmail.getText().toString());
            c.setPhone(edtPhone.getText().toString());
            c.setUsername(edtUserName.getText().toString());
            c.setPassword(edtPassword.getText().toString());

            Intent resultIntent = new Intent(); // Create a new Intent for the result
            if (isUpdateMode) {
                resultIntent.putExtra("UPDATED CUSTOMER", c);
            } else {
                resultIntent.putExtra("NEW CUSTOMER", c);
            }
            setResult(500, resultIntent); // resultCode 500 for save/update
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Customer ID format.", Toast.LENGTH_SHORT).show();
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

        display_info();
    }

    private void display_info() {
        Intent intent = getIntent();
        Customer selected = null;

        if (intent.hasExtra("SELECTED_CUSTOMER")) {
            selected = (Customer) intent.getSerializableExtra("SELECTED_CUSTOMER");
            isUpdateMode = true; // Activity is in update mode
        } else {
            isUpdateMode = false; // Activity is in new customer mode
        }

        if (isUpdateMode && selected != null) { // Existing customer data for update
            edtCustomerId.setText(String.valueOf(selected.getId()));
            edtCustomerName.setText(selected.getName());
            edtEmail.setText(selected.getEmail());
            edtPhone.setText(selected.getPhone());
            edtUserName.setText(selected.getUsername());
            // edtPassword.setText(selected.getPassword()); // Usually, password is not pre-filled
            edtCustomerId.setEnabled(false); // ID should not be editable for existing customer
        } else { // New customer mode
            edtCustomerId.setText("");
            edtCustomerName.setText("");
            edtEmail.setText("");
            edtPhone.setText("");
            edtUserName.setText("");
            edtPassword.setText("");
            edtCustomerId.setEnabled(true); // ID can be entered for new customer (or left blank if auto-generated)
        }
    }
}