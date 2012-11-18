package eu.areamobile.android.apps.amrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gcm.GCMRegistrar;

import eu.areamobile.android.apps.amrevolution.AMRevolutionApplication;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.utils.Constants;

public class AMMainActivity extends AMBaseActivity {
	public final static String TAG = AMMainActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ammain);

		registerGoogleCloudMessaging();
		
		/* lancia AMNewsActivity */
		Intent activityIntent = new Intent(this, AMNewsActivity.class);
		startActivity(activityIntent);
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_ammain, menu);
		return true;
	}

	private void registerGoogleCloudMessaging() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(getApplicationContext(), Constants.SENDER_ID);
		} else {
			((AMRevolutionApplication) getApplication()).myRegId = regId;

			// ServerPushNotification myServerPushNotification = new ServerPushNotification(this, regId);
			// myServerPushNotification.execute();

			Log.v("AMRevolution MainActivity Push Notification", "Already registered and registration id is: " + regId);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.news_activity:{
			Intent activityIntent = new Intent(this, AMNewsActivity.class);
			startActivity(activityIntent);
		}break;
		case R.id.snippets_activity:{
			Intent activityIntent = new Intent(this, AMSnippetsActivity.class);
			startActivity(activityIntent);
		}break;
		default:
			break;
		}
		return true;
	}
	
}
