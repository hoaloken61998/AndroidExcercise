package adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.models.PaymentMethod;
import com.example.myapplication.R;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {

    Activity context;
    int resource;

    Typeface typeface;
    public PaymentMethodAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/UVNBayBuomHep_R.TTF");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item = inflater.inflate(resource, parent, false);
        TextView txtPaymentMethodName = item.findViewById(R.id.txtPaymentMethodName);
        TextView txtPaymentMethodDescription = item.findViewById(R.id.txtPaymentMethodDescription);

        PaymentMethod pm = getItem(position);
        txtPaymentMethodName.setText(pm.getName());
        txtPaymentMethodDescription.setText(pm.getDescription());

        txtPaymentMethodName.setTypeface(this.typeface);

        return item;
    }

}
