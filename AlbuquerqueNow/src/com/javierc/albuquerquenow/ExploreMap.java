package com.javierc.albuquerquenow;



import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ExploreMap extends MapActivity {
	ProgressDialog pdialog;
	String kmz = null, url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.arrow_left);
		
		Bundle extras = getIntent().getExtras();
		useOfflineRoutes = false;
		
		url = extras.getString("url");
		kmz = extras.getString("kmz");
		pdialog=new ProgressDialog(this);
		pdialog.setCancelable(false);



		 runTask();

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explore_map, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		} 
		return super.onOptionsItemSelected(item);
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
