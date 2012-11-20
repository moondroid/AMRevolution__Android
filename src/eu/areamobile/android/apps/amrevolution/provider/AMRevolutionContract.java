package eu.areamobile.android.apps.amrevolution.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

public final class AMRevolutionContract {
	private AMRevolutionContract(){}
	
	final static String AUTHORITY = "eu.areamobile.apps.amrevolution.provider";
	
	final static Uri BASE_URI = Uri.parse("content://"+AUTHORITY);
	
	
	private static final String VND_MIME_PREFIX = "vnd.amrevolution.";
	private final static String CURSOR_DIR_BASE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+'/'+VND_MIME_PREFIX;
	final static String CURSOR_ITEM_BASE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+'/'+VND_MIME_PREFIX;
	
	public final static class News{
		private News(){};
		
		final static String PATH = "news";
		final static String PATH_FOR_ID = PATH+"/#";
		final static int TOKEN = 1;
		final static int ITEM_TOKEN = 2;
		final static int TIMED_TOKEN = 3;
		public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
		
		public final static String CONTENT_TYPE_DIR = CURSOR_DIR_BASE_TYPE+PATH;
		
		final static String SINCE_TIMESTAMP_PATH = PATH+"/since/#";
		
		public static Uri since(long timestamp){
			return CONTENT_URI.buildUpon().appendPath("since").appendPath(Long.toString(timestamp)).build();
		}
		
		public static ContentValues values(int id, long timestamp, String title, String body, String img_url, String web_url){
			ContentValues myContentValues = new ContentValues();
			myContentValues.put(Columns.ID, id);
			myContentValues.put(Columns.TIME_STAMP, timestamp);
			myContentValues.put(Columns.TITLE, title);
			myContentValues.put(Columns.BODY, body);
			myContentValues.put(Columns.IMAGE_URL, img_url);
			myContentValues.put(Columns.WEB_URL, web_url);
			
			
			return myContentValues;
			//return CONTENT_URI.buildUpon().appendPath("since").appendPath(Long.toString(timestamp)).build();
		}
		
		public final static String CONTENT_TYPE_ITEM = CURSOR_ITEM_BASE_TYPE+PATH;
		public static final class Columns implements CommonColumns{
			private Columns(){}
			
			public final static String BODY = "body";
			public final static String IMAGE_URL = "image_url";
			public final static String WEB_URL = "web_url";
		}
	}
	
	public final static class Snippets{
		private Snippets(){};
		
		final static String PATH = "snippets";
		final static String PATH_FOR_ID = PATH+"/#";
		final static int TOKEN = 4;
		final static int ITEM_TOKEN = 5;
		final static int TIMED_TOKEN = 6;
		public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
		
		final static String SINCE_TIMESTAMP_PATH = PATH+"/since/#";
		
		public static Uri since(long timestamp){
			return CONTENT_URI.buildUpon().appendPath("since").appendPath(Long.toString(timestamp)).build();
		}
		
		public static ContentValues values(int id, long timestamp, String language_type, String title, String code){
			ContentValues myContentValues = new ContentValues();
			myContentValues.put(Columns.ID, id);
			myContentValues.put(Columns.TIME_STAMP, timestamp);
			myContentValues.put(Columns.LANG, language_type);
			myContentValues.put(Columns.TITLE, title);
			myContentValues.put(Columns.CODE, code);
			
			
			return myContentValues;
			//return CONTENT_URI.buildUpon().appendPath("since").appendPath(Long.toString(timestamp)).build();
		}

		public final static String CONTENT_TYPE_DIR = CURSOR_DIR_BASE_TYPE+PATH;
		public final static String CONTENT_TYPE_ITEM = CURSOR_ITEM_BASE_TYPE+PATH;
		
		public static final class Columns implements CommonColumns{
			private Columns(){}
			
			public final static String LANG="lang";
			public final static String CODE = "code";
		}
		
	}
	
	

	static interface CommonColumns{
		public final static String ID = "_id";
		public final static String TIME_STAMP ="tstamp";
		public final static String TITLE = "title";
	}
}
