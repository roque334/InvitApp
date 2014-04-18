package bwl.invitapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import bwl.invitapp.R;

import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;


public class LoginActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {
	
	private String TAG = "PersonList";
	
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	
	/* A flag indicating that a PendingIntent is in progress and prevents
	 * us from starting further intents.
	 */
	private boolean mIntentInProgress;
	
	/* Track whether the sign-in button has been clicked so that we know to resolve
	 * all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;
	
	/* Store the connection result from onConnectionFailed callbacks so that we can
	 * resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;
	
	private DatabaseOpenHelper mDbHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d("Debug", "onCreate");

		setContentView(R.layout.login_layout);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		
		mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
						       .addConnectionCallbacks(this)
						       .addOnConnectionFailedListener(this)
						       .addApi(Plus.API, null)
						       .addScope(Plus.SCOPE_PLUS_LOGIN)
						       .build();
		
		findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mGoogleApiClient.isConnected()) {
					if (!mGoogleApiClient.isConnecting()) {
						Log.d("Debug", "onClick");
						mSignInClicked = true;
						resolveSignInError();
				    }
			    }else{
			    	Toast.makeText(getApplicationContext(), "You are alrady connected", Toast.LENGTH_LONG).show();
			    }
			}
		});
		
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    Log.d("Debug", "onStart");
	    mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
	    super.onStop();
	    Log.d("Debug", "onStop");
	    if (mGoogleApiClient.isConnected()) {
	      mGoogleApiClient.disconnect();
	    }
	}
	
	/* A helper method to resolve the current ConnectionResult error. */
	public void resolveSignInError() {
		Log.d("Debug", "resolverSignError " + mConnectionResult.hasResolution());
		if (mConnectionResult.hasResolution()) {
			Log.d("Debug", "hasResolution");
		    try {
		      mIntentInProgress = true;
		      startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
		          RC_SIGN_IN, null, 0, 0, 0);
		    } catch (SendIntentException e) {
		      // The intent was canceled before it was sent.  Return to the default
		      // state and attempt to connect to get an updated ConnectionResult.
		      mIntentInProgress = false;
		      mGoogleApiClient.connect();
		    }
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.d("Debug", "onConnectionFailed");
		if (!mIntentInProgress) {
		    // Store the ConnectionResult so that we can use it later when the user clicks
		    // 'sign-in'.
			Log.d("Debug", "mConnectionResult Saved");
		    mConnectionResult = result;

		    if (mSignInClicked) {
		      // The user has already clicked 'sign-in' so we attempt to resolve all
		      // errors until the user is signed in, or they cancel.
		      resolveSignInError();
		    }
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Log.d("Debug", "onConnected");
		ContentValues values = new ContentValues();
		Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		values.put(DatabaseOpenHelper.USER_ID, currentUser.getId());
		values.put(DatabaseOpenHelper.USER_NAME, currentUser.getDisplayName());
		
		Log.d("Debug", currentUser.getId());
		
		mSignInClicked = false;
		
		mDbHelper.insertRecord(DatabaseOpenHelper.TABLE_USER, null, values);
		
		Intent intent = new  Intent(getApplicationContext(), EventViewActivity.class);
		startActivity(intent);
//		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
//		Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
//	      .setResultCallback(this);
		
	}
	
	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.d("Debug", "onConnectionSuspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent intent) {
		Log.d("Debug", "onActivityResult");
		if (requestCode == RC_SIGN_IN) {
		    if (responseCode != RESULT_OK) {
		      mSignInClicked = false;
		    }

		    mIntentInProgress = false;

		    if (!mGoogleApiClient.isConnecting()) {
		      mGoogleApiClient.connect();
		    }
		  }
	}
	
	@Override
	public void onResult(LoadPeopleResult peopleData) {
	  if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
	    PersonBuffer personBuffer = peopleData.getPersonBuffer();
	    try {
	      int count = personBuffer.getCount();
	      for (int i = 0; i < count; i++) {
		    Log.d(TAG, "ID: " + personBuffer.get(i).getId());
	        Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
	        Toast.makeText(getApplicationContext(), personBuffer.get(i).getId()+" "+personBuffer.get(i).getDisplayName()
	        		, Toast.LENGTH_LONG).show();
	      }
	    } finally {
	      personBuffer.close();
	    }
	  } else {
	    Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
	  }
	}
}
