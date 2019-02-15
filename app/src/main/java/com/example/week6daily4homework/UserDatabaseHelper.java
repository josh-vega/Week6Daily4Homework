package com.example.week6daily4homework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user_login_table";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";

    public UserDatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + FIELD_USERNAME + " TEXT PRIMARY KEY, "
                + FIELD_PASSWORD + " TEXT);";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public ArrayList<User> getAllUsers(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        if(cursor.moveToFirst()){
            ArrayList<User> arrayList = new ArrayList<>();
            do {
                String username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(FIELD_PASSWORD));
                arrayList.add(new User(username, password));
            } while(cursor.moveToNext());
            return arrayList;
        } else {
            return null;
        }
    }

    public User getSingleUserByName(String passedName){
        User returnUser = null;
        if(passedName != null && !passedName.isEmpty()) {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + FIELD_USERNAME + " = \"" + passedName + "\"";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndex(FIELD_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(FIELD_PASSWORD));
                returnUser = new User(username, password);
            }
            cursor.close();
        }
        return returnUser;
    }

    public void insertNewUser(User passedUser){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if(passedUser != null){
            String username = passedUser.getUsername();
            String password = passedUser.getPassword();

            contentValues.put(FIELD_USERNAME, username);
            contentValues.put(FIELD_PASSWORD, password);

            database.insert(TABLE_NAME, null, contentValues);
        }
    }

    public int deleteUser(String passedName){
        String whereClause = FIELD_USERNAME + " = \"" + passedName + "\"";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, whereClause, null);
    }

    public int updateUser(User passedUser){
        if(passedUser != null) {
            String whereClause = FIELD_USERNAME + " = \"" + passedUser.getUsername() + "\"";
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FIELD_USERNAME, passedUser.getUsername());
            contentValues.put(FIELD_PASSWORD, passedUser.getPassword());
            return sqLiteDatabase.update(TABLE_NAME, contentValues, whereClause, null);
        }
        return 0;
    }
}
