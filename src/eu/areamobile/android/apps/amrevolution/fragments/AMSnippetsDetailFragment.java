package eu.areamobile.android.apps.amrevolution.fragments;

import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AMSnippetsDetailFragment extends AMBaseFragment{
	public final static String TAG = AMSnippetsDetailFragment.class.getSimpleName();
	
	private final static String SNIPPETS_URI_KEY = "SNIPPETS_URI_KEY";
	private Uri mSnippetsUri;
	private TextView mTitleTextView;
	private TextView mCodeTextView;
	
	public static AMSnippetsDetailFragment newInstance(Uri snippetsUri){
		AMSnippetsDetailFragment f = new AMSnippetsDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(SNIPPETS_URI_KEY, snippetsUri);
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.snippets_detail_layout, container, false);
		mTitleTextView = (TextView)v.findViewById(R.id.tv_snippets_detail_title);
		mCodeTextView = (TextView)v.findViewById(R.id.tv_snippets_detail_body);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState==null) {
			mSnippetsUri = getArguments().getParcelable(SNIPPETS_URI_KEY);
		}else {
			mSnippetsUri = savedInstanceState.getParcelable(SNIPPETS_URI_KEY);
		}
		fillDataByUri(mSnippetsUri);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(SNIPPETS_URI_KEY, mSnippetsUri);
	}
	
	private void fillDataByUri(Uri uri) {
		String[] projection = { 
			AMRevolutionContract.Snippets.Columns.TITLE,
			AMRevolutionContract.Snippets.Columns.CODE,
		};
		Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,null,null);
		if (cursor != null) {
			cursor.moveToFirst();
			String title = cursor.getString(cursor.getColumnIndex(AMRevolutionContract.Snippets.Columns.TITLE));
			String body = cursor.getString(cursor.getColumnIndex(AMRevolutionContract.Snippets.Columns.CODE));
			mTitleTextView.setText(title);
			mCodeTextView.setText(body);
			cursor.close();
		}
	}
	
}
