package com.javierc.albuquerquenow;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class SportMap extends MapActivity implements ActionBar.OnNavigationListener{
	ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.home);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] dropdownValues = getResources().getStringArray(
				R.array.sports);

		// Specify a SpinnerAdapter to populate the dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(adapter, this);
		
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
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		String url = "";

		switch (arg0) {
		case 0:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.0004b2e5e761b1f8bb9ac";
			break;
		
		case 1:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.0004b0b1c17bce10ed5e4";
			break;
		
		case 2:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.0004a16f8b578adc71335";
			break;
		case 3:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.00044bb43ac436fe75755";
			break;
			
		case 4:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.00044d9bf0aa34e87f9f1";
			break;
		
		case 5:
			url = "https://maps.google.com/maps/ms?hl=en&gl=us&ptab=2&ie=UTF8&oe=UTF8&authuser=0&msa=0&output=kml&msid=212436961353859021350.000445ac22cf6155f168d";
			break;
		default:
			break;
		}
		googlemap.clear();
		new KmlTask(this).execute(url);
		return false;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		googlemap.clear();
		super.onPause();
	}
}
