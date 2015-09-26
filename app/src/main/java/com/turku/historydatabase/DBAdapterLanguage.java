package com.turku.historydatabase;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterLanguage {

	public static final boolean DEBUG = true;
	public static final String KEY_ID = "_id";
	public static final int DATABASE_VERSION = 1;
	public static final String KEY_NAME = "_name";
	public static final String LOG_TAG = "DBAdapter";
	public static final String ADDRESS_TABLE = "tbl_language";
	private static final String[] ALL_TABLES = { ADDRESS_TABLE };
	public static final String DATABASE_NAME = "DB_sqllite_language";
	private static final String AADDRESS_CREATE = "create table tbl_language(_id integer primary key autoincrement, _name text not null);";


	private static DataBaseHelper DBHelper = null;

	protected DBAdapterLanguage() {}

/*********************************************************************************************************************
 * 						Initialize database.																		 *
*********************************************************************************************************************/	
	public static void init(Context context) {
		if (DBHelper == null) {
			if (DEBUG) Log.i("DBAdapter", context.toString());
			DBHelper = new DataBaseHelper(context);
		}
	}

/*********************************************************************************************************************
 * 						Main Database creation INNER class.															 *
*********************************************************************************************************************/
	private static class DataBaseHelper extends SQLiteOpenHelper {
		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (DEBUG) Log.i(LOG_TAG, "new create");
			try { db.execSQL(AADDRESS_CREATE);} 
			catch (Exception exception) {if (DEBUG)Log.i(LOG_TAG, "Exception onCreate() exception");}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (DEBUG) Log.w(LOG_TAG, "Upgrading database from version" + oldVersion + "to" + newVersion + "...");
			for (String table : ALL_TABLES) {
				db.execSQL("DROP TABLE IF EXISTS " + table);
			}
			onCreate(db);
		}
	}

/*********************************************************************************************************************
 * 						Open database for insert,update,delete in syncronized manner.								 *
*********************************************************************************************************************/	
	private static synchronized SQLiteDatabase open() throws SQLException {
			return DBHelper.getWritableDatabase();
		}

/*********************************************************************************************************************
 * 						General functions.																			 *
 * 						Escape string for single quotes (Insert,Update).											 *
*********************************************************************************************************************/	
	private static String sqlEscapeString(String aString) {
		String aReturn = "";
		if (null != aString) {
			aReturn = DatabaseUtils.sqlEscapeString(aString);
			aReturn = aReturn.substring(1, aReturn.length() - 1);
		}
		return aReturn;
	}
	public static void addAddress(Language language) {
		final SQLiteDatabase db = open();
		String name = sqlEscapeString(language.get_name());
		ContentValues cVal = new ContentValues();
		cVal.put(KEY_NAME, name);
		db.insert(ADDRESS_TABLE, null, cVal);
		db.close();
	}	

	public static Address getUserData(int id) {
		final SQLiteDatabase db = open();
		Cursor cursor = db.query(ADDRESS_TABLE, new String[] { KEY_ID, KEY_NAME}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		Address data = new Address(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		cursor.close();
		db.close();
		return data;
	}

	public static List<String> getAllData() {
		List<String> addresslist = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + ADDRESS_TABLE;
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				addresslist.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
//		cursor.close();
		//db.close();
		return addresslist;
	}

	public static void updateAddressData(Language language) {
		final SQLiteDatabase db = open();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, language.get_name());
		db.update(ADDRESS_TABLE, values, KEY_ID + " = 1", null);
		//db.close();
	}

	public static void deleteAddressData(Address data) {
		final SQLiteDatabase db = open();
		db.delete(ADDRESS_TABLE, KEY_NAME + " = ?", new String[] { String.valueOf(data.get_name()) });
		//db.close();
	}

	public static int checkEmpty() {
		String countQuery = "SELECT count(*) FROM " + ADDRESS_TABLE;
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(countQuery, null);
		int empty = cursor.getInt(0);
		cursor.close();
		//db.close();
		return empty;
	}
	public static int dataExist(String name) {
		String countQuery = "SELECT * FROM " + ADDRESS_TABLE +" WHERE _name = '"+ name+"'";
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		//db.close();
		return count;
	}

	public static int exist(String  name) {
		final SQLiteDatabase db = open();
		Cursor cursor = db.query(ADDRESS_TABLE, new String[] { KEY_NAME, KEY_NAME}, KEY_NAME + "=?", new String[] { name }, null, null, null, null);
		int val = cursor.getCount();
		cursor.close();
		//db.close();
		return val;
	}
}
