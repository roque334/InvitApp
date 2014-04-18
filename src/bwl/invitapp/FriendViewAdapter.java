package bwl.invitapp;

import java.util.ArrayList;

import bwl.invitapp.R;

import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.model.people.PersonBuffer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendViewAdapter extends BaseAdapter {
	
	private ArrayList<FriendRecord> list = new ArrayList<FriendRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	public FriendViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
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
		View newView = convertView;
		ViewHolder holder;
		
		FriendRecord curr = list.get(position);
		
		if(convertView == null){
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.friend_view, null);
			holder.name = (TextView) newView.findViewById(R.id.friend_name_textview);
			newView.setTag(holder);
		}else{
			holder = (ViewHolder) newView.getTag();
		}
		
		holder.name.setText(curr.getName());
		
		return newView;
	}
	
	static class ViewHolder {
		
		TextView name;
		
	}
	
	public ArrayList<FriendRecord> getList(){
		return list;
	}
	
	public void add(FriendRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public void add(LoadPeopleResult peopleData, Context mcontext){
		list.clear();
		PersonBuffer personBuffer = peopleData.getPersonBuffer();
	    try {
	      int count = personBuffer.getCount();
	      for (int i = 0; i < count; i++) {
	    	FriendRecord fr = new FriendRecord(personBuffer.get(i).getId(), personBuffer.get(i).getDisplayName());
	    	list.add(fr);
	      }
	    } finally {
	      personBuffer.close();
	      notifyDataSetChanged();
	    }
	}
}
