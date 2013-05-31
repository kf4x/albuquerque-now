package com.javierc.albuquerquenow;



import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

public class ExploreMap extends MapActivity {
	ProgressDialog pdialog;
	String kmz = null, url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);
		
		Bundle extras = getIntent().getExtras();
		useOfflineRoutes = false;
		
		url = extras.getString("url");
		kmz = extras.getString("kmz");
		pdialog=new ProgressDialog(this);
		pdialog.setCancelable(false);



		 runTask();

		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explore_map, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		googlemap.clear();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 runTask();
		super.onResume();
	}

	private void runTask() {
		pdialog.setMessage("Downloading and plotting....");
		pdialog.show();
		
		if (kmz != null && url != null) {
			//Log.d("=====","kmz");
			new KmzTask(this).execute(url);
		} else if (url != null) {
			//Log.d("=====","url");
			new KmlTask(this).execute(url);
		}
	}
}
