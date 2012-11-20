package eu.areamobile.android.apps.amrevolution.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.activities.AMNewsActivity;
import eu.areamobile.android.apps.amrevolution.activities.AMSnippetsActivity;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;

public class NotificationReceiver extends BroadcastReceiver{
	public final static String EXTRA_COUNT = "EXTRA_COUNT";
	private final static String EXTRA_CLEAR_ACTION ="EXTRA_CLEAR_ACTION";
	
	private final static String TAG = NotificationReceiver.class.getSimpleName();
	
	private final static int NOTIFICATION_ID = 100;
	private final static String PENDING_NOTIFICATIONS="PENDING_NOTIFICATIONS";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final SharedPreferences prefs =context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		if(intent.getBooleanExtra(EXTRA_CLEAR_ACTION, false)){
			Log.i(TAG, "Clear running notification count");
			prefs.edit().remove(PENDING_NOTIFICATIONS).commit();
		}else{
			Log.i(TAG, "New content");
			if(!NotificationReceiver.isApplicationRunning(context, "eu.areamobile.android.apps.amrevolution")){
				Log.i(TAG, "Delivering notification, application is not running");
				// Update pending notifications count
				final int pendingNotifications = prefs.getInt(PENDING_NOTIFICATIONS,0)+intent.getIntExtra(EXTRA_COUNT, 1);
				prefs.edit().putInt(EXTRA_COUNT, pendingNotifications).commit();
				
				final NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				Notification notify = new NotificationCompat.Builder(context)
										.setSmallIcon(R.drawable.ic_launcher)
										.setContentTitle("AreaMobileRevolution")
										.setContentText("AreaMobileRevolution")
										.setAutoCancel(true)
										.setNumber(pendingNotifications)
										.setDeleteIntent(clearNotificationsIntent(context))
										.setContentIntent(chooseActivityIntent(context, intent.getData()))
										.build();
				
				manager.notify(NOTIFICATION_ID, notify);
			}
		}
	}

	
	
	private static PendingIntent chooseActivityIntent(Context context,Uri data) {
		final Class<?> clazz=AMRevolutionContract.News.CONTENT_URI.equals(data)?AMNewsActivity.class:AMSnippetsActivity.class;
		return PendingIntent.getActivity(context, 0, new Intent(context,clazz), 0);
	}

	private static PendingIntent clearNotificationsIntent(Context context) {
		Intent intent = new Intent(context,NotificationReceiver.class);
		intent.putExtra(EXTRA_CLEAR_ACTION, true);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}
	
	private static boolean isApplicationRunning(Context context,String componentName){
		final ActivityManager manager= (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningTaskInfo> taskInfos=manager.getRunningTasks(1);
		return taskInfos.get(0).topActivity.getPackageName().equalsIgnoreCase(componentName);
	}
}
