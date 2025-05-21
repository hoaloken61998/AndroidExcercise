package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Category;
import com.example.models.ListCategory;
import com.example.models.Product;

import java.util.ArrayList;

public class ProductManagementActivity extends AppCompatActivity {

    ListView lvProduct;
    ArrayAdapter<Product> adapter;
    ArrayAdapter<Product> productAdapter;
    ArrayAdapter<Category> categoryAdapter;
    Spinner spinnerCategory;
    ArrayAdapter adapterCategory;


    ListCategory listCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_management);

        // Initialize data source first if adapters depend on it
        listCategory = new ListCategory();
        listCategory.generate_example_products_dataset(); // Assuming this populates categories

        addViews();    // Initialize views and set up adapters
        addEvents();   // Set up event listeners

        // Apply window insets listener (ensure R.id.main is the root layout of your activity_product_management.xml)
        // Or apply it to lvProduct AFTER it's initialized in addViews if that's the specific target
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { // Assuming R.id.main is your root container
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category c=categoryAdapter.getItem(position);
                displayProductByCategory(c);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = productAdapter.getItem(position);
                displayProductDetailActivity(selected);
            }
        });
    }

    private void displayProductDetailActivity(Product selected) {
        Intent intent = new Intent(ProductManagementActivity.this, ProductsDetailActivity.class);
        intent.putExtra("SELECTED_PRODUCT", selected);
        startActivity(intent);
    }

    private void displayProductByCategory(Category c) {
        // Xóa dữ liệu cũ trong listView
        productAdapter.clear();
        //nạp mới lại dữ liệu cho adapter;
        productAdapter.addAll(c.getProducts());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addViews() {
        // Initialize ListView
        lvProduct = findViewById(R.id.lvProduct); // <<<< IMPORTANT: ADD THIS ID TO YOUR XML
        productAdapter = new ArrayAdapter<>(
                ProductManagementActivity.this,
                android.R.layout.simple_list_item_1 // Or a custom layout for products
        );
        lvProduct.setAdapter(productAdapter);

        // Initialize Spinner
        spinnerCategory = findViewById(R.id.spinnerCategory); // <<<< IMPORTANT: ADD THIS ID TO YOUR XML
        if (spinnerCategory == null) {
            // Handle error: Spinner not found
            return;
        }

        // Setup Category Spinner Adapter
        categoryAdapter = new ArrayAdapter<>(
                ProductManagementActivity.this,
                android.R.layout.simple_spinner_item,
                listCategory.getCategory() // Directly pass the list of categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Setup Product ListView Adapter
        // Example: Assuming you have a method in ListCategory to get products
        // Or create a new ArrayList<Product> here and populate it
        ArrayList<Product> products = new ArrayList<>();
        // TODO: Populate 'products' list, perhaps based on selected category or all products
        // For now, an empty list or example data:
        // products.add(new Product("Sample Product 1", "Details..."));
        // products.add(new Product("Sample Product 2", "Details..."));

        productAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, // Or a custom layout for products
                products
        );
        lvProduct.setAdapter(productAdapter);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_new_product) {
            Intent intent = new Intent(ProductManagementActivity.this, ProductsDetailActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menu_manage_categories) {
            Toast.makeText(this, "Manage Categories", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.menu_help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}