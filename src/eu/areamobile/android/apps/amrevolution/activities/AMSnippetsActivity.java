package eu.areamobile.android.apps.amrevolution.activities;

import android.os.Bundle;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.fragments.AMSnippetsFragment;

public class AMSnippetsActivity extends AMBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amsnippets);
		transactToAMSnippetsFragment();
	}
	
	private void transactToAMSnippetsFragment(){
		AMSnippetsFragment f = new AMSnippetsFragment();
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_fragment_container, f, AMSnippetsFragment.TAG).commit();
	}
	
}
