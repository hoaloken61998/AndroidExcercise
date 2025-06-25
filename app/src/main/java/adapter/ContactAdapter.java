package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private List<String> contacts;
    private LayoutInflater inflater;

    public ContactAdapter(Context context, List<String> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(contacts.get(position));
        // Example: highlight the first item
        if (position == 0) {
            textView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            textView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
        return convertView;
    }

    public void clear() {
        contacts.clear();
        notifyDataSetChanged();
    }

    public void add(String contact) {
        contacts.add(contact);
        notifyDataSetChanged();
    }
}
