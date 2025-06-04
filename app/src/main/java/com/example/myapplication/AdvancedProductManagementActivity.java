package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.listProduct;

import adapter.ProductAdapter;

public class AdvancedProductManagementActivity extends AppCompatActivity {

    ListView lvAdvancedProduct;
    ProductAdapter adapter;
    listProduct ListProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advanced_product_management);

        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvAdvancedProduct = findViewById(R.id.lvAdvancedProduct);
        adapter = new ProductAdapter(AdvancedProductManagementActivity.this,
                R.layout.item_advanced_product);
        lvAdvancedProduct.setAdapter(adapter);

        listProduct listProduct = new listProduct();
        listProduct.generate_sample_dataset();
        adapter.addAll(listProduct.getProducts());
    }
}