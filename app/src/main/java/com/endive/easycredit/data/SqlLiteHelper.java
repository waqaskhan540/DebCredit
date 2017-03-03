package com.endive.easycredit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.endive.easycredit.models.RecordItem;

/**
 * Created by MWaqas on 12/31/2016.
 */

public class SqlLiteHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "systemdb";
    public static int DB_VERSION = 2;

    public static String Table_Records = "Records";
    public static String KEY_ID = "Id";
    public static String KEY_DEBIT = "Debit";
    public static String KEY_CREDIT = "Credit";
    public static String KEY_TOTAL = "Total";
    public static String KEY_DESC = "Description";
    public static String KEY_DATE = "Date";


    public static String Create_Table_Records = "CREATE TABLE IF NOT EXISTS " + Table_Records + "(" +
            KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_DEBIT + " REAL,"+KEY_DATE + " TIMESTAMP DEFAULT current_timestamp,"+
            KEY_CREDIT + " REAL," + KEY_TOTAL + " REAL,"+ KEY_DESC + " TEXT)";

    public SqlLiteHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(Create_Table_Records);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Table_Records );

        onCreate(db);
    }


    public List<RecordItem> getAllRecords(){
        List<RecordItem> contactList = new ArrayList<RecordItem>();
        // Select All Query
       // String selectQuery = "SELECT  * FROM " + Table_Records + " ORDER BY datetime("+KEY_DATE + ") DESC";
        String selectQuery = "SELECT  * FROM " + Table_Records + " ORDER BY "+KEY_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

      //  SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecordItem record = new RecordItem();
                 //record.setDate(cursor.get)
                record.setCredit(cursor.getDouble(cursor.getColumnIndex(KEY_CREDIT)));
                record.setDebit(cursor.getDouble(cursor.getColumnIndex(KEY_DEBIT)));
                record.setTotal(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL)));
                record.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESC)));
                record.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                contactList.add(record);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    public List<RecordItem> getTodayRecords(){
        List<RecordItem> contactList = new ArrayList<RecordItem>();
        // Select All Query
      //  String selectQuery = "SELECT  * FROM " + Table_Records + " WHERE date(" + KEY_DATE + ")= DATE('now') ORDER BY datetime("+KEY_DATE+")";
        String selectQuery = "SELECT  * FROM " + Table_Records + " WHERE date(" + KEY_DATE + ")= DATE('now') ORDER BY datetime("+KEY_DATE+")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //  SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecordItem record = new RecordItem();
                //record.setDate(cursor.get)
                record.setCredit(cursor.getDouble(cursor.getColumnIndex(KEY_CREDIT)));
                record.setDebit(cursor.getDouble(cursor.getColumnIndex(KEY_DEBIT)));
                record.setTotal(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL)));
                record.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESC)));
                record.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                contactList.add(record);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteAll() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Delete  from Records");

        db.close();
    }
    public void addRecord(RecordItem record){
        ContentValues cv = new ContentValues();

        cv.put(KEY_CREDIT,record.getCredit());
        cv.put(KEY_DEBIT,record.getDebit());
        cv.put(KEY_TOTAL,record.getTotal());
        cv.put(KEY_DESC,record.getDescription());
        cv.put(KEY_DATE,record.getDate());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(Table_Records,null,cv);
        db.close();
    }

    public double getCurrentBalance(){

    SQLiteDatabase db = this.getWritableDatabase();
        String[] allColumns = {KEY_ID,KEY_CREDIT,KEY_DEBIT,KEY_DESC,KEY_TOTAL,KEY_DATE};

        Cursor cursor = db.query(Table_Records, allColumns, null, null, null, null, KEY_ID +" DESC", "1");


      if(cursor != null){

          if(cursor.getCount() < 1)
              return 0;

          double balance = 0;
          if(cursor.moveToFirst())
            balance = Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TOTAL)));

          if(balance > 0)
              return balance;

      }

        return 0;
    }
}
