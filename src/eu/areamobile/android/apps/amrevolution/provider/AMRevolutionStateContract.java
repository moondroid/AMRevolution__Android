package eu.areamobile.android.apps.amrevolution.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class AMRevolutionStateContract {
	final static String PATH = "state";
	final static int TOKEN = 100;
	public final static Uri CONTENT_URI = AMRevolutionContract.BASE_URI.buildUpon().appendPath(PATH).build();
	final static String CONTENT_TYPE = AMRevolutionContract.CURSOR_ITEM_BASE_TYPE+PATH;
	
	final static int REC_ID = 1;
	public final static class State{
		public final static int IDLE = 0;
		public final static int PENDING = 1;
		public final static int FAILED = 2;
	}
	
	public final static class Columns{
		public final static String ID =BaseColumns._ID;
		public final static String LAST_UPDATE ="last_update";
		public final static String STATE = "state";
		public final static String LAST_TRY ="last_try";
	}
}
