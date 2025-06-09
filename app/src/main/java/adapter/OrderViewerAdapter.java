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

import com.example.models.OrderViewer;
import com.example.myapplication.R;

public class OrderViewerAdapter extends ArrayAdapter<OrderViewer>
{
    Activity context;
    int resource;

    public OrderViewerAdapter(@NonNull Context context, int resource) {
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

        OrderViewer ov = getItem(position);

        if (ov != null) {
            TextView txtOrderCode = item.findViewById(R.id.txtOrderCode);
            TextView txtOrderDate = item.findViewById(R.id.txtOrderDate);
            TextView txtCustomerName = item.findViewById(R.id.txtCustomerName);
            TextView txtEmployeeName = item.findViewById(R.id.txtEmployeeName);
            TextView txtTotalOrderValue = item.findViewById(R.id.txtTotalOrderValue);

            txtOrderCode.setText(ov.getCode());
            txtOrderDate.setText(ov.getOrderDate());
            txtCustomerName.setText(ov.getCustomerName());
            txtEmployeeName.setText(ov.getEmployeeName());
            txtTotalOrderValue.setText(String.format("%.2f", ov.getTotalOrderValue()));
        }

        return item;
    }
}
