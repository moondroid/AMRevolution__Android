package eu.areamobile.android.apps.amrevolution.activities;

import android.os.Bundle;
import eu.areamobile.android.apps.amrevolution.R;
import eu.areamobile.android.apps.amrevolution.fragments.AMNewsFragment;

public class AMNewsActivity extends AMBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amnews);
		transactToAMNewsFragment();
	}
	
	private void transactToAMNewsFragment(){
		AMNewsFragment f = new AMNewsFragment();
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fl_fragment_container, f, AMNewsFragment.TAG).commit();
	}
	
}
