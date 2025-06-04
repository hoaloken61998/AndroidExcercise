package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.ListPaymentMethod;
import com.example.connectors.PaymentMethodConnector;
import com.example.connectors.SQLiteConnector;

import adapter.PaymentMethodAdapter;

public class PaymentMethodActivity extends AppCompatActivity {

    ListView lvPaymentMethod;
    PaymentMethodAdapter paymentMethodAdapter;
    ListPaymentMethod lpm;
    SQLiteConnector sqliteConnector;
    PaymentMethodConnector paymentMethodConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);

        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvPaymentMethod = findViewById(R.id.lvPaymentMethod);
        paymentMethodAdapter = new PaymentMethodAdapter(PaymentMethodActivity.this, R.layout.item_payment_method);
        lvPaymentMethod.setAdapter(paymentMethodAdapter);
        // Initialize connectors
        sqliteConnector = new SQLiteConnector(this);
        paymentMethodConnector = new PaymentMethodConnector();
        sqliteConnector.openDatabase();
        // Use PaymentMethodConnector to get ListPaymentMethod from database
        lpm = paymentMethodConnector.getAllPaymentMethods(sqliteConnector.getDatabase());
        paymentMethodAdapter.clear();
        paymentMethodAdapter.addAll(lpm.getPaymentMethods());
    }

}

