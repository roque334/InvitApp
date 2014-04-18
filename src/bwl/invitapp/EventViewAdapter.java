package bwl.invitapp;

import java.util.ArrayList;

import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.model.people.PersonBuffer;

import bwl.invitapp.FriendViewAdapter.ViewHolder;
import bwl.invitapp.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewAdapter extends BaseAdapter {
	
	ArrayList<EventRecord> list = new ArrayList<EventRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	public EventViewAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int attendance_percent;
		View newView = convertView;
		ViewHolder holder;
		
		EventRecord curr = list.get(position);
		
		if(convertView == null){
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.event_view, null);
			holder.event_name = (TextView) newView.findViewById(R.id.event_name);
			holder.event_date = (TextView) newView.findViewById(R.id.event_date);
			holder.my_attendance = (CheckBox) newView.findViewById(R.id.my_attendance_confirmation_check);
			holder.attendance_icon = (ImageView) newView.findViewById(R.id.attendance_icon);
			newView.setTag(holder);
		}else{
			holder = (ViewHolder) newView.getTag();
		}
		
		holder.event_name.setText(curr.getName());
		holder.event_date.setText(curr.getDatetime());
		
		if(curr.isChecked()){
			holder.my_attendance.setChecked(true);
		}else{
			holder.my_attendance.setChecked(false);
		}
		
		attendance_percent = (int) (((float) curr.getAttendance()/ (float)curr.getTotalGuests())*100);
		if(attendance_percent >= 70 && attendance_percent <= 100){
			holder.attendance_icon.setImageResource(R.drawable.green_ball_icon);
		}else if(attendance_percent >= 40 && attendance_percent < 70){
			holder.attendance_icon.setImageResource(R.drawable.yellow_ball_icon);
		}else{
			holder.attendance_icon.setImageResource(R.drawable.red_ball_icon);
		}
		
		return newView;
	}
	
	static class ViewHolder {
		ImageView attendance_icon;
		TextView event_name;
		TextView event_date;
		CheckBox my_attendance;
		
	}
	
	public ArrayList<EventRecord> getList(){
		return list;
	}
	
	public void add(EventRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public void add(ArrayList<EventRecord> events, Context mcontext){
		list = events;
		notifyDataSetChanged();
	}
	
//	public void add(Cursor cursor, Context mcontext){
//		list.clear();
//		while (cursor.moveToNext()){
//			EventRecord er = new EventRecord(cursor.getString(cursor.getColumnIndex("name")),
//						cursor.getString(cursor.getColumnIndex("description")),
//						cursor.getString(cursor.getColumnIndex("date")),
//						cursor.getString(cursor.getColumnIndex("time")),
//						cursor.getInt(cursor.getColumnIndex("checked")),
//						cursor.getInt(cursor.getColumnIndex("attendance")),
//						cursor.getInt(cursor.getColumnIndex("totalGuests")));
//			list.add(er);
//		}
//		cursor.close();
//		notifyDataSetChanged();
//	}

}
