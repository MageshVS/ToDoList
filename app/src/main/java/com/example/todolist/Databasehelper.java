package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Databasehelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "todo_list.db";
    public static final String TABLE_NAME = "user_info";
    public static final String COLUMN_0 = "ID";
    public static final String COLUMN_1 = "LABEL";
    public static final String COLUMN_2 = "DATE";
    public static final String COLUMN_3 = "TIME";
    public static final String COLUMN_4 = "NOTES";

    public Databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LABEL INTEGER , DATE TEXT, TIME TEXT, NOTES TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public  boolean insertData( String label, String date, String time, String notes){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1,label);
        contentValues.put(COLUMN_2,date);
        contentValues.put(COLUMN_3,time);
        contentValues.put(COLUMN_4,notes);
        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor viewAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String label, String date, String time, String notes){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentV = new ContentValues();
        contentV.put(COLUMN_1, label);
        contentV.put(COLUMN_2, date);
        contentV.put(COLUMN_3, time);
        contentV.put(COLUMN_4, notes);

        long result = database.update(TABLE_NAME, contentV,"ID=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed To Success", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Update Success ", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteOneRow(String id){
        SQLiteDatabase database = getWritableDatabase();
        long result = database.delete(TABLE_NAME,"id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Deletion Failed", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Deletion Completed", Toast.LENGTH_LONG).show();
        }
    }
}
