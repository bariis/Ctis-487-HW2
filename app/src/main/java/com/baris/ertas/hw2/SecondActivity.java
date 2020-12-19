package com.baris.ertas.hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baris.ertas.hw2.Database.DatabaseHelper;
import com.baris.ertas.hw2.Database.UserDB;
import com.baris.ertas.hw2.RecyclerView.MyRecyclerViewAdapter;
import com.baris.ertas.hw2.model.User;

public class SecondActivity extends AppCompatActivity {

    Intent intent;
    Dialog customDialog;
    MyRecyclerViewAdapter recyclerAdapter;
    //DatabaseHelper
    DatabaseHelper dbHelper = MainActivity.dbHelper;
    TextView name, email, city, phone, company;
    Button addButton, deleteButton, editButton;
    User viewedUser;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_second);

        name = findViewById(R.id.viewedUserName);
        email = findViewById(R.id.viewedUserEmail);
        city = findViewById(R.id.viewedUserCity);
        phone = findViewById(R.id.viewedUserPhone);
        company = findViewById(R.id.viewedUserCompany);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        intent = getIntent();
        Bundle b = intent.getExtras();
        viewedUser = b.getParcelable("viewedContact");
        id = b.getInt("id");
        name.setText(viewedUser.getName());
        email.setText(viewedUser.getEmail());
        city.setText(viewedUser.getCity());
        phone.setText(viewedUser.getPhone());
        company.setText(viewedUser.getCompany());
    }

    public void addButton(View view) {
        Toast.makeText(this, "add tiklandi", Toast.LENGTH_SHORT).show();
    }
    public void editButton(View view) {
        displayDialog("dialog", 1);
    }
    public void deleteButton(View view) {
        boolean isDeletedSuccesfully = UserDB.delete(dbHelper, String.valueOf(viewedUser.getId()));
        if(isDeletedSuccesfully) {
            Intent intent = new Intent();
            //intent.putExtra("deletedPos", viewedUser.getId());
            System.out.println("BURADA VIEW EDILEN USERIN IDSI VAR ==== " + viewedUser.getId() + " ISIM: " + viewedUser.getName());
            //intent.putExtra("deletedUser", String.valueOf(viewedUser.getId()));
            intent.putExtra("deletedUserId", id);
            setResult(RESULT_OK, intent);
            System.out.println("BURASI DELETED SUCCESFULL BUTONU ICI");
            Toast.makeText(SecondActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
            //recyclerAdapter.refreshMyAdapterAfterDelete(viewedUser.getId());
            finish();
        }
    }

    public void displayDialog(final String msg,int pos){

        EditText emailTV, phoneTV;
        Button buttonUpdate;

        customDialog = new Dialog(this);

        customDialog.setContentView(R.layout.dialog);
        emailTV =  customDialog.findViewById(R.id.updateEmail);
        buttonUpdate = customDialog.findViewById(R.id.updateButton);
        phoneTV = customDialog.findViewById(R.id.updatePhone);
        emailTV.setText(viewedUser.getEmail());
        phoneTV.setText(viewedUser.getPhone());
        System.out.println("BURASI displayDialog() ici -> "+ viewedUser.toString() + " \n\n\n\n" );

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDB.update(dbHelper, String.valueOf(viewedUser.getId()), emailTV.getText().toString(), phoneTV.getText().toString());
                Toast.makeText(SecondActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
}