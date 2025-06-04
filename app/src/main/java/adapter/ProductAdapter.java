package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.models.Product;
import com.example.myapplication.R;

public class ProductAdapter extends ArrayAdapter<Product> {

    Activity context;
    int resource;
    public ProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        // Nhân bản giao diện theo từ vị trí position trong danh sách
        View item=inflater.inflate(this.resource, null);
        // Lúc này toàn bộ view nằm trong layout resource(item_advanced_product)
        // sẽ được mô hình hóa hướng đối tượng và dc quản lý với biến item
        // như vậy muốn truy xuất tới các view con trong nó phải thông qua biến item .
        ImageView imgProduct = item.findViewById(R.id.imgProduct);
        TextView txtProductId = item.findViewById(R.id.txtProductId);
        TextView txtProductName = item.findViewById(R.id.txtProductName);
        TextView txtProductQuantity = item.findViewById(R.id.txtProductQuantity);
        TextView txtProductPrice = item.findViewById(R.id.txtProductPrice);
        ImageView imgCart = item.findViewById(R.id.imgCart);

        //Lấy mô hình đối tượng đang được nhân bản ở vị trí position (đối số 1):
        Product p = getItem(position);
        // Rải dữ liệu của Product lấy giao diện trong item
        imgProduct.setImageResource(p.getImage_id());
        txtProductId.setText(p.getId()+"");
        txtProductName.setText(p.getName());
        txtProductQuantity.setText(p.getQuantity()+"");
        txtProductPrice.setText(p.getPrice()+"");
        imgCart.setImageResource(R.mipmap.ic_cart);

        // Xử lý bấm vào nút mua.... tính sau....

        return item;
    }
}
