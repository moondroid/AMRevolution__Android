package eu.areamobile.android.apps.amrevolution.provider;

import android.content.UriMatcher;

class ProviderConstants {
	final static UriMatcher MATCHER = buildMatcher();
	
	final static String DATABASE_NAME = "amrevolution.db";
	final static int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLE = "CREATE TABLE ";
	
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
	
	private static UriMatcher buildMatcher(){
		UriMatcher m= new UriMatcher(UriMatcher.NO_MATCH);
		
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.PATH, AMRevolutionContract.News.TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.PATH_FOR_ID, AMRevolutionContract.News.ITEM_TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.News.SINCE_TIMESTAMP_PATH, AMRevolutionContract.News.TIMED_TOKEN);
		
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.PATH, AMRevolutionContract.Snippets.TOKEN);
		
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.PATH_FOR_ID, AMRevolutionContract.Snippets.ITEM_TOKEN);
		m.addURI(AMRevolutionContract.AUTHORITY, AMRevolutionContract.Snippets.SINCE_TIMESTAMP_PATH, AMRevolutionContract.Snippets.TIMED_TOKEN);
		
		return m;
	}
}
