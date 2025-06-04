package com.example.models;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ListPaymentMethod {

    ArrayList<PaymentMethod> paymentMethods;

    public ListPaymentMethod() {
        this.paymentMethods = new ArrayList<>();
    }

    public ListPaymentMethod(SQLiteDatabase database) {
        this.paymentMethods = new ArrayList<>();
        loadFromDatabase(database);
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(ArrayList<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public void generate_payments_methods() {
        paymentMethods.add(new PaymentMethod(1, "Cash", "Thanh toán bằng tiền mặt"));
        paymentMethods.add(new PaymentMethod(2, "Credit Card", "Thanh toán bằng thẻ tín dụng"));
        paymentMethods.add(new PaymentMethod(3, "Digital Wallet", "Thanh toán bằng ví điện tử"));
        paymentMethods.add(new PaymentMethod(4, "COD", "Nhận hàng rồi thanh toán"));
    }

    public void loadFromDatabase(SQLiteDatabase database) {
        paymentMethods.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM PaymentMethod", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            paymentMethods.add(new PaymentMethod(id, name, description));
        }
        cursor.close();
    }
}
