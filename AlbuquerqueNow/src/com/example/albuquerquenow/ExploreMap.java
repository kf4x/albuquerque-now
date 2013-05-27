package com.example.albuquerquenow;



import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class ExploreMap extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		String kmz, url;
		url = extras.getString("url");
		kmz = extras.getString("kmz");

		if (kmz != null && url != null) {
//			Log.d("=====","kmz");
			new KmlTask(this).execute(url, kmz);
		} else if (url != null) {
//			Log.d("=====","url");
			new KmlTask(this).execute(url);
		}
		
		

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

}
