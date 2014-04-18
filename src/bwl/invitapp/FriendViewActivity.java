package bwl.invitapp;

import bwl.invitapp.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.plus.Plus;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendViewActivity extends ListActivity implements ConnectionCallbacks,
OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>{
	
	private String TAG = "Debug"; 
	
	private FriendViewAdapter mAdapter;
	
	private TextView mFooterView;
	
	GoogleApiClient mGoogleApiClient;
	
	ListView mListView;
	
	Bundle extras;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
		   .addConnectionCallbacks(this)
		   .addOnConnectionFailedListener(this)
	       .addApi(Plus.API, null)
	       .addScope(Plus.SCOPE_PLUS_LOGIN)
	       .build();
		
		extras = new Bundle();
		extras = getIntent().getExtras();
		
		mAdapter = new FriendViewAdapter(getApplicationContext());
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mFooterView = (TextView) inflater.inflate(R.layout.footer_friend_view, null);
		
		ListView mListView = getListView();
		mListView.addFooterView(mFooterView);
		
		mFooterView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Create New Event");
				Toast.makeText(getApplicationContext(), 
						"Establecer conexión para crear evento en el servidor", 
						Toast.LENGTH_LONG).show();	
			}
		});
		
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		FriendRecord fr = (FriendRecord)l.getItemAtPosition(position);
		
		CheckBox cb = (CheckBox) v.findViewById(R.id.friend_checkbox);
		if(cb.isChecked()){
			cb.setChecked(false);
		}else{
			cb.setChecked(true);
			Toast.makeText(getApplicationContext(), "ID: "+fr.getId()+" Name: "+fr.getName(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onResult(LoadPeopleResult peopleData) {
	  if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
		    mAdapter.add(peopleData, getApplicationContext());
		  } else {
		    Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
		  }
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		Log.d(TAG, mGoogleApiClient.toString());
		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
}
