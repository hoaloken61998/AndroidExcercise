package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.utils.BMIResult;
import com.example.utils.Healthcare;


public class EmployeeHealhCareActivity extends AppCompatActivity {

    EditText edtHeight;
    EditText edtWeight;
    Button btnCalculate, btnClear, btnClose;
    TextView txtResult;

    View.OnClickListener myClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_healh_care);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtNetworkType), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        addViews();
        addEvents();

    }

    private void addEvents() {
        myClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(btnCalculate))
                {
                    //View calculate use this event
                    double height = Double.parseDouble(edtHeight.getText().toString());
                    double weight = Double.parseDouble(edtWeight.getText().toString());
                    BMIResult result = Healthcare.calculateBMI(height, weight, EmployeeHealhCareActivity.this);
                    txtResult.setText(result.getBMI()+"=>"+result.getDescription());
                }
                else if(view.equals(btnClear))
                {
                 //View clear use this  event
                    edtHeight.setText("");
                    edtHeight.setText("");
                    txtResult.setText("");
                    edtHeight.requestFocus();
                }
                else if(view.equals(btnClose))
                {
                    //View close use this event
                    finish();
                }
            }

        };
        btnCalculate.setOnClickListener(myClick);
        btnClear.setOnClickListener(myClick);
        btnClose.setOnClickListener(myClick);
    }

    private void addViews() {
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        btnClose = findViewById(R.id.btnClose);

        txtResult= findViewById(R.id.txtResult);
    }
    
}