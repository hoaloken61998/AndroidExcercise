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

public class CustomerManagementActivity extends AppCompatActivity {

    ListView lvCustomer;
    ArrayAdapter<Customer> adapter;
    CustomerConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addView();
        addEvents();
    }

    private void addEvents() {
        lvCustomer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Customer selected = adapter.getItem(position);
//                adapter.remove(selected);
                Customer c = adapter.getItem(position);
                displayCustomerDetailActivity(c);
                return false;
            }
        });
//        lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Customer c = adapter.getItem(position);
//                displayCustomerDetailActivity(c);
//            }
//        });
    }

    private void displayCustomerDetailActivity(Customer c) {
        Intent intent = new Intent(CustomerManagementActivity.this, CustomerDetailActivity.class);
        intent.putExtra("SELECTED_CUSTOMER",c);
//        startActivity(intent);
        startActivityForResult(intent, 400);
    }

    private void addView() {
        lvCustomer = findViewById(R.id.txtNetworkType);
        adapter = new ArrayAdapter<>(CustomerManagementActivity.this, android.R.layout.simple_list_item_1);
        connector = new CustomerConnector();
        ListCustomer lc = connector.getAllCustomer(new SQLiteConnector(this).openDatabase());
        adapter.addAll(lc.getCustomers());
        lvCustomer.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu_customer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_new_customer){

            Toast.makeText(CustomerManagementActivity.this,"Mở màn hình thêm mới khách hàng",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CustomerManagementActivity.this, CustomerDetailActivity.class);
            //đóng gói và đặt mã request code là 300
            startActivityForResult(intent,300);
            //startActivity(intent)
        }
        else if(item.getItemId()==R.id.menu_broadcast_advertising){

            Toast.makeText(CustomerManagementActivity.this,"Gửi quảng cáo hàng loạt tới khách hàng",Toast.LENGTH_LONG).show();

        }
        else if(item.getItemId()==R.id.menu_help){
            
            Toast.makeText(CustomerManagementActivity.this,"Mở Website hướng dẫn sử dụng",Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //trường hợp xử lý cho NEW CUSTOMER ta chỉ quan tâm 300 và 500 do ta định nghĩa:
        if (requestCode == 300 && resultCode == 500) {
            if (data != null) {
                Customer c = (Customer) data.getSerializableExtra("NEW CUSTOMER");
                if (c != null) {
                    process_save_customer(c);
                } else {
                    // Handle the case where the customer object is null
                    Toast.makeText(this, "Error: Could not retrieve customer details.", Toast.LENGTH_LONG).show();
                }
            } else {
                // Handle the case where data itself is null
                Toast.makeText(this, "Error: No data received from the previous activity.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 400 && resultCode == 500) {
            if (data != null) {
                Customer c = (Customer) data.getSerializableExtra("UPDATED CUSTOMER"); // Ensure this key is correct
                if (c != null) {
                    process_save_update_customer(c);
                } else {
                    Toast.makeText(this, "Error: Could not retrieve updated customer details.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Error: No data received for update.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode==400 && resultCode==600) {
            //xóa dữ liệu cho Customer:
            //lấy gói tin ra:
            String id = data.getStringExtra("REMOVED CUSTOMER ID");
            process_remove_customer(id);
        }
    }

    private void process_remove_customer(String id) {
        SQLiteConnector connector = new SQLiteConnector(this);
        SQLiteDatabase database = connector.openDatabase();
        CustomerConnector cc = new CustomerConnector();
        long flag = cc.removeCustomer(database,id);
        if(flag>0){
            adapter.clear();
            adapter.addAll(cc.getAllCustomer(database).getCustomers());
            Toast.makeText(CustomerManagementActivity.this,"Xóa thành công",Toast.LENGTH_LONG).show();
        }
    }

    private void process_save_update_customer(Customer c) {
        SQLiteConnector con = new SQLiteConnector(this);
        SQLiteDatabase database = con.openDatabase();
        CustomerConnector cc = new CustomerConnector();
        long flag = cc.save_update_customer(c, database); // Corrected 'da' to 'database'
        if(flag > 0) {
            // Thêm thành công (Update successful)
            Toast.makeText(this, "Cập nhật khách hàng thành công", Toast.LENGTH_LONG).show(); // Changed message to reflect update
            adapter.clear();
            adapter.addAll(cc.getAllCustomer(database).getCustomers());
        } else {
            // Thêm thất bại (Update failed)
            Toast.makeText(this, "Cập nhật khách hàng thất bại", Toast.LENGTH_LONG).show(); // Changed message to reflect update
        }
    }

    private void process_save_customer(Customer c) {
//        boolean result = connector.isExist(c);
//        if(result==true){
//            //tức l customer này đã tồn tại trong hệ thống
//            //họ có nhu cầu sử các thông tin khác, ví dụ:
//            //Địa chỉ, payment method,..
//            //Sinh viên tự xử lý trường hợp sửa thông tin
//        }
//        else{
//            //là thêm mới Customer
//            connector.addCustomer(c);
//            adapter.clear();
//            adapter.addAll(connector.get_all_customers());
//        }
        SQLiteConnector con = new SQLiteConnector(this);
        SQLiteDatabase database = null;
        try {
            database = con.openDatabase();
            CustomerConnector cc = new CustomerConnector();
            long flag = cc.insertNewCustomer(database, c);

            if (flag > 0) { // rowId of new row, or -1 if error
                Toast.makeText(this, "Thêm mới khách hàng thành công. ID: " + flag, Toast.LENGTH_LONG).show();
                adapter.clear();
                adapter.addAll(cc.getAllCustomer(database).getCustomers()); // Refresh list
            } else {
                // flag is likely -1 if an error occurred (e.g., constraint violation)
                Toast.makeText(this, "Thêm mới khách hàng thất bại. (Mã lỗi DB: " + flag + ")", Toast.LENGTH_LONG).show();
                // For debugging, consider logging details of 'c' and the error.
                // android.util.Log.e("SAVE_CUSTOMER_FAIL", "Failed to insert customer: " + c.toString() + ". DB error code: " + flag);
            }
        } catch (Exception e) { // Catch SQLiteException or other runtime errors
            Toast.makeText(this, "Lỗi khi lưu khách hàng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // android.util.Log.e("SAVE_CUSTOMER_ERROR", "Exception during save: ", e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close(); // Ensure the database is closed
            }
        }
    }

}
