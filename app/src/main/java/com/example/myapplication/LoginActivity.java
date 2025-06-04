package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.connectors.SQLiteConnector;
import com.example.models.Employee;
import com.example.connectors.EmployeeConnectors;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassword;
    CheckBox chkSaveLoginInfor;
    Button btnLogin;
    ImageView imgExit;
    private boolean doubleBackToExitPressedOnce = false;
    private static final long DOUBLE_BACK_PRESS_THRESHOLD = 1200;

    String DATABASE_NAME="SalesDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        addView();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvCustomer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addView() {
        edtUserName=findViewById(R.id.edtUserName);
        edtPassword=findViewById(R.id.edtPassword);
        chkSaveLoginInfor=findViewById(R.id.chkSaveLoginInfor);
        btnLogin=findViewById(R.id.btnLogin);
        imgExit=findViewById(R.id.imgExit);
    }


    public void do_login(View view) {

        String usr=edtUserName.getText().toString();
        String pwd=edtPassword.getText().toString();
        EmployeeConnectors ec=new EmployeeConnectors();

        // Open the database
        SQLiteConnector sqLiteConnector= new SQLiteConnector(this);
        sqLiteConnector.openDatabase();

        Employee emp=ec.login_sql(sqLiteConnector.getDatabase(),usr,pwd);
        if(emp!=null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,
                    "Login failed - please check your account again!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void do_exit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        Resources res = getResources();

        //tiêu đề
        builder.setTitle(res.getText(R.string.title_confirm_exit));
        //nội dung cửa sổ:
        builder.setMessage(res.getText(R.string.title_confirm_exit_message));
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        //Thiết lập tương tác YES:
        builder.setPositiveButton(res.getText(R.string.title_confirm_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.ext(0);
                finish();
            }
        });
        builder.setNegativeButton(res.getText(R.string.title_confirm_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return true;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, DOUBLE_BACK_PRESS_THRESHOLD);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void saveLoginInformation() {
        // Implement logic to save login information if needed
        // This could involve saving to SharedPreferences or a database

        SharedPreferences preferences=getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        String username = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        boolean isSave = chkSaveLoginInfor.isChecked();
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.putBoolean("SAVE", isSave);
        editor.commit();
    }

    @Override
    protected void onPause() {
        // Khi màn hình tạm dừng, lưu thông tin đăng nhập, đứng ở onPause mà ko đủ tài nguyên thì
        // sẽ chuyển thành on stop
        super.onPause();
        saveLoginInformation();
    }

    public void restoreLoginInformation() {
        // Implement logic to restore login information if needed
        // This could involve reading from SharedPreferences or a database

        SharedPreferences preferences = getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        String username = preferences.getString("USERNAME", "");
        String password = preferences.getString("PASSWORD", "");
        boolean isSave = preferences.getBoolean("SAVE", true);

        edtUserName.setText(username);
        edtPassword.setText(password);
        chkSaveLoginInfor.setChecked(isSave);

        if (isSave) {
            edtUserName.setText(username);
            edtPassword.setText(password);
            chkSaveLoginInfor.setChecked(true);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreLoginInformation();
    }
}