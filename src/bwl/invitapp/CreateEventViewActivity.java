package bwl.invitapp;

import java.util.Calendar;

import bwl.invitapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEventViewActivity extends Activity {
	
	EditText eventName;
	DatePicker eventDate;
	TimePicker eventTime;
	EditText eventDescription;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event_view);
		
		eventName = (EditText) findViewById(R.id.event_name_edit);
		eventDate = (DatePicker) findViewById(R.id.datePicker);
		eventTime = (TimePicker) findViewById(R.id.timePicker);
		eventDescription = (EditText) findViewById(R.id.event_description_edit);
		
		eventTime.setIs24HourView(true);
		eventTime.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		
		Button bt = (Button) findViewById(R.id.next_button);
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (eventName.getText().length()!=0
						&& eventDescription.getText().length()!=0){
					
					String datetime = eventDate.getYear() 
							+ "-" + eventDate.getMonth()
							+ "-" + eventDate.getDayOfMonth()
							+ " " + eventTime.getCurrentHour()
							+ ":" + eventTime.getCurrentMinute()
							+ ":00";
					
					Intent intent = new Intent(getApplicationContext(), FriendViewActivity.class);
					intent.putExtra(DatabaseOpenHelper.EVENT_NAME, eventName.getText().toString());
					intent.putExtra(DatabaseOpenHelper.EVENT_DATETIME, datetime);
					intent.putExtra(DatabaseOpenHelper.EVENT_DESCRIPTION, eventDescription.getText().toString());
					
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), 
							"Debes llenar todos los campos para poder continuar", 
							Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}

}
