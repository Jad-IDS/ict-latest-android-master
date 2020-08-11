package com.ids.ict.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ids.ict.Actions;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;



public class toMoreActivity extends Activity {
	Connection conn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Actions.setLocal(this);
	//	TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
		setContentView(R.layout.morelist);
		Actions.loadMainBar(this);
		TCTDbAdapter datasoure =new  TCTDbAdapter(this);
		datasoure.open();
		ArrayList<Profile> arr =   datasoure.getAllProfiles();
		String lan="";
		if(arr.size()>0){
			lan = arr.get(0).getlang();
		}
		if(lan.equals("English")){
			Intent intent = new Intent(toMoreActivity.this, MoreActivity.class);
			startActivity(intent);
			datasoure.close();
		}
		else{
			final Button butEn = (Button) findViewById(R.id.en_button);
			final Button butAr = (Button) findViewById(R.id.ar_button);
			butEn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Locale locale_en = new Locale("en"); 
					Locale.setDefault(locale_en);
					Configuration config_en = new Configuration();
					config_en.locale = locale_en;
					getBaseContext().getResources().updateConfiguration(config_en, getBaseContext().getResources().getDisplayMetrics());

					Intent intent = new Intent(toMoreActivity.this, MoreActivity.class);
					startActivity(intent);
					toMoreActivity.this.finish();
				}
			});

			butAr.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Locale locale_ar = new Locale("ar_EG"); 
					Locale.setDefault(locale_ar);
					Configuration config_ar = new Configuration();
					config_ar.locale = locale_ar;
					getBaseContext().getResources().updateConfiguration(config_ar, getBaseContext().getResources().getDisplayMetrics());

					Intent intent = new Intent(toMoreActivity.this, MoreActivity.class);
					startActivity(intent);

				}
			});

			Launching mLaunching=new Launching();
			mLaunching.execute();
		}
	}

	public void backTo(View v){
		Actions.backTo(this);
	}

	protected class Launching extends AsyncTask<Void,Void,Integer>{
		@Override
		protected void onPreExecute(){
			// ImageView mv=(ImageView)findViewById(R.idr.img);
			// mv.setVisibility(View.VISIBLE);
		}
		@Override
		protected Integer doInBackground(Void... a){
			Actions.setScreenWidthHeight(toMoreActivity.this);

			Intent intent=new Intent(toMoreActivity.this,MoreActivity.class);
			//  intent.putExtra("footerButton", R.id.paper);

			startActivity(intent);

			return 1;

		}
		@Override
		protected void onPostExecute(Integer result){

			toMoreActivity.this.finish();


		}
	}

	public void footer(View v) {

		ImageButton mButton = (ImageButton) v;
		Intent intent = new Intent();
		switch (mButton.getId()) {
		case R.id.morebtn: {
		}
		case R.id.home: {
			intent.setClass(toMoreActivity.this, HomePageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			toMoreActivity.this.startActivity(intent);
			toMoreActivity.this.finish();
			break;
		}

		}
	}

}
