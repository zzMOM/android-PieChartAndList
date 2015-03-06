package com.example.piechartandlist;

import java.util.ArrayList;
import java.util.List;

import com.example.piechartandlist.PLContract.ListEntry;
import com.example.piechartandlist.PLContract.PieEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PLDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "pielist.db";

    public PLDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_PIE_TABLE = "CREATE TABLE " + PieEntry.TABLE_NAME + " (" +
        		PieEntry._ID + " INTEGER PRIMARY KEY," +
        		PieEntry.COLUMN_GREEN + " TEXT NOT NULL, " +
        		PieEntry.COLUMN_YELLOW + " TEXT NOT NULL, " +
        		PieEntry.COLUMN_RED + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_LIST_TABLE = "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                ListEntry.COLUMN_NAME + " INTEGER NOT NULL, " +
                ListEntry.COLUMN_DISPLAY_NAME + " TEXT NOT NULL, " +
                ListEntry.COLUMN_FIELD1 + " TEXT NOT NULL, " +
                ListEntry.COLUMN_FIELD2 + " TEXT NOT NULL, " +
                ListEntry.COLUMN_FIELD3 + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_PIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    
    public void addColorInfo(String[] colorResult){
    	Log.e("addcolorinfo", colorResult[0] + " " + colorResult[1] + " " + colorResult[2]);
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	ContentValues value = new ContentValues();
    	value.put(PieEntry.COLUMN_GREEN, colorResult[0]);
    	value.put(PieEntry.COLUMN_YELLOW, colorResult[1]);
    	value.put(PieEntry.COLUMN_RED, colorResult[2]);
    	
    	db.insert(PieEntry.TABLE_NAME, null, value);
    	db.close();
    }
    
    public void addListInfo(List<String> result){
    	SQLiteDatabase db = this.getReadableDatabase();
    	ContentValues value = new ContentValues();
    	value.put(ListEntry.COLUMN_NAME, result.get(0));
    	value.put(ListEntry.COLUMN_DISPLAY_NAME, result.get(1));
    	value.put(ListEntry.COLUMN_FIELD1, result.get(2));
    	value.put(ListEntry.COLUMN_FIELD2, result.get(3));
    	value.put(ListEntry.COLUMN_FIELD3, result.get(4));
    	
    	db.insert(ListEntry.TABLE_NAME, null, value);
    	db.close();
    }
    
    public void deleteAllPie(){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(PieEntry.TABLE_NAME, null, null);
    	db.close();
    }
    
    public void deleteAllList(){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(ListEntry.TABLE_NAME, null, null);
    	db.close();
    }
    
    public void deletListItemByName(String name){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(ListEntry.TABLE_NAME, 
    			ListEntry.COLUMN_NAME+"=?", 
    			new String[] { name });
    	db.close();
    }
    
    public String[] getPieTableInfo(){
    	String[] result = new String[3];
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.query(PieEntry.TABLE_NAME, 
    			null, null, null, null, null, null);
    	if(cursor.moveToFirst()){
    		result[0] = cursor.getString(1);
    		result[1] = cursor.getString(2);
    		result[2] = cursor.getString(3);
    	} else {
    		db.close();
    		return null;
    	}
    	
    	db.close();
    	Log.e("getPieTableInfo", result[0] + " " + result[1] + " " + result[2]);
    	return result;
    }
    
    public List<List<String>> getListTableInfo(){
    	List<List<String>> result = new ArrayList<List<String>>();
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.query(ListEntry.TABLE_NAME, 
    			null, null, null, null, null, null);
    	if(cursor.moveToFirst()){
    		do{
    			List<String> l = new ArrayList<String>();
    			l.add(cursor.getString(1));
    			l.add(cursor.getString(2));
    			l.add(cursor.getString(3));
    			l.add(cursor.getString(4));
    			l.add(cursor.getString(5));
    			result.add(l);
    		}while(cursor.moveToNext());
    	} else {
    		db.close();
    		return null;
    	}
    	
    	db.close();
    	return result;
    }

}
