package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.connectors.CategoryConnector;
import com.example.models.Category;

public class ProductCategoryManagement extends AppCompatActivity {

    ListView lvCategory;
    ArrayAdapter<Category> adapter;
    CategoryConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_category_management);

        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvCategory), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                Category selected = adapter.getItem(i);
                adapter.remove(selected);
                return false;
            }
        });
    }

    private void addViews() {
        lvCategory = findViewById(R.id.lvCategory);
        connector = new CategoryConnector();
        adapter = new ArrayAdapter<>(
                ProductCategoryManagement.this,
                android.R.layout.simple_list_item_1);
        adapter.addAll(connector.getAllCategories());
        lvCategory.setAdapter(adapter);
    }
}