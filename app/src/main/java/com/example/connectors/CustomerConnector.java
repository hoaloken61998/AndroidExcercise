package com.example.connectors;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Customer;
import com.example.models.ListCustomer;

import java.util.ArrayList;

public class CustomerConnector {
    private ListCustomer listCustomer;

    public CustomerConnector() {
        listCustomer = new ListCustomer();
        listCustomer.generate_sample_dataset();
    }

    public ArrayList<Customer> get_all_customers() {
        if (listCustomer == null) {  // Remove semicolon
            listCustomer = new ListCustomer();
            listCustomer.generate_sample_dataset();
        }
        return listCustomer.getCustomers();
    }

    public ArrayList<Customer> get_customer_by_provider(String provider) {
        if (listCustomer == null) {  // Remove semicolon
            listCustomer = new ListCustomer();
            listCustomer.generate_sample_dataset();
        }
        ArrayList<Customer> results = new ArrayList<>();
        for(Customer c: listCustomer.getCustomers()) {
            if (c.getPhone().startsWith(provider)) {
                results.add(c);
            }
        }
        return results;
    }
    public boolean isExist(Customer c){
        return listCustomer.isExist(c);
    }
    public void addCustomer(Customer c) {
        listCustomer.addCustomer(c);
    }

    /**
    * Đây là hàm truy vấn toàn bộ dữ liệu khách hàng từ cơ sở dữ liệu SQLite,
    * sau đó mô hình dữ liệu này thành theo hướng đối tượng
    * và trả về danh sách ListCustomer.
    * @param database SQLiteDatabase
    * @return ListCustomer
    * */
    public ListCustomer getAllCustomer(SQLiteDatabase database) {
        listCustomer = new ListCustomer();

        Cursor cursor = database.rawQuery("SELECT * FROM Customer",null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            String username = cursor.getString(4);
            String password = cursor.getString(5);

            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setUsername(username);
            customer.setPassword(password);
            listCustomer.addCustomer(customer);
            //To do something ….
        }
        cursor.close();
        return listCustomer;
    }

    public long insertNewCustomer(SQLiteDatabase database, Customer c) {
        ContentValues values = new ContentValues();

        values.put("Name", c.getName());
        values.put("Email", c.getEmail());
        values.put("Phone", c.getPhone());
        values.put("Username", c.getUsername());
        values.put("Password", c.getPassword());
        long flag = database.insert("Customer", null, values); // Changed int to long
        return flag;
    }

    public long save_update_customer(Customer c, SQLiteDatabase database) { // Changed private to public
        ContentValues values = new ContentValues();
        values.put("Name", c.getName());
        values.put("Email", c.getEmail());
        values.put("Phone", c.getPhone());
        values.put("Username", c.getUsername());
        values.put("Password", c.getPassword());
        long flag = database.update("Customer", values, "Id=?", new String[]{c.getId()+""}); // Changed int to long
        return flag;
    }

    public long removeCustomer(SQLiteDatabase database, String id){
        int flag = database.delete("Customer","Id=?",new String[]{id});
        return flag;
    }
}