package bwl.invitapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final private Context mContext;
	
	final static int DATABASE_VERSION = 1;
	final static String DATABASE_NAME="eventsManager";
	
	final static String TABLE_USER= "users";
	final static String TABLE_EVENT= "events";
	
	final static String USER_ID = "userID";
	final static String USER_NAME = "userName";
	final static String[] user_columns = { USER_ID, USER_NAME };
	
	final static String EVENT_ID = "eventID";
	final static String EVENT_NAME = "eventName";
	final static String EVENT_DESCRIPTION = "description";
	final static String EVENT_DATETIME = "datetime";
	final static String EVENT_CHECKED = "checked";
	final static String EVENT_ATTENDANCE = "attendance";
	final static String EVENT_TOTAL_GUESTS = "totalGuests";
	final static String EVENT_CREATOR_USER = "creatorUser";
	final static String[] event_columns = { EVENT_ID, EVENT_NAME, EVENT_DESCRIPTION,
											EVENT_DATETIME, EVENT_CHECKED, EVENT_ATTENDANCE,
											EVENT_TOTAL_GUESTS, EVENT_CREATOR_USER };
	
	
	
	final static String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + USER_ID + " TEXT PRIMARY KEY," + USER_NAME
            + " TEXT" + ")";
	
	final static String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_EVENT + "(" + EVENT_ID + " TEXT PRIMARY KEY," + EVENT_NAME
            + " TEXT," + EVENT_DESCRIPTION + " TEXT," + EVENT_DATETIME + " DATETIME,"
            + EVENT_CHECKED + " INTEGER,"  + EVENT_ATTENDANCE + " INTEGER," 
            + EVENT_TOTAL_GUESTS + " INTEGER," + EVENT_CREATOR_USER + " TEXT" + ")";
	
	
	//---------------------------------------QUERYS--------------------------------------//
	final static String GET_ALL_EVENTS_QUERY = "SELECT * FROM " + TABLE_EVENT + " te, "
			+ TABLE_USER + " tu " + "WHERE tu." + USER_ID + " = te." + EVENT_CREATOR_USER;

	
	
	public DatabaseOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_EVENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
 
        onCreate(db);

	}

	public ArrayList<EventRecord> getAllEvents(){
		ArrayList<EventRecord> list = new ArrayList<EventRecord>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(GET_ALL_EVENTS_QUERY, null);
		
		while(cursor.moveToNext()){
			EventRecord er = new EventRecord(cursor.getString(cursor.getColumnIndex(EVENT_ID)),
					cursor.getString(cursor.getColumnIndex(EVENT_NAME)),
					cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)),
					cursor.getString(cursor.getColumnIndex(EVENT_DATETIME)),
					cursor.getInt(cursor.getColumnIndex(EVENT_CHECKED)),
					cursor.getInt(cursor.getColumnIndex(EVENT_ATTENDANCE)),
					cursor.getInt(cursor.getColumnIndex(EVENT_TOTAL_GUESTS)),
					cursor.getString(cursor.getColumnIndex(EVENT_CREATOR_USER)),
					cursor.getString(cursor.getColumnIndex(USER_NAME)));
			list.add(er);
		}
		db.close();
		return list;
	}
	
	public void insertRecord(String TABLE_NAME, String nullColumnHack, ContentValues values){
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(TABLE_NAME, nullColumnHack, values);
		db.close();
		
	}
	
	public void updateRecord(String table, ContentValues values, String whereClause, String[] whereArgs){
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(table, values, whereClause, whereArgs);
		db.close();
		
	}
	
	public void delete(String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table, whereClause, whereArgs);
		db.close();
		
	}
	
	void deleteDatabase() {
		mContext.deleteDatabase(DATABASE_NAME);
	}
}
