package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HandlerSendMessageActivity extends AppCompatActivity {

    EditText edtNumber;
    Button btnDraw;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    LinearLayout linearLayoutButtons;
    Handler handler;
    int numb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_handler_send_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtPercent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnDraw.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                doDrawMultiThreading();
        }
        });;
    }

    private void createHandlerCLass() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int percent = message.arg1;
                if(percent == 100){
                    Toast.makeText(HandlerSendMessageActivity.this, "DONE",
                            Toast.LENGTH_LONG).show();
                    txtPercent.setText("100 %");
                    progressBarPercent.setProgress(100);
                    btnDraw.setVisibility(View.VISIBLE);
                }
                else
                {
                    int value = (int) message.obj;
                    // Đưa value lên giao diện để cập nhật GUI thời gian thực:
                    Button btn = new Button(HandlerSendMessageActivity.this);
                    btn.setText(value+"");
                    btn.setWidth(300);
                    btn.setHeight(100);
                    linearLayoutButtons.addView(btn);
                }
                txtPercent.setText(percent + " %");
                progressBarPercent.setProgress(percent);
                return true;
            }
        });
    }

    private void startThread()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for(int i=1; i<=numb; i++){
                    Message message = handler.obtainMessage();
                    message.arg1 =  i*100/numb; //Percent
                    message.obj = random.nextInt(100);
                    handler.sendMessage(message);
                    SystemClock.sleep(10);
                }
            }
        });
        thread.start();


    }
    private void doDrawMultiThreading() {
        createHandlerCLass();
        numb = Integer.parseInt(edtNumber.getText().toString());
        // Khóa button Draw
        btnDraw.setVisibility(View.INVISIBLE);
        txtPercent.setText("0 %");
        progressBarPercent.setProgress(0);
        linearLayoutButtons.removeAllViews();
        startThread();
    }

    private void addViews() {
        edtNumber = findViewById(R.id.edtNumber);
        btnDraw = findViewById(R.id.btnDraw);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarPercent = findViewById(R.id.progressBarPercent);
        linearLayoutButtons = findViewById(R.id.linearLayoutButtons);
    }
}
