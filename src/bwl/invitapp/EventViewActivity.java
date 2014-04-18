package bwl.invitapp;

import java.util.ArrayList;

import bwl.invitapp.R;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventViewActivity extends ListActivity {
	
	private String TAG = "Debug";
	
	private DatabaseOpenHelper mDbHelper;
	
	private EventViewAdapter mAdapter;
	
	private TextView mHeaderView;
	
	ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		
		clearAll();
		
		insetEvent();
		
		ArrayList<EventRecord> events = mDbHelper.getAllEvents();
		
		mAdapter = new EventViewAdapter(getApplicationContext());
		
		mAdapter.add(events, getApplicationContext());
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mHeaderView = (TextView) inflater.inflate(R.layout.event_header_view, null);
		
		ListView mListView = getListView();
		mListView.addHeaderView(mHeaderView);
		
		mHeaderView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Create New Event");
				Intent intent = new Intent(getApplicationContext(), CreateEventViewActivity.class);
				startActivity(intent);
			}
		});
		
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ArrayList<EventRecord> events = mDbHelper.getAllEvents();
		
		mAdapter.add(events, getApplicationContext());
	}

	private void insetEvent() {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.EVENT_ID, "1");
		values.put(DatabaseOpenHelper.EVENT_NAME, "Cumpleaños");
		values.put(DatabaseOpenHelper.EVENT_DESCRIPTION, "Reunión en mi casa");
		values.put(DatabaseOpenHelper.EVENT_DATETIME, "2014-11-13 12:00:00");
		values.put(DatabaseOpenHelper.EVENT_CHECKED, 0);
		values.put(DatabaseOpenHelper.EVENT_ATTENDANCE, 6);
		values.put(DatabaseOpenHelper.EVENT_TOTAL_GUESTS, 12);
		values.put(DatabaseOpenHelper.EVENT_CREATOR_USER, "116946458399888840050");
		mDbHelper.insertRecord(DatabaseOpenHelper.TABLE_EVENT, null, values);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		EventRecord er = (EventRecord)l.getItemAtPosition(position);
		
		Intent intent = new Intent(getApplicationContext(), CompleteEventViewActivity.class);
		intent.putExtra(DatabaseOpenHelper.EVENT_ID, er.getId());
		intent.putExtra(DatabaseOpenHelper.EVENT_NAME, er.getName());
		intent.putExtra(DatabaseOpenHelper.EVENT_DESCRIPTION, er.getDescription());
		intent.putExtra(DatabaseOpenHelper.EVENT_DATETIME, er.getDatetime());
		intent.putExtra(DatabaseOpenHelper.EVENT_CHECKED, er.isChecked());
		intent.putExtra(DatabaseOpenHelper.EVENT_ATTENDANCE, er.getAttendance());
		intent.putExtra(DatabaseOpenHelper.EVENT_TOTAL_GUESTS, er.getTotalGuests());
		intent.putExtra(DatabaseOpenHelper.EVENT_CREATOR_USER, er.getCreatorUserID());
		intent.putExtra(DatabaseOpenHelper.USER_NAME, er.getCreatorUserName());
		
		startActivity(intent);
		
	}
	
	// Delete all records
		private void clearAll() {

			mDbHelper.delete(DatabaseOpenHelper.TABLE_EVENT, null, null);

		}

		// Close database
		@Override
		protected void onDestroy() {
			
			mDbHelper.deleteDatabase();

			super.onDestroy();

		}

}
