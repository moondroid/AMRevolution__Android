package eu.areamobile.android.apps.amrevolution.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AMRDBOpenHelper extends SQLiteOpenHelper{

	public AMRDBOpenHelper(Context context) {
		super(context, ProviderConstants.DATABASE_NAME, null, ProviderConstants.DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ProviderConstants.CREATE_STATE_TABLE);
		db.execSQL(ProviderConstants.CREATE_NEWS_TABLE);
		db.execSQL(ProviderConstants.CREATE_CODE_SNIPPETS);
		db.insert(AMRevolutionStateContract.PATH, null, ProviderConstants.INITIAL_STATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion>newVersion){
			db.execSQL(ProviderConstants.DROP_STATE);
			db.execSQL(ProviderConstants.DROP_SNIPPETS);
			db.execSQL(ProviderConstants.DROP_NEWS);
			this.onCreate(db);
		}
	}

}
