package com.example.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.ListPaymentMethod;
import com.example.models.PaymentMethod;

public class PaymentMethodConnector {
    private ListPaymentMethod listPaymentMethod;

    public PaymentMethodConnector() {
        listPaymentMethod = new ListPaymentMethod();
        listPaymentMethod.generate_payments_methods();
    }

    /**
     * Query all payment methods from SQLite database and return as ListPaymentMethod
     * @param database SQLiteDatabase
     * @return ListPaymentMethod
     */
    public ListPaymentMethod getAllPaymentMethods(SQLiteDatabase database) {
        listPaymentMethod = new ListPaymentMethod();
        Cursor cursor = database.rawQuery("SELECT * FROM PaymentMethod", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            PaymentMethod paymentMethod = new PaymentMethod(id, name, description);
            listPaymentMethod.getPaymentMethods().add(paymentMethod);
        }
        cursor.close();
        return listPaymentMethod;
    }
}

