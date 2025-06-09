package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.connectors.OrderViewerConnector;
import com.example.connectors.SQLiteConnector;
import com.example.models.OrderViewer;

import java.util.ArrayList;

import adapter.OrderViewerAdapter;

public class OrderViewerActivity extends AppCompatActivity {

    ListView lvOrderViewer;
    OrderViewerAdapter orderViewerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_viewer);

        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvOrderViewer = findViewById(R.id.lvOrderViewer);
        orderViewerAdapter = new OrderViewerAdapter(this, R.layout.item_orderview);
        lvOrderViewer.setAdapter(orderViewerAdapter);

        // Set item click listener using lambda for cleaner code
        lvOrderViewer.setOnItemClickListener((parent, view, position, id) -> {
            OrderViewer selectedOrder = orderViewerAdapter.getItem(position);
            showOrderDetails(selectedOrder);
        });

        SQLiteConnector connector = new SQLiteConnector(this);
        OrderViewerConnector ovc = new OrderViewerConnector();
        ArrayList<OrderViewer> dataset = ovc.getAllOrdersViewer(connector.getDatabase());

        // Add data to adapter
        for (OrderViewer order : dataset) {
            orderViewerAdapter.add(order);
        }
        orderViewerAdapter.notifyDataSetChanged();
    }

    private void showOrderDetails(OrderViewer order) {
        Intent intent = new Intent(OrderViewerActivity.this, OrderDetailsActivity.class);
        // We use Parcelable/Serializable here because we made Orders implement Serializable
        intent.putExtra("SELECTED_ORDER", order);
        startActivity(intent);
    }
}