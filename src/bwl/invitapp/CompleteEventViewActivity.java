package bwl.invitapp;

import bwl.invitapp.R;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class CompleteEventViewActivity extends Activity {
	
	DatabaseOpenHelper mDbHelper;
	Bundle bundle;
	CheckBox eventChecked;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_event_view);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		
		bundle = getIntent().getExtras();
		
		TextView eventName = (TextView) findViewById(R.id.event_name);
		TextView eventDescription = (TextView) findViewById(R.id.event_description);
		TextView eventDatetime = (TextView) findViewById(R.id.event_datetime);
		eventChecked = (CheckBox) findViewById(R.id.my_attendance);
		TextView eventAttendance = (TextView) findViewById(R.id.event_attendance);
		TextView eventAttendant = (TextView) findViewById(R.id.event_attendant);
		
		eventName.setText(bundle.getString(DatabaseOpenHelper.EVENT_NAME));
		eventDescription.setText(bundle.getString(DatabaseOpenHelper.EVENT_DESCRIPTION));
		eventDatetime.setText(bundle.getString(DatabaseOpenHelper.EVENT_DATETIME));
		eventChecked.setChecked(bundle.getBoolean(DatabaseOpenHelper.EVENT_CHECKED));
		eventAttendance.setText(bundle.getInt(DatabaseOpenHelper.EVENT_ATTENDANCE)
				      + "/" + bundle.getInt(DatabaseOpenHelper.EVENT_TOTAL_GUESTS));
		eventAttendant.setText(bundle.getString(DatabaseOpenHelper.USER_NAME));
		
	}
	
	@Override
	public void onPause(){
		if(bundle.getBoolean(DatabaseOpenHelper.EVENT_CHECKED) != eventChecked.isChecked()){
			ContentValues values = new ContentValues();
			values.put(DatabaseOpenHelper.EVENT_ID, bundle.getString(DatabaseOpenHelper.EVENT_ID));
			values.put(DatabaseOpenHelper.EVENT_NAME, bundle.getString(DatabaseOpenHelper.EVENT_NAME));
			values.put(DatabaseOpenHelper.EVENT_DESCRIPTION, bundle.getString(DatabaseOpenHelper.EVENT_DESCRIPTION));
			values.put(DatabaseOpenHelper.EVENT_DATETIME, bundle.getString(DatabaseOpenHelper.EVENT_DATETIME));
			values.put(DatabaseOpenHelper.EVENT_CHECKED, eventChecked.isChecked());
			if (eventChecked.isChecked()){
				values.put(DatabaseOpenHelper.EVENT_ATTENDANCE, bundle.getInt(DatabaseOpenHelper.EVENT_ATTENDANCE)+1);
			}else{
				values.put(DatabaseOpenHelper.EVENT_ATTENDANCE, bundle.getInt(DatabaseOpenHelper.EVENT_ATTENDANCE)-1);
			}
			values.put(DatabaseOpenHelper.EVENT_TOTAL_GUESTS, bundle.getInt(DatabaseOpenHelper.EVENT_TOTAL_GUESTS));
			values.put(DatabaseOpenHelper.EVENT_CREATOR_USER, bundle.getString(DatabaseOpenHelper.EVENT_CREATOR_USER));
			
			mDbHelper.updateRecord(DatabaseOpenHelper.TABLE_EVENT, values, DatabaseOpenHelper.EVENT_ID + "=" + bundle.getString(DatabaseOpenHelper.EVENT_ID), null);
		}
		super.onPause();
	}
}
