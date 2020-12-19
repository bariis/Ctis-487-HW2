package com.baris.ertas.hw2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baris.ertas.hw2.Database.DatabaseHelper;
import com.baris.ertas.hw2.Database.UserDB;
import com.baris.ertas.hw2.RecyclerView.MyRecyclerViewAdapter;
import com.baris.ertas.hw2.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static DatabaseHelper dbHelper;
    Spinner userSpinner;
    Dialog customDialog;
    RecyclerView recyclerViewUsers;
    private RecyclerView.LayoutManager mLayoutManager;
    //private MyRecyclerViewAdapter mRecyclerAdapter;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private boolean isDataLoaded = false;
    ArrayList<User> userList;
    private String jsonStr;
    private JSONArray users;
    CustomSpinnerAdapter spinnerAdapter;
    private JSONObject userJSONObject;
    String key="";
    private ArrayList<User> mArrayList;
    private boolean isSpinnerTriggered = false;

    public static final String TAG_USERS = "users";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_CITY = "city";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_COMPANY = "company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        userSpinner = findViewById(R.id.userSpinner);
        recyclerViewUsers = findViewById(R.id.recyclerViewCars);
        mArrayList = new ArrayList<User>();

        jsonStr = loadFileFromAssets("users.json");

        if(!isDataLoaded) {
            new GetUsersFromJsonFile().execute();
            isDataLoaded = true;
        }

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!isSpinnerTriggered) {
                    isSpinnerTriggered = true;
                    return;
                }
                if(isSpinnerTriggered) {
                    System.out.println("POZISYON BURASI ->>>>" + position);
                    User selectedUser = userList.get(position);
                    carryData(selectedUser, position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Copy database from the assets/ folder to the data/data/databases/ folder. İf ıt exist, there is no need to copy it
       try {
            String fileToDatabase = "/data/data/" + getPackageName() + "/databases/"+DatabaseHelper.DATABASE_NAME;
            File file = new File(fileToDatabase);
            File pathToDatabasesFolder = new File("/data/data/" + getPackageName() + "/databases/");
            if (!file.exists()) {
                pathToDatabasesFolder.mkdirs();
                Log.d("BURDA", "BURDA");
                CopyDB( getResources().getAssets().open(DatabaseHelper.DATABASE_NAME+".db"),
                        new FileOutputStream(fileToDatabase));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //dbHelper = new DatabaseHelper(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewUsers.setLayoutManager(layoutManager);
    }


    private class GetUsersFromJsonFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Creating and showing the progress Dialog
           // mProgressBar.setVisibility(View.VISIBLE);
        }

        // Main job should be done here
        @Override
        protected Void doInBackground(Void... params) {
            //Log.d("TAG", "HERE.....");

            if (jsonStr != null) {
                try {
                    userJSONObject = new JSONObject(jsonStr);
                    // Getting JSON Array
                    users = userJSONObject.getJSONArray(TAG_USERS);


                    // looping through all books
                    for (int i = 0; i < users.length(); i++) {

                        JSONObject jsonObj = users.getJSONObject(i);

                        Thread.sleep(2000);//This is here only to simulate parsing json takes time so that ProgressBar execution can be displayed better
                        int id = jsonObj.getInt(TAG_ID);
                        String name = jsonObj.getString(TAG_NAME);
                        String email = jsonObj.getString(TAG_EMAIL);
                        String city = jsonObj.getString(TAG_CITY);
                        String phone = jsonObj.getString(TAG_PHONE);
                        String company = jsonObj.getString(TAG_COMPANY);

                        User user = new User(id, name, email, city, phone, company);
                        System.out.println(user.toString());

                        boolean isAdded = UserDB.insert(dbHelper, id, name, email, city, phone, company);

                       if(isAdded) {
                           System.out.println("kayit basariyla eklendi");
                        }

                        Log.d("KEY", key+" "+name);
                      /*  if(key.isEmpty())
                            mArrayList.add(user);
                        else if(name.toLowerCase().contains(key.toLowerCase()))
                            mArrayList.add(user); */
                    }
                } catch (JSONException ee) {
                    ee.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        // What do you want to do after doInBackground() finishes
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            //mProgressBar.setVisibility(View.INVISIBLE);
            System.out.println("BURASI onPostExecute() ici");
                userList = UserDB.getAllUsers(dbHelper);
                spinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, userList);
                userSpinner.setAdapter(spinnerAdapter);
                recyclerViewAdapter = new MyRecyclerViewAdapter(MainActivity.this, userList);
                recyclerViewUsers.setAdapter(recyclerViewAdapter);
        }

    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        Log.d("BURDA", "BURDA2");

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
            Log.d("BURDA", "BURDA3");
        }
        inputStream.close();
        outputStream.close();
    }

    private String loadFileFromAssets(String fileName) {
        String fileContent = null;
        try {
            InputStream is = getBaseContext().getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            fileContent = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return fileContent;
    }

    public void carryData(User selectedUser, int position) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("viewedContact", selectedUser);
        b.putInt("id", position);
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("superin alti");
        if (resultCode == RESULT_OK) {
            System.out.println("on activity ici, IF'IN ICI");
            //String deletedUserId = data.getStringExtra("deletedUser");
            Intent intent = data;
            int deletedUserId = intent.getIntExtra("deletedUserId", 0);
            System.out.println("deletedUserId ==== " + deletedUserId);
            //int id = Integer.parseInt(deletedUserId);
            System.out.println("parseladiktan sonra " + deletedUserId);
         //   recyclerViewAdapter.refreshMyAdapterAfterDelete(deletedUserId);
       //     spinnerAdapter.refreshSpinnerAfterDeletion(deletedUserId-1);
        }
    }

    public void refreshButton(View view) {
        userList = UserDB.getAllUsers(dbHelper);
        spinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, userList);
        userSpinner.setAdapter(spinnerAdapter);
        recyclerViewAdapter = new MyRecyclerViewAdapter(MainActivity.this, userList);
        recyclerViewUsers.setAdapter(recyclerViewAdapter);
    }

}