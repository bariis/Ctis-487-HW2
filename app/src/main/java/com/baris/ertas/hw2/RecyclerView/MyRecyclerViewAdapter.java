package com.baris.ertas.hw2.RecyclerView;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.baris.ertas.hw2.Database.DatabaseHelper;
import com.baris.ertas.hw2.MainActivity;
import com.baris.ertas.hw2.R;
import com.baris.ertas.hw2.SecondActivity;
import com.baris.ertas.hw2.model.User;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyRecyclerViewItemHolder> {

    private Context context;
    private static ArrayList<User> recyclerItemValues;
    Dialog customDialog;
    User viewedUser;


    DatabaseHelper dbHelper;
    public MyRecyclerViewAdapter(Context context, ArrayList<User> values){
        this.context = context;
        this.recyclerItemValues = values;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyRecyclerViewItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflator = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflator.inflate(R.layout.recycler_item, viewGroup, false);

        MyRecyclerViewItemHolder mViewHolder = new MyRecyclerViewItemHolder(itemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewItemHolder holder, int position) {
        final User userItem = recyclerItemValues.get(position);

        holder.userName.setText(userItem.getName());
        holder.userCompany.setText(userItem.getCompany());
        holder.userEmail.setText(userItem.getEmail());
        System.out.println("onBindView'in altinda su an");
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondActivity.class);
                Bundle bundle = new Bundle();
                viewedUser = recyclerItemValues.get(position);
                bundle.putParcelable("viewedContact",viewedUser);
                intent.putExtras(bundle);
                context.startActivity(intent);
                //context.startActivityForResult(intent);
            }
        });
    }

     public void displayDialog(final String msg,int pos){

        EditText emailTV, phoneTV;
        Button buttonUpdate;

        customDialog = new Dialog(context);

        customDialog.setContentView(R.layout.dialog);
        emailTV =  customDialog.findViewById(R.id.updateEmail);
        buttonUpdate = customDialog.findViewById(R.id.updateButton);
        phoneTV = customDialog.findViewById(R.id.updatePhone);
        emailTV.setText(viewedUser.getEmail());
        phoneTV.setText(viewedUser.getPhone());

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "butona basildi", Toast.LENGTH_SHORT).show();
            }
        });
        customDialog.show();
    }

    public void refreshMyAdapterAfterDelete(int position){
        notifyItemRangeRemoved(0, recyclerItemValues.size());
        recyclerItemValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeInserted(0, recyclerItemValues.size());
    }

    @Override
    public int getItemCount() {
        return recyclerItemValues.size();
    }

    class MyRecyclerViewItemHolder extends  RecyclerView.ViewHolder{
        TextView userName, userEmail, userCompany;
        ConstraintLayout parentLayout;
        public MyRecyclerViewItemHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userCompany = itemView.findViewById(R.id.userCompany);
            parentLayout = itemView.findViewById(R.id.itemConstLayout);
        }
    }
}
