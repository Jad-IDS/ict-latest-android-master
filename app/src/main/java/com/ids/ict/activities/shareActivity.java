package com.ids.ict.activities;

import java.sql.Connection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ids.ict.R;
import com.ids.ict.R.layout;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;


public class shareActivity extends Activity {

	Connection conn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Actions.setLocal(this);
		setContentView(R.layout.appshare);
		Actions.loadMainBar(this);
		MyApplication app=(MyApplication) getApplicationContext();
		//    app.face=Typeface.createFromAsset(getAssets(), "fonts/tahomabd.ttf");

		Launching mLaunching=new Launching();
		mLaunching.execute();

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
			Actions.setScreenWidthHeight(shareActivity.this);

			Intent intent=new Intent(shareActivity.this,LanguageActivity.class);
			//  intent.putExtra("footerButton", R.id.paper);

			startActivity(intent);

			return 1;

		}
		@Override
		protected void onPostExecute(Integer result){

			shareActivity.this.finish();


		}
	}



}
