package com.ids.ict.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.ReportsListActivity;


import java.sql.Connection;


public class ReportFailActivity extends Activity {

	Connection conn;
	String lang = "";
	String num,qatarid,email,issueid,spid,date,comm,locx,locy,countid,affqatarid,id_rep;
	int repid;
	MyApplication app;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lang= Actions.setLocal(this);
		//TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
		setContentView(R.layout.reportfailedinter);
		Actions.loadMainBar(this);
		ViewResizing.setPageTextResizing(this);
		Typeface tf;
		if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)){
			tf=MyApplication.facePolarisMedium;
		}else{
			tf=MyApplication.faceDinar;
		}
		
		final Button butFin = (Button) findViewById(R.id.cont_button_fail);
		butFin.setTypeface(tf);
		TextView txt = (TextView) findViewById(R.id.textView2);
		txt.setTypeface(tf);
		TextView txt1 = (TextView) findViewById(R.id.textView21);
		txt1.setTypeface(tf);

		butFin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ReportFailActivity.this, ReportsListActivity.class);
				startActivity(intent);
			}
		});

	}
	public void goToSettings(View v){
		Actions.goToSettings(this);
	}
	public void backTo(View v){
		Actions.backTo(this);
	}
//
//	public void sendSMS(String phoneNumber, String message)
//	{        
//		String SENT = "SMS_SENT";
//		String DELIVERED = "SMS_DELIVERED";
//
//		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
//				new Intent(SENT), 0);
//
//		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//				new Intent(DELIVERED), 0);
//
//		//---when the SMS has been sent---
//		registerReceiver(new BroadcastReceiver(){
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				switch (getResultCode())
//				{
//				case Activity.RESULT_OK:
//					Toast.makeText(getBaseContext(), "SMS sent", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//					Toast.makeText(getBaseContext(), "Generic failure", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				case SmsManager.RESULT_ERROR_NO_SERVICE:
//					Toast.makeText(getBaseContext(), "No service", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				case SmsManager.RESULT_ERROR_NULL_PDU:
//					Toast.makeText(getBaseContext(), "Null PDU", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				case SmsManager.RESULT_ERROR_RADIO_OFF:
//					Toast.makeText(getBaseContext(), "Radio off", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//		}, new IntentFilter(SENT));
//
//		registerReceiver(new BroadcastReceiver(){
//			@Override
//			public void onReceive(Context arg0, Intent arg1) {
//				switch (getResultCode())
//				{
//				case Activity.RESULT_OK:
//					Toast.makeText(getBaseContext(), "SMS delivered", 
//							Toast.LENGTH_SHORT).show();
//					break;
//				case Activity.RESULT_CANCELED:
//					Toast.makeText(getBaseContext(), "SMS not delivered", 
//							Toast.LENGTH_SHORT).show();
//					break;                        
//				}
//			}
//		}, new IntentFilter(DELIVERED));        
//
//		SmsManager sms = SmsManager.getDefault();
//		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
//	}

}



