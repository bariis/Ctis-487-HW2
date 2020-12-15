package com.baris.ertas.hw2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.baris.ertas.hw2.model.User;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<User> {

    Context context;

    ArrayList<User> spinnerUserItems;
    public CustomSpinnerAdapter(@NonNull Context context, @NonNull ArrayList<User> spinnerUserItems) {
        super(context, R.layout.spinner_item, spinnerUserItems);
        this.context = context;
        this.spinnerUserItems = spinnerUserItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.spinner_item, parent, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.itemConstraintLayout);
        TextView spinnerUserEmail = view.findViewById(R.id.spinnerUserEmail);
        User selectedUser = spinnerUserItems.get(position);
        spinnerUserEmail.setText(selectedUser.getEmail());

        return view;
    }
}
