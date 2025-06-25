package com.example;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import java.util.Random;

public class AsyncTaskActivity extends AppCompatActivity {

    EditText edtNumberOfButton;
    Button btnDrawButton;
    TextView txtPercent;
    ProgressBar progressBar;
    LinearLayout LinearLayoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_async_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtPercent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawButtonRealTime();
            }
        });



    }

    private void drawButtonRealTime() {
        int n = Integer.parseInt(edtNumberOfButton.getText().toString());
        MyDrawButtonTask task = new MyDrawButtonTask();
        task.execute(n);

    }

    private void addViews() {
        edtNumberOfButton = findViewById(R.id.edtNumberOfButton);
        btnDrawButton = findViewById(R.id.btnDrawButton);
        txtPercent = findViewById(R.id.txtPercent);
        progressBar = findViewById(R.id.progressBarPercent);
        LinearLayoutButton = findViewById(R.id.LinearLayoutButton);
    }

    class MyDrawButtonTask extends AsyncTask<Integer, Integer, Double> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtPercent.setText("0%");
            progressBar.setProgress(0);
            LinearLayoutButton.removeAllViews();
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            txtPercent.setText("100%");
            progressBar.setProgress(100);
            AlertDialog.Builder builder = new AlertDialog.Builder(AsyncTaskActivity.this);
            builder.setMessage("Giá trị trung bình là: " + aDouble)
                    .setTitle("Kết quả")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // hàm tự động kích hoạt khi publishProgress được gọi
            int percent = values[0];
            int value = values[1];
            txtPercent.setText(percent + "%");
            progressBar.setProgress(percent);
            // Tạo nút mới và thêm vào LinearLayout
            Button button = new Button(AsyncTaskActivity.this);
            button.setText(value+ "");
            button.setWidth(300);
            button.setHeight(10);
            LinearLayoutButton.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setTextColor(Color.RED);
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    button.setVisibility(View.GONE);
                    return false;
                }
            });
        }

        @Override
        protected void onCancelled(Double aDouble) {
            super.onCancelled(aDouble);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Double doInBackground(Integer... integers) {
            int n = integers[0];
            double avg = 0;
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                int percent = (i * 100) / n;
                int value = random.nextInt(100);
                avg += value;
                // Đẩy dữ liệu qua onProgressUpdate để vẽ giao diện thời gian thực
                publishProgress(percent, value);
                SystemClock.sleep(100); // Giả lập thời gian xử lý
                avg = avg + value;
            }
            return avg;

        }
    }
}