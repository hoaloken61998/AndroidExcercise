package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.connectors.CustomerConnector;
import com.example.connectors.SQLiteConnector;
import com.example.models.Customer;
import com.example.models.ListCustomer;

import java.util.ArrayList;

public class CustomerManagementActivity extends AppCompatActivity {
    private ListView lvCustomer;
    private ArrayAdapter<Customer> adapter;
    private CustomerConnector connector;
    private SQLiteConnector sqliteConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_management);

        addViews();
        addEvents();
        loadCustomers();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvCustomer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvCustomer = findViewById(R.id.lvCustomer);
        // Initialize adapter with context and layout
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvCustomer.setAdapter(adapter);

        // Initialize connectors
        connector = new CustomerConnector();
        sqliteConnector = new SQLiteConnector(this);
    }

    private void loadCustomers() {
        sqliteConnector.openDatabase();
        ListCustomer lc = connector.getAllCustomer(sqliteConnector.getDatabase());
        if (lc != null && lc.getCustomers() != null) {
            adapter.clear();
            adapter.addAll(lc.getCustomers());
        }

    }

    private void addEvents() {
        lvCustomer.setOnItemLongClickListener((parent, view, i, l) -> {
            Customer selected = adapter.getItem(i);
            adapter.remove(selected);
            return true;
        });
    }
}
