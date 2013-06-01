package com.javierc.albuquerquenow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ExploreActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);
		
		((Button)findViewById(R.id.dogBtn)).setOnClickListener(exploreBtn());
		((Button)findViewById(R.id.publicArtBtn)).setOnClickListener(exploreBtn());
		((Button)findViewById(R.id.sportBtn)).setOnClickListener(exploreBtn());
		((Button)findViewById(R.id.bbBtn)).setOnClickListener(exploreBtn());
		((Button)findViewById(R.id.wifi)).setOnClickListener(exploreBtn());
		((Button)findViewById(R.id.bike)).setOnClickListener(exploreBtn());
		

	}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	private OnClickListener exploreBtn() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			Intent i=new Intent(getBaseContext(),ExploreMap.class);
			String url = "";
			boolean t= true;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				switch (v.getId()) {
				case R.id.bbBtn:
					url = "https://maps.google.com/maps/ms?ie=UTF8&t=m&source=embed&msa=0&output=kml&msid=210068994063648859995.0004c599eab0a94d0f229";
					break;
				case R.id.dogBtn:
					//url = "http://data.cabq.gov/community/reghistplaces/RegisteredHistoricPlaces.kmz";
					i.setClass(getBaseContext(),DogParkMap.class);
					break;
				case R.id.publicArtBtn:
					url = "http://data.cabq.gov/community/art/publicart/PublicArt.kmz";
					i.putExtra("kmz", "kmz");
					break;
				case R.id.sportBtn:
					i.setClass(getBaseContext(),SportMap.class);
					break;
				case R.id.wifi:
					t=false;
//					i.setClass(getBaseContext(), WifiMap.class);
					Toast.makeText(getBaseContext(), "A new feature!", Toast.LENGTH_SHORT).show();
					break;
				case R.id.bike:
					t=false;
//					i.setClass(getBaseContext(), WifiMap.class);
					Toast.makeText(getBaseContext(), "A new feature!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				if (t) {
					i.putExtra("url", url);
					startActivity(i);
				}
				
			}
		};
	}
	/* going to implement a tilt of each button on slide up and down similar to flipboard
	public void tilt(final View v, final float deg) {
		v.setRotationX(deg);
		
		v.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				v.setRotationX(0);
			}
		}, 300);
	}
	
	*/
}
