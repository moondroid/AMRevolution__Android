package eu.areamobile.android.apps.amrevolution.fragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.activities.AMBaseActivity;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;

public class AMSnippetsFragment extends AMBaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
	OnItemClickListener{
	public final static String TAG = AMSnippetsFragment.class.getSimpleName();
	
	private AdapterView<SimpleCursorAdapter> mAdapterView;
	private SimpleCursorAdapter mAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.snippet_layout, null);
		mAdapterView = (AdapterView<SimpleCursorAdapter>) v.findViewById(R.id.snippet_list);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO: change columns names
		mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] { AMRevolutionContract.Snippets.Columns.ID },
				new int[] { android.R.id.text1 }, 0);
		mAdapterView.setAdapter(mAdapter);
		getLoaderManager().initLoader(AMBaseActivity.SNIPPET_LOADER_ID, null, this);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO: change projection and selection!
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

}
