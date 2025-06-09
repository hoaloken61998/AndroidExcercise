package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.models.OrderDetail;
import com.example.myapplication.R;

public class OrderDetailAdapter extends ArrayAdapter<OrderDetail> {

    Activity context;
    int resource;

    public OrderDetailAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = (Activity) context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(resource, parent, false);
        }

        OrderDetail detail = getItem(position);

        if (detail != null) {
            TextView txtProductName = item.findViewById(R.id.txtProductName);
            TextView txtQuantity = item.findViewById(R.id.txtQuantity);
            TextView txtPrice = item.findViewById(R.id.txtPrice);
            TextView txtDiscount = item.findViewById(R.id.txtDiscount);
            TextView txtTotal = item.findViewById(R.id.txtTotal);

            txtProductName.setText(detail.getProductName());
            txtQuantity.setText(String.valueOf(detail.getQuantity()));
            txtPrice.setText(String.format("$%.2f", detail.getPrice()));
            txtDiscount.setText(String.format("%.2f%%", detail.getDiscount()));
            txtTotal.setText(String.format("$%.2f", detail.getTotal()));
        }

        return item;
    }
}
