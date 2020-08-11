package com.ids.ict.classes;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.shipp.util.MenuEventController;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.reportActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ArReportsNotificationListActivity extends Activity{
	ArrayList<Mail_OFF> events;	
	ListView eventList;
	ProgressBar mProgressBar;
	boolean launching;
	private boolean open = false;
	private final Context context = this;
	private ListView listMenu;
	private RelativeLayout layout;
	//the footer button corresponding to this activity
	int footerButton;
	MyApplication app;
	View mainBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Actions.setLocal(this);
		setContentView(R.layout.arreportsnotelist);
		Actions.loadMainBar(this);
		ViewResizing.setPageTextResizing(this);

		listMenu = (ListView) findViewById(R.id.listMenu1);
		final String [] values =  get_values();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.aligned_right, values);
		this.listMenu.setAdapter(adapter);
		footerButton= this.getIntent().getIntExtra("footerButton", R.id.home); 
		this.listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				if(position==0 || position ==10){

				}
				else{
					Intent intent = new Intent(getApplicationContext(), reportActivity.class);
					Bundle bundle=new Bundle();
					Event[] a = get_values_all();
					Event event=null;
					if(position>9){
						event =  a[position-2];
					}
					else{
						event =  a[position-1];	
					}

					bundle.putInt("eventId", event.getId());
					bundle.putString("eventName", event.getName());
					bundle.putString("eventDescription", event.getDescription());
					bundle.putString("eventDate",event.getDate());
					bundle.putString("caller", "eventslist");
					bundle.putString("eventLocation", event.getLocation());
					bundle.putString("activity", "EventsListActivity");
					//adding the bundle to the intent
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});


		startMyActivity();

	}

	public void goToSettings(View v){
		Actions.goToSettings(this);
	}

	public void backTo(View v){
		Actions.backTo(this);
	}

	public void openCloseMenu(View view){
		if(!this.open){
			this.open = true;
			MenuEventController.open(this.context, this.layout);
			MenuEventController.closeKeyboard(this.context, view);
		} else {
			this.open = false;
			MenuEventController.close(this.context, this.layout);
			MenuEventController.closeKeyboard(this.context, view);
		}
	}

	public String[] get_values(){

		int k=1;
		TCTDbAdapter source = new TCTDbAdapter(this);
		source.open();
		//source.de
		ArrayList<Event> arr1 = source.getissue_Type("1");
		ArrayList<Event> arr2 = source.getissue_Type("2");
		source.close();
		String[] values=new String[arr1.size()+arr2.size()+2];
		if(arr1.size()>0){
			Event [] n = arr1.toArray(new Event[arr1.size()]);
			values[0] = "Mobile";

			for(int i=0;i<n.length;i++){
				values[i+1] = n[i].getName();
				k++;
			}
		}

		//	ArrayList<Event> arr2 = source.getissue_Type("2");
		values[k] = "Fixed Line";
		if(arr2.size()>0){
			Event [] n1 = arr2.toArray(new Event[arr2.size()]);

			for(int i=0;i<n1.length;i++){
				values[k+1] = n1[i].getName();
				k++;
			}
		}

		return values;
	}

	public Event[] get_values_all(){
		Event[]n=null;
		Event[]n1=null;
		int k=0;
		TCTDbAdapter source = new TCTDbAdapter(this);
		source.open();
		ArrayList<Event> arr1 = source.getissue_Type("1");
		ArrayList<Event> arr2 = source.getissue_Type("2");
		Event[] values=new Event[arr1.size()+arr2.size()+2];
		if(arr1.size()>0){
			n = arr1.toArray(new Event[arr1.size()]);
			//values[0] = "Mobile";
		}
		if(arr2.size()>0){
			n1 = arr2.toArray(new Event[arr2.size()]);
			//values[0] = "Mobile";
		}

		for(int i=0;i<n.length;i++){
			values[i] = n[i];
			k++;
		}
		for(int i=0;i<n1.length;i++){
			values[k] = n1[i];
			k++;
		}
		//	ArrayList<Event> arr2 = source.getissue_Type("2");


		return values;
	}

	private void startMyActivity(){

		initializeViews();
		//footer button set checked state
		launching = true;
		eventList=(ListView)findViewById(R.id.reportsnote_list);
		//eventsList();
		LaunchingEvent eventLaunching = new LaunchingEvent();
		eventLaunching.execute();
	}

	private void initializeViews(){
		app=(MyApplication)getApplicationContext();
		launching=true;
		mainBar=(View)findViewById(R.id.main_bar);
		mProgressBar=(ProgressBar)findViewById(R.id.progress_bar2);
		final View mainBar= findViewById(R.id.main_bar);
		//ViewResizing.setHeightViewResizingListener(this, mainBar);
		final View footerBar= findViewById(R.id.footer);
		// ViewResizing.setHeightViewResizingListener(this, footerBar);
	}

	protected class LaunchingEvent extends AsyncTask<Void,Void,Integer>{
		Error error;
		Mail_OFF[] nn;
		@Override
		protected void onPreExecute(){


			//		mProgressBar.setVisibility(View.GONE);

		}
		@Override
		protected Integer doInBackground(Void... params) {
			Log.d("launching","passed here");
			AtomicReference <Mail_OFF[]> ref=new AtomicReference<Mail_OFF[]>(null);
			TCTDbAdapter source = new TCTDbAdapter(ArReportsNotificationListActivity.this);
			source.open();
			ArrayList<Mail_OFF> arr = source.getAllReports_off();
			nn = new Mail_OFF[arr.size()];
			nn = arr.toArray(nn);
			// nn = (Mail_OFF[]) arr.toArray();
			source.close();
			//	nn  = ref.get();
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result){

			ArReportsNotificationListArrayAdapter adapter  = new ArReportsNotificationListArrayAdapter( ArReportsNotificationListActivity.this ,nn);
			eventList.setAdapter(adapter);
			//}
			//mProgressBar.setVisibility(View.GONE);
		}

	}

	public void search(View v){
		onSearchRequested();
	}

}