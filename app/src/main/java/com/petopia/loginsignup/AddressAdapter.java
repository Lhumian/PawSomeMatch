package com.petopia.loginsignup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddressModel> addressList;

    public AddressAdapter(Context context, ArrayList<AddressModel> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.address_item_layout, null);
        }

        // Get the AddressModel object for this position
        AddressModel address = addressList.get(position);

        // Bind data to UI elements in the address_item_layout.xml
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView phoneTextView = convertView.findViewById(R.id.phoneTextView);
        TextView cityTextView = convertView.findViewById(R.id.cityTextView);
        TextView barangayTextView = convertView.findViewById(R.id.barangayTextView);
        TextView streetTextView = convertView.findViewById(R.id.streetTextView);

        // Set the values from the AddressModel to the TextViews
        nameTextView.setText(address.getName());
        phoneTextView.setText(address.getPhone());
        cityTextView.setText(address.getCity());
        barangayTextView.setText(address.getBarangay());
        streetTextView.setText(address.getStreet());

        return convertView;
    }
}
