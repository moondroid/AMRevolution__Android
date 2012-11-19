package eu.areamobile.android.apps.amrevolution.fragments;

import java.util.Date;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;

public class AMNewsDetailFragment extends AMBaseFragment{
	public final static String TAG = AMNewsDetailFragment.class.getSimpleName();

	private final static String NEWS_URI_KEY = "NEWS_URI_KEY";
	private Uri mNewsUri;
	private ImageView mImageView;
	private TextView mTitleTextView;
	private TextView mBodyTextView;
	private TextView mTimestampTextView;
	private TextView mUrlTextView;
	
	public static AMNewsDetailFragment newInstance(Uri newsUri){
		AMNewsDetailFragment f = new AMNewsDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(NEWS_URI_KEY, newsUri);
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.news_detail_layout, container, false);
		mImageView = (ImageView)v.findViewById(R.id.iv_news_image);
		mTitleTextView = (TextView)v.findViewById(R.id.tv_news_detail_title);
		mBodyTextView = (TextView)v.findViewById(R.id.tv_news_detail_body);
		mTimestampTextView = (TextView)v.findViewById(R.id.tv_news_detail_date);
		mUrlTextView = (TextView)v.findViewById(R.id.tv_news_detail_url);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState==null) {
			mNewsUri = getArguments().getParcelable(NEWS_URI_KEY);
		}else {
			mNewsUri = savedInstanceState.getParcelable(NEWS_URI_KEY);
		}
		fillDataByUri(mNewsUri);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(NEWS_URI_KEY, mNewsUri);
	}
	
	private void fillDataByUri(Uri uri) {
		String[] projection = { 
			AMRevolutionContract.News.Columns.TITLE,
			AMRevolutionContract.News.Columns.BODY,
			AMRevolutionContract.News.Columns.TIME_STAMP,
			AMRevolutionContract.News.Columns.WEB_URL,
		};
		Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,null,null);
		if (cursor != null) {
			cursor.moveToFirst();
			String title = cursor.getString(cursor.getColumnIndex(AMRevolutionContract.News.Columns.TITLE));
			String body = cursor.getString(cursor.getColumnIndex(AMRevolutionContract.News.Columns.BODY));
			Long timestamp = cursor.getLong(cursor.getColumnIndex(AMRevolutionContract.News.Columns.TIME_STAMP));
			String web_url = cursor.getString(cursor.getColumnIndex(AMRevolutionContract.News.Columns.WEB_URL));
			mTitleTextView.setText(title!=null?title:"");
			mBodyTextView.setText(body!=null?body:"");
			mTimestampTextView.setText(new Date(timestamp).toLocaleString());
			mUrlTextView.setText(web_url!=null?web_url:"");
			Linkify.addLinks(mUrlTextView, Linkify.WEB_URLS);
			cursor.close();
		}
	}
	
}