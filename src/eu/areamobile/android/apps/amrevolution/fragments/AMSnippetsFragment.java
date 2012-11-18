package eu.areamobile.android.apps.amrevolution.fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.activities.AMBaseActivity;
import eu.areamobile.android.apps.amrevolution.activities.AMNewsActivity;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;

public class AMSnippetsFragment extends AMBaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
	OnItemClickListener, OnClickListener{
	public final static String TAG = AMSnippetsFragment.class.getSimpleName();
	
	private AdapterView<SimpleCursorAdapter> mAdapterView;
	private SimpleCursorAdapter mAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.snippets_layout, null);
		mAdapterView = (AdapterView<SimpleCursorAdapter>) v.findViewById(R.id.snippet_list);
		v.findViewById(R.id.btn_to_news_activity).setOnClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.snippets_row_layout, null,
				new String[] { AMRevolutionContract.Snippets.Columns.TITLE },
				new int[] { R.id.tv_snippets_title } ,0);
		mAdapterView.setAdapter(mAdapter);
		mAdapterView.setOnItemClickListener(this);
		getLoaderManager().initLoader(AMBaseActivity.SNIPPET_LOADER_ID, null, this);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getActivity(), AMRevolutionContract.Snippets.CONTENT_URI, null, null, null, null);
		return loader;

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	public void onItemClick(AdapterView<?> av, View view, int pos, long id) {
		Uri snippetsUri = ContentUris.withAppendedId(AMRevolutionContract.Snippets.CONTENT_URI, id);
		AMSnippetsDetailFragment f = AMSnippetsDetailFragment.newInstance(snippetsUri);
		getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_fragment_container, f, AMSnippetsDetailFragment.TAG)
			.addToBackStack(null).commit();
	}

	@Override
	public void onClick(View v) {
		Intent activityIntent = new Intent(getActivity(), AMNewsActivity.class);
		activityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(activityIntent);
	}

}
