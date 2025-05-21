package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Product;

public class ProductsDetailActivity extends AppCompatActivity {

    EditText edtProductID;
    EditText edtProductName;
    EditText edtPrice;
    EditText edtCategoryID;
    EditText edtDescription;
    EditText edtImageID;
    EditText edtQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_detail);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtProductID = findViewById(R.id.edtProductID);
        edtProductName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtPrice);
        edtCategoryID = findViewById(R.id.edtCategoryID);
        edtDescription = findViewById(R.id.edtDescription);
        edtImageID = findViewById(R.id.edtImageID);
        edtQuantity = findViewById(R.id.edtQuantity);

        display_infor();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void display_infor() {
        Intent intent = getIntent();
        Product selected = (Product) intent.getSerializableExtra("SELECTED_PRODUCT");
        if (selected != null) {
            edtProductID.setText(String.valueOf(selected.getId()+""));
            edtProductName.setText(selected.getName());
            edtPrice.setText(String.valueOf(selected.getPrice()));
            edtCategoryID.setText(String.valueOf(selected.getCate_id()+""));
            edtDescription.setText(selected.getDescription());
            edtImageID.setText(selected.getImage_id()+"");
            edtQuantity.setText(String.valueOf(selected.getQuantity()));
        } else {
            // Handle the case when no customer data is received
            //finish(); // Or show an error message
            Toast.makeText(this, "No customer data received", Toast.LENGTH_SHORT).show();
        }
    }
}