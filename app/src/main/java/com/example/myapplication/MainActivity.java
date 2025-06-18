package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imgEmployee;
    TextView txtEmployee;
    ImageView imgCustomer;
    TextView txtCustomer;
    ImageView imgProduct;
    TextView txtProduct;

    ImageView imgCategory;
    TextView txtCategory;
    ImageView imgPaymentMethod;
    TextView txtPaymentMethod, txtPaymentMethodDescription;

    ImageView imgOrder;
    TextView txtOrder;

    ImageView imgTelephony;
    TextView txtTelephony;


    String DATABASE_NAME="SalesDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        addViews();
        addEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtNetworkType), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        processCopy();
    }

    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;

            myInput = getAssets().open(DATABASE_NAME);


            // Path to the just created empty db
            String outFileName = getDatabasePath(DATABASE_NAME).getPath();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addEvents() {
        imgOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi code mở màn hình quản trị nhân sự
                openOrderViewActivity();
            }
        });

        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi code mở màn hình quản trị nhân sự
                openOrderViewActivity();
            }
        });


        imgEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi code mở màn hình quản trị nhân sự
                openEmployeeManagementActivity();
            }
        });
        txtEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi code mở màn hình quản trị nhân sự
                openEmployeeManagementActivity();
            }
        });

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerManagementActivity();
            }
        });

        txtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerManagementActivity();
            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductManagementActivity();
            }
        });

        txtProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductManagementActivity();
            }
        });

        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductCategoryManagementActivity();
            }
        });

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductCategoryManagementActivity();
            }
        });


        txtPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentMethodActivity();
            }
        });

        imgPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentMethodActivity();
            }
        });

        txtTelephony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTelephonyActivity();
            }
        });

        imgTelephony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTelephonyActivity();
            }
        });
    }

    private void openTelephonyActivity() {
        Intent intent = new Intent(MainActivity.this, TelephonyActivity.class);
        startActivity(intent);
    }

    private void openOrderViewActivity() {
        Intent intent = new Intent(MainActivity.this, OrderViewerActivity.class);
        startActivity(intent);
    }

    private void openPaymentMethodActivity() {
        Intent intent = new Intent(MainActivity.this, PaymentMethodActivity.class);
        startActivity(intent);
    }

    private void openAdvancedProductManagementActivity() {
        Intent intent = new Intent(MainActivity.this, AdvancedProductManagementActivity.class);
        startActivity(intent);
    }

    void openEmployeeManagementActivity()
    {
        Intent intent = new Intent(MainActivity.this, EmployeeManagementActivity.class);
        startActivity(intent);
    }

    void openCustomerManagementActivity()
    {
        Intent intent = new Intent(MainActivity.this, CustomerManagementActivity.class);
        startActivity(intent);
    }

    void openProductManagementActivity()
    {
        Intent intent = new Intent(MainActivity.this, ProductManagementActivity.class);
        startActivity(intent);
    }

    void openProductCategoryManagementActivity()
    {
        Intent intent = new Intent(MainActivity.this, ProductCategoryManagement.class);
        startActivity(intent);
    }
    private void addViews() {
        imgEmployee = findViewById(R.id.imgEmployee);
        txtEmployee = findViewById(R.id.txtEmployee);
        imgCustomer = findViewById(R.id.imgCustomer);
        txtCustomer = findViewById(R.id.txtCustomer);
        imgProduct = findViewById(R.id.imgProduct);
        txtProduct = findViewById(R.id.txtProduct);
        imgCategory = findViewById(R.id.imgCategory);
        txtCategory = findViewById(R.id.txtCategory);
        imgPaymentMethod = findViewById(R.id.imgPaymentMethod);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        imgOrder = findViewById(R.id.imgOrder);
        txtOrder = findViewById(R.id.txtOrder);
        imgTelephony = findViewById(R.id.imgTelephony);
        txtTelephony = findViewById(R.id.txtTelephony);

    }
}

