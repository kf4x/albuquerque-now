package com.javierc.albuquerquenow;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_about);
	
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.home);
		
		((Button)findViewById(R.id.contact_me)).setOnClickListener(contactBtnlick());
		((ImageButton)findViewById(R.id.cabqBtn)).setOnClickListener(cabqBtnClick());
		((ImageButton)findViewById(R.id.its_tripBtn)).setOnClickListener(itsaTripClick());
	
	}
	
	private OnClickListener itsaTripClick() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.itsatrip.org/"));
				startActivity(i);
			}
		};
	}

	private OnClickListener cabqBtnClick() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.cabq.gov/"));
				startActivity(i);
			}
		};
	}

	private OnClickListener contactBtnlick() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				/* Fill it with Data */
				emailIntent.setType("plain/text");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"javier@itsnotartitscode.com"});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Albuquerque Now");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey Javier,\n");

				/* Send it off to the Activity-Chooser */
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		};
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
