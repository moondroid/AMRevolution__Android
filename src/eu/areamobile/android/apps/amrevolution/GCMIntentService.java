package eu.areamobile.android.apps.amrevolution;

import com.google.android.gcm.GCMBaseIntentService;

import eu.areamobile.android.apps.amrevolution.service.AMDownloaderIntentService;
import eu.areamobile.android.apps.amrevolution.utils.Constants;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onError(Context context, String arg1) {
		Toast.makeText(context, arg1, Toast.LENGTH_LONG);
		Log.v("GCM onError", arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.v("GCM onMessage", "Message Arrived");
		handleMessage(context, intent);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.v("GCM OnRegistered", "OnRegistered Arrived with registration id: " + regId);
		((AMRevolutionApplication)getApplication()).myRegId = regId;
		//ServerPushNotification myServerPushNotification = new ServerPushNotification(context, regId);
    	//myServerPushNotification.execute();
	}

	@Override
	protected void onUnregistered(Context context, String arg1) {
		
	}

	
	private void handleMessage(Context context, Intent intent) {
		if(intent == null) {
			return;
		}
		
		Bundle myBundle = intent.getExtras();
		
		if(myBundle != null) {
			// THE SECTION: NEWS, CODE_SNIPPETS, ...
			String mySection = myBundle.getString("section");
			
			// THE MESSAGE
			String myMessage = myBundle.getString("message");
			
			// THE CUSTOM VALUE -> The id value (depending on the section)
			String myCustomVal = myBundle.getString("custom_value");
			
			//handleNotification(context, mySection, myCustomVal, myMessage);
			
			Log.v("GCM handleMessage", "Section: " + mySection + "\nMessage: " + myMessage + "\nCustomValue: " + myCustomVal);
			
			// launch the download service
			AMDownloaderIntentService.startDownloaderService(context, mySection, Constants.MODALITY_ID, myCustomVal);
		}
	}
}
