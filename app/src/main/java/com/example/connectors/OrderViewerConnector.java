package com.example.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Customer;
import com.example.models.OrderViewer;

import java.util.ArrayList;

public class OrderViewerConnector {
    public ArrayList<OrderViewer> getAllOrdersViewer(SQLiteDatabase database) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");
        builder.append(" o.Id AS OrderId, ");
        builder.append(" o.Code AS OrderCode, ");
        builder.append(" o.OrderDate, ");
        builder.append(" e.Name AS EmployeeName, ");
        builder.append(" c.Name AS CustomerName, ");
        builder.append(" SUM(od.Quantity * od.Price - od.Discount / 100.0) ");
        builder.append(" FROM Orders o ");
        builder.append(" JOIN Employee e ON o.EmployeeId = e.Id ");
        builder.append(" JOIN Customer c ON o.CustomerId = c.Id ");
        builder.append(" JOIN OrderDetails od ON o.Id = od.OrderId ");
        builder.append(" GROUP BY o.Id, o.Code, o.OrderDate, e.Name, c.Name; ");

        Cursor cursor = database.rawQuery(builder.toString(), null);

        ArrayList<OrderViewer> datasets = new ArrayList<>();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String code = cursor.getString(1);
            String orderDate = cursor.getString(2);
            String employeeName = cursor.getString(3);
            String customerName = cursor.getString(4);
            double totalValue = cursor.getDouble(5);

            OrderViewer ov = new OrderViewer();
            ov.setId(id);
            ov.setCode(code);
            ov.setOrderDate(orderDate);
            ov.setEmployeeName(employeeName);
            ov.setCustomerName(customerName);
            ov.setTotalOrderValue(totalValue);
            datasets.add(ov);

        }
        cursor.close();

        return datasets;
    }
}
