package com.example.albuquerquenow;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public class MainActivity extends Activity implements iRibbonMenuCallback {
	private RibbonMenuView rbmView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getActionBar().setTitle("Menu");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        rbmView.setBackgroundResource(R.color.concrete);


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();

		if (id == android.R.id.home) {
			
			rbmView.toggleMenu();
			
			return true;
		
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void RibbonMenuItemClick(int itemId) {
		// TODO Auto-generated method stub
		if(itemId == R.id.ribbon_menu_explore){
			startActivity(new Intent(getBaseContext(), ExploreActivity.class));
		}
		else if(itemId == R.id.ribbon_menu_transit){
			startActivity(new Intent(getBaseContext(), TransitMap.class));
		}
		else if(itemId == R.id.ribbon_menu_rrtransit){
			startActivity(new Intent(getBaseContext(), RailTransitMap.class));
		}
		else if(itemId == R.id.ribbon_menu_search){
			startActivity(new Intent(getBaseContext(), SearchMap.class));
		}
		else if(itemId == R.id.ribbon_menu_weather){
			startActivity(new Intent(getBaseContext(), WeatherActivity.class));
		}
	}

}
