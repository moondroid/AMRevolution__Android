package eu.areamobile.android.apps.amrevolution.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver{
	private final static String TAG = NotificationReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		if(!NotificationReceiver.isApplicationRunning(context, "eu.areamobile.android.apps.amrevolution")){
			Log.v(TAG, "Delivering notification, application is not running");
			
		}
	}
	
	
	private static boolean isApplicationRunning(Context context,String componentName){
		ActivityManager manager= (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfos=manager.getRunningTasks(1);
		return taskInfos.get(0).topActivity.getPackageName().equalsIgnoreCase(componentName);
	}
}
