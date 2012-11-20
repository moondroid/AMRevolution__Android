package eu.areamobile.android.apps.amrevolution.provider;

import android.content.ContentValues;
import android.content.UriMatcher;

class ProviderConstants {
	final static UriMatcher MATCHER = buildMatcher();
	
	final static String DATABASE_NAME = "amrevolution.db";
	final static int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLE = "CREATE TABLE ";
	
	final static String CREATE_STATE_TABLE = CREATE_TABLE+ AMRevolutionStateContract.PATH + '('
				+AMRevolutionStateContract.Columns.ID+ " INTEGER PRIMARY KEY, "
				+AMRevolutionStateContract.Columns.LAST_TRY+ " INTEGER NOT NULL, "
				+AMRevolutionStateContract.Columns.STATE+ " INTEGER NOT NULL, "
				+AMRevolutionStateContract.Columns.LAST_TRY+ " INTEGER NOT NULL"
				+')';
	
	final static String CREATE_NEWS_TABLE = CREATE_TABLE + AMRevolutionContract.News.PATH + '('
				+AMRevolutionContract.News.Columns.ID + " INTEGER PRIMARY KEY, "
				+AMRevolutionContract.News.Columns.TITLE+ " TEXT, "
				+AMRevolutionContract.News.Columns.BODY + " TEXT, "
				+AMRevolutionContract.News.Columns.IMAGE_URL + " TEXT, "
				+AMRevolutionContract.News.Columns.WEB_URL + " TEXT, "
				+AMRevolutionContract.News.Columns.TIME_STAMP + " INTEGER NOT NULL"
				+')';
	
	final static String CREATE_CODE_SNIPPETS = CREATE_TABLE + AMRevolutionContract.Snippets.PATH + '('
			+AMRevolutionContract.Snippets.Columns.ID + " INTEGER PRIMARY KEY, "
			+AMRevolutionContract.Snippets.Columns.TITLE+ " TEXT, "
			+AMRevolutionContract.Snippets.Columns.CODE+ " TEXT, "
			+AMRevolutionContract.Snippets.Columns.LANG+ " TEXT, "
			+AMRevolutionContract.Snippets.Columns.TIME_STAMP+ " INTEGER NOT NULL"
			+')';
	
	
	private final static String DROP_TABLE = "DROP TABLE ";
	final static String DROP_NEWS = DROP_TABLE + AMRevolutionContract.News.PATH;
	final static String DROP_SNIPPETS = DROP_TABLE + AMRevolutionContract.Snippets.PATH;
	final static String DROP_STATE = DROP_TABLE + AMRevolutionStateContract.PATH;
	
	final static ContentValues INITIAL_STATE = initStateValues();
	
	private static ContentValues initStateValues(){
		final ContentValues values = new ContentValues();
		values.put(AMRevolutionStateContract.Columns.ID, AMRevolutionStateContract.REC_ID);
		values.put(AMRevolutionStateContract.Columns.LAST_TRY, 0);
		values.put(AMRevolutionStateContract.Columns.STATE, AMRevolutionStateContract.State.IDLE);
		values.put(AMRevolutionStateContract.Columns.LAST_UPDATE, 0);
		return values;
	}
	
	private static UriMatcher buildMatcher(){
		UriMatcher m= new UriMatcher(UriMatcher.NO_MATCH);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionStateContract.PATH, AMRevolutionStateContract.TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.PATH, AMRevolutionContract.News.TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.PATH_FOR_ID, AMRevolutionContract.News.ITEM_TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.SINCE_TIMESTAMP_PATH, AMRevolutionContract.Snippets.ITEM_TOKEN);
		
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.PATH, AMRevolutionContract.Snippets.TOKEN);
		
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.PATH_FOR_ID, AMRevolutionContract.Snippets.ITEM_TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.SINCE_TIMESTAMP_PATH, AMRevolutionContract.Snippets.ITEM_TOKEN);
		
		return m;
	}
}
