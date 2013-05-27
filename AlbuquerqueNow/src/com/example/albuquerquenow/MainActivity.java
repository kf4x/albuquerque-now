package com.example.albuquerquenow;

import java.io.File;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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
        rbmView.getVisibility();
        

	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		LinearLayout ll = (LinearLayout)findViewById(R.id.mainll_mA);
		if (id == android.R.id.home) {
			ll.setVisibility(ll.getVisibility() == View.GONE ? View.VISIBLE :View.GONE); 

			rbmView.toggleMenu();
			
			return true;
		
		} else {
			return super.onOptionsItemSelected(item);
		}

	}
	
	
	
	

	@Override
	public void RibbonMenuItemClick(int itemId) {
		// TODO Auto-generated method stub
		LinearLayout ll = (LinearLayout)findViewById(R.id.mainll_mA);
		ll.setVisibility(ll.getVisibility() == View.GONE ? View.VISIBLE :View.GONE); 
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
	@Override
	protected void onDestroy() {
	// closing Entire Application
		trimCache(this);
	    super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	public static void trimCache(Context context) {
	    try {
	        File dir = new File(Environment.getExternalStorageDirectory() + "/abqnowkml");
	        if (dir != null && dir.isDirectory()) {
	            deleteDir(dir);

	        }
	    } catch (Exception e) {
	        // TODO: handle exception
	    }
	}


	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    return dir.delete();
	}
}
