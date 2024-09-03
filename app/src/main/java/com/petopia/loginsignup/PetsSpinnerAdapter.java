package com.petopia.loginsignup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PetsSpinnerAdapter extends ArrayAdapter<Pets> {

    private List<Pets> petsList;
    private LayoutInflater inflater;

    public PetsSpinnerAdapter(Context context, List<Pets> petsList) {
        super(context, 0, petsList);
        this.petsList = petsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(petsList.get(position).getPet_Name());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(petsList.get(position).getPet_Name());

        return convertView;
    }
}
