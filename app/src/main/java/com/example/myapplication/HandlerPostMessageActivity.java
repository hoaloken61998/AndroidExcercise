package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Customer;

import java.util.Random;

public class HandlerPostMessageActivity extends AppCompatActivity {

    EditText edtNumberOfCustomer;
    Button btnLoadCustomer;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    ListView lvCustomer;
    ArrayAdapter<Customer> adapters;

    int numb = 0, percent, value;
    Handler handler = new Handler();
    Customer customerInBackground = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_handler_post_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtPercent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnLoadCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCustomerInBackground();
            }
        });
    }
    Runnable foregroundThread = new Runnable() {
        @Override
        public void run() {
            txtPercent.setText(percent + " %");
            progressBarPercent.setProgress(percent);

            if (customerInBackground != null) {
                adapters.add(customerInBackground);
                adapters.notifyDataSetChanged();
            }
            if (percent >= 100) {
                Toast.makeText(HandlerPostMessageActivity.this, "Load customer successfully", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadCustomerInBackground() {
        numb = Integer.parseInt(edtNumberOfCustomer.getText().toString());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for(int i=1; i<=numb; i++){
                    percent = i * 100 / numb;

                    customerInBackground = new Customer();
                    customerInBackground.setId(i);
                    customerInBackground.setName("Customer " + i);
                    customerInBackground.setEmail("customer" + i + "@example.com");
                    String []providers = {"098", "097", "090", "091", "092", "093", "094", "095", "096"};
                    String phone = providers[random.nextInt(providers.length)];
                    for (int j = 0; j < 7; j++) {
                        phone += random.nextInt(10);
                    }
                    customerInBackground.setPhone(phone);
                    value = random.nextInt(100);
                    handler.post(foregroundThread);
                    SystemClock.sleep(100);
                }
            }
        });
        thread.start();



    }

    private void addViews() {
        edtNumberOfCustomer = findViewById(R.id.edtNumberOfCustomer);
        btnLoadCustomer = findViewById(R.id.btnLoadCustomer);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarPercent = findViewById(R.id.progressBarPercent);
        lvCustomer = findViewById(R.id.lvCustomer);
        adapters = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvCustomer.setAdapter(adapters);
    }
}