package eu.areamobile.android.apps.amrevolution.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class AMRevolutionProvider extends ContentProvider{
	public final static String TAG = AMRevolutionProvider.class.getSimpleName();
	
	private AMRDBOpenHelper mDB;
	
	@Override
	public boolean onCreate() {
		mDB = new AMRDBOpenHelper(getContext());
		return true;
	}

	
	@Override
	public String getType(Uri uri) {
		Log.v(TAG, "getType: "+uri);
		final int match = ProviderConstants.MATCHER.match(uri);
		switch (match) {
		case AMRevolutionContract.News.TOKEN: return AMRevolutionContract.News.CONTENT_TYPE_DIR;
		case AMRevolutionContract.News.ITEM_TOKEN: return AMRevolutionContract.News.CONTENT_TYPE_ITEM;
		case AMRevolutionContract.Snippets.TOKEN: return AMRevolutionContract.Snippets.CONTENT_TYPE_DIR;
		case AMRevolutionContract.Snippets.ITEM_TOKEN: return AMRevolutionContract.Snippets.CONTENT_TYPE_ITEM;
		default: return null;
		}
	}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		Log.v(TAG, "query: "+uri);
		final int match = ProviderConstants.MATCHER.match(uri);
		final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (match) {
		case AMRevolutionContract.News.TOKEN:
			qb.setTables(AMRevolutionContract.News.PATH);
			break;
		case AMRevolutionContract.News.ITEM_TOKEN:
			qb.setTables(AMRevolutionContract.News.PATH);
			addWhereId(uri, qb);
			break;
		case AMRevolutionContract.Snippets.TOKEN: 
			qb.setTables(AMRevolutionContract.Snippets.PATH);
			break;
		case AMRevolutionContract.Snippets.ITEM_TOKEN:
			qb.setTables(AMRevolutionContract.Snippets.PATH);
			addWhereId(uri, qb);
			break;
		case AMRevolutionContract.News.TIMED_TOKEN:
			qb.setTables(AMRevolutionContract.News.PATH);
			qb.appendWhere(AMRevolutionContract.CommonColumns.TIME_STAMP+ " > "+uri.getLastPathSegment());
			break;
		case AMRevolutionContract.Snippets.TIMED_TOKEN:
			qb.setTables(AMRevolutionContract.Snippets.PATH);
			qb.appendWhere(AMRevolutionContract.CommonColumns.TIME_STAMP+ " > "+uri.getLastPathSegment());
			break;
		default: 
			throw unsupportedUri(uri);
		}
		final SQLiteDatabase db = mDB.getReadableDatabase();
		final Cursor c =qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	

	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.v(TAG, "insert: "+uri+" with "+values);
		final int match = ProviderConstants.MATCHER.match(uri);
		final String table;
		switch (match) {
		case AMRevolutionContract.News.TOKEN:
			table = AMRevolutionContract.News.PATH;
			break;
		case AMRevolutionContract.Snippets.TOKEN: 
			table = AMRevolutionContract.Snippets.PATH;
			break;
		case AMRevolutionContract.Snippets.ITEM_TOKEN:
		case AMRevolutionContract.News.ITEM_TOKEN:			
		default: throw unsupportedUri(uri);
		}
		
		final SQLiteDatabase db = mDB.getWritableDatabase();
		final long id =db.insert(table, "", values);
		if(id==-1) return null;
		getContext().getContentResolver().notifyChange(uri, null);
		return ContentUris.withAppendedId(uri, id);
	}

	
	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		Log.v(TAG, "update: "+uri);
		final int match = ProviderConstants.MATCHER.match(uri);
		final String table;
		final String where;
		switch (match) {
		case AMRevolutionContract.News.TOKEN:
			table = AMRevolutionContract.News.PATH;
			where = selection;
			break;
		case AMRevolutionContract.Snippets.TOKEN: 
			table = AMRevolutionContract.Snippets.PATH;
			where = selection;
			break;
		case AMRevolutionContract.Snippets.ITEM_TOKEN:
			table = AMRevolutionContract.Snippets.PATH;
			where = TextUtils.isEmpty(selection)?getWhereId(uri):
							(getWhereId(uri)+ " AND ("+selection+')');
			break;
		case AMRevolutionContract.News.ITEM_TOKEN:
			table = AMRevolutionContract.News.PATH;
			where = TextUtils.isEmpty(selection)?getWhereId(uri):
				(getWhereId(uri)+ " AND ("+selection+')');
			break;
		default: throw unsupportedUri(uri);
		}
		
		final SQLiteDatabase db = mDB.getWritableDatabase();
		final int updates = db.update(table, values, where, selectionArgs);
		if(updates>0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return updates;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.v(TAG, "delete: "+uri);
		final int match = ProviderConstants.MATCHER.match(uri);
		final String table;
		final String where;
		switch (match) {
		case AMRevolutionContract.News.TOKEN:
			table = AMRevolutionContract.News.PATH;
			where = selection;
			break;
		case AMRevolutionContract.Snippets.TOKEN: 
			table = AMRevolutionContract.Snippets.PATH;
			where = selection;
			break;
		case AMRevolutionContract.Snippets.ITEM_TOKEN:
			table = AMRevolutionContract.Snippets.PATH;
			where = TextUtils.isEmpty(selection)?getWhereId(uri):
							(getWhereId(uri)+ " AND ("+selection+')');
			break;
		case AMRevolutionContract.News.ITEM_TOKEN:
			table = AMRevolutionContract.News.PATH;
			where = TextUtils.isEmpty(selection)?getWhereId(uri):
				(getWhereId(uri)+ " AND ("+selection+')');
			break;
		default: throw unsupportedUri(uri);
		}
		
		final SQLiteDatabase db = mDB.getWritableDatabase();
		final int deleted = db.delete(table, where, selectionArgs);
		if(deleted>0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return deleted;
	}



	private static UnsupportedOperationException unsupportedUri(Uri uri) {
		return new UnsupportedOperationException("uri: "+uri+ " is not supported");
	}


	private static String getWhereId(Uri uri){
		return AMRevolutionContract.CommonColumns.ID+" = "+uri.getLastPathSegment();
	}
	
	private static void addWhereId(Uri uri, final SQLiteQueryBuilder qb) {
		qb.appendWhere(getWhereId(uri));
	}


	
}
