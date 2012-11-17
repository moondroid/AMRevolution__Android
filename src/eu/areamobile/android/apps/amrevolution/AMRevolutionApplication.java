package eu.areamobile.android.apps.amrevolution;

import android.app.Application;

public class AMRevolutionApplication extends Application {
	public String myRegId;
	
	@Override
	public void onCreate() {
		// workaround for http://code.google.com/p/android/issues/detail?id=20915
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		super.onCreate();
	}
}
