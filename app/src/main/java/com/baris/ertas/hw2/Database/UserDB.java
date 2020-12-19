package com.baris.ertas.hw2.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.baris.ertas.hw2.model.User;

import java.util.ArrayList;

public class UserDB {
    public static String TABLE_NAME="user";
    public static String FIELD_ID = "id";
    public static String FIELD_NAME = "name";
    public static String FIELD_EMAIL = "email";
    public static String FIELD_CITY = "city";
    public static String FIELD_PHONE = "phone";
    public static String FIELD_COMPANY = "company";


    public static String CREATE_TABLE_SQL="CREATE TABLE user ( "+FIELD_ID+" INTEGER, "+FIELD_NAME+" TEXT, "+FIELD_EMAIL+" TEXT,"+FIELD_CITY+" TEXT, " +FIELD_PHONE +" TEXT,"+FIELD_COMPANY+" TEXT, PRIMARY KEY("+FIELD_ID+" AUTOINCREMENT))";


    public static String DROP_TABLE_SQL = "DROP TABLE if exists "+TABLE_NAME;

    public static ArrayList<User> getAllUsers(DatabaseHelper dbHelper){
        User anItem;
        ArrayList<User> data = new ArrayList<>();
        Cursor cursor = dbHelper.getAllRecords(TABLE_NAME, null);
        Log.d("DATABASE OPERATIONS", cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name = cursor.getString(1);
            String email= cursor.getString(2);
            String city= cursor.getString(3);
            String phone= cursor.getString(4);
            String company= cursor.getString(5);

            anItem = new User(id,name, email, phone, company, city);
            data.add(anItem);

        }
        Log.d("DATABASE OPERATIONS",data.toString());
        return data;
    }

    public static ArrayList<User> findUser(DatabaseHelper dbHelper, String key) {
        User anItem;
        ArrayList<User> data = new ArrayList<>();
        String where = FIELD_ID +" like '%"+key+"%'";

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        Log.d("DATABASE OPERATIONS",  where+", "+cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name = cursor.getString(1);
            String email= cursor.getString(2);
            String city= cursor.getString(3);
            String phone= cursor.getString(4);
            String company= cursor.getString(5);;

            anItem = new User(id,name, email, city, phone, company);
            data.add(anItem);
        }
        Log.d("DATABASE OPERATIONS",data.toString());
        return data;
    }

    public static boolean insert(DatabaseHelper dbHelper, int id, String name, String email, String city,String phone, String company) {
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_EMAIL, email);
        contentValues.put(FIELD_CITY, city);
        contentValues.put(FIELD_PHONE, phone);
        contentValues.put(FIELD_COMPANY, company);
        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }

    public static boolean update(DatabaseHelper dbHelper, String id, String email,String phone) {
        //ContentValues  allows to define key value pairs.
        //The key represents the table column identifier and the value represents the content for the table record in this column.
        //ContentVales can be used for insert and update operations over table

        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_EMAIL, email);
        contentValues.put(FIELD_PHONE, phone);

        String where = FIELD_ID +" = "+id;
        boolean res = dbHelper.update(TABLE_NAME,contentValues,where );
        return res;
    }

    public static boolean delete(DatabaseHelper dbHelper, String id){
        Log.d("DATABASE OPERATIONS", "DELETE DONE");
        String where = FIELD_ID + " = "+id;
        boolean res =  dbHelper.delete(TABLE_NAME, where);
        return  res;
    }
}

