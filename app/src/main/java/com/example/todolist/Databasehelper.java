package com.example.todolist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class Databasehelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "todo_list.db";
    public static final String TABLE_NAME = "user_info";
    public static final String USER_TABLE_COLUMN_0 = "USER_ID";
    public static final String USER_TABLE_COLUMN_1 = "NICKNAME";
    public static final String USER_TABLE_COLUMN_2 = "PHONE";
    public static final String USER_TABLE_COLUMN_3 = "EMAIL";
    public static final String USER_TABLE_COLUMN_4 = "CITY";
    public static final String COLUMN_0 = "ID";
    public static final String COLUMN_1 = "LABEL";
    public static final String COLUMN_2 = "DATE";
    public static final String COLUMN_3 = "TIME";
    public static final String COLUMN_4 = "NOTES";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageArraybytes;

    public Databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LABEL INTEGER , DATE TEXT, TIME TEXT, NOTES TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, NICKNAME TEXT , PHONE TEXT , EMAIL TEXT, CITY TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String nickname) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_1, nickname);
        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void CreateUserTable(String nickname) {
        SQLiteDatabase database = this.getWritableDatabase();
        String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
        String USER_PROFILE_TABLE_NAME = TABLE_NAME + "_" + nickname + "_" + "ASSERTS";
        database.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LABEL INTEGER , DATE TEXT, TIME TEXT, NOTES TEXT )");
        String query = "CREATE TABLE " + USER_PROFILE_TABLE_NAME + " (USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, PICTURE BLOB )";
        database.execSQL(query);
    }

    public boolean insertData(String nickname, String label, String date, String time, String notes) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, label);
        contentValues.put(COLUMN_2, date);
        contentValues.put(COLUMN_3, time);
        contentValues.put(COLUMN_4, notes);
        String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
        long result = database.insert(USER_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor viewAllData(String nickname) {
        String query;
        Cursor cursor = null;
        if (!(nickname.equals("logout"))) {
            String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
            query = "SELECT * FROM " + USER_TABLE_NAME;
            SQLiteDatabase database = this.getReadableDatabase();

            if (database != null) {
                cursor = database.rawQuery(query, null);
            }
        }
        return cursor;
    }

    public void updateData(String nickname, String row_id, String label, String date, String time, String notes) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentV = new ContentValues();
        contentV.put(COLUMN_1, label);
        contentV.put(COLUMN_2, date);
        contentV.put(COLUMN_3, time);
        contentV.put(COLUMN_4, notes);

        String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
        long result = database.update(USER_TABLE_NAME, contentV, "ID=?", new String[]{row_id});
        if (result == -1) {
            //Toast.makeText(context, "Failed To Success", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "Update Success ", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteOneRow(String nickname, String id) {
        SQLiteDatabase database = getWritableDatabase();
        String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
        long result = database.delete(USER_TABLE_NAME, "id=?", new String[]{id});
        if (result == -1) {
            //Toast.makeText(context, "Deletion Failed", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "Deletion Completed", Toast.LENGTH_LONG).show();
        }
    }

    public Cursor userChartInfo(String nickname) {
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String USER_TABLE_NAME = TABLE_NAME + "_" + nickname;
        String query = "SELECT LABEL, COUNT(*) FROM " + USER_TABLE_NAME + " GROUP BY label";
        if (!nickname.equals("logout")) {
            if (sqLiteDatabase != null) {
                cursor = sqLiteDatabase.rawQuery(query, null);
            }
        }
        return cursor;
    }

    public boolean insertPhoneNumber(String phone, String email, String city) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_2, phone);
        contentValues.put(USER_TABLE_COLUMN_3, email);
        contentValues.put(USER_TABLE_COLUMN_4, city);
        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertEmailName(String phone, String email, String city) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_2, phone);
        contentValues.put(USER_TABLE_COLUMN_3, email);
        contentValues.put(USER_TABLE_COLUMN_4, city);
        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertCityName(String phone, String email, String city) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_2, phone);
        contentValues.put(USER_TABLE_COLUMN_3, email);
        contentValues.put(USER_TABLE_COLUMN_4, city);
        long result = database.insert(TABLE_NAME, null, contentValues);
        /*  long result = database.update(USER_TABLE_NAME, contentValues,"_NICKNAME="+nickname, null);*/
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor viewProfileData() {
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public void saveProfileImage(String nickname, Bitmap profilePicture) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String USER_PROFILE_TABLE_NAME = TABLE_NAME + "_" + nickname + "_" + "ASSERTS";
        Bitmap imageToStore = profilePicture;
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageArraybytes = byteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put("PICTURE", imageArraybytes);


        long result = sqLiteDatabase.insert(USER_PROFILE_TABLE_NAME, null, contentValues);
        if (result != -1) {
            Toast.makeText(context, "Image Inserted Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Image Insertion failed", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor retriveProfileImages(String nickname) {
        String USER_PROFILE_TABLE_NAME = TABLE_NAME + "_" + nickname + "_" + "ASSERTS";
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT PICTURE FROM " + USER_PROFILE_TABLE_NAME;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;

    }

}
