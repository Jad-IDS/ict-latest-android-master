package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import org.shipp.util.MenuEventController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReportDetailsActivity extends Activity {

	Connection conn;
	private boolean open = false;
	private final Context context = this;
	private ListView listMenu;
	private RelativeLayout layout;
	String num1, qatarid;
	static String email;
	String issueid;
	String spid;
	String date;
	static String comm;
	String locx;
	MyApplication app;
	String locy;
	String lang = "";
	String countid;
	String affqatarid;
	String id_rep;
	int id_rep1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lang = Actions.setLocal(this);
		//TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
		setContentView(R.layout.reportdetailspg);
		Actions.loadMainBar(this);
		app = (MyApplication) getApplicationContext();
		// app.face=Typeface.createFromAsset(getAssets(), "fonts/tahomabd.ttf");
		final Bundle mbundle = this.getIntent().getExtras();
		Log.d("add favorite", "passed here");
		final TextView num = (TextView) findViewById(R.id.fil_num_edit_rep);
		final TextView email = (TextView) findViewById(R.id.fill_email_edit_rep);
		TextView issue = (TextView) findViewById(R.id.spinCountry_rep);
		TextView comm = (TextView) findViewById(R.id.comment_edit_rep);
		TextView sp = (TextView) findViewById(R.id.spinSP_rep);
		TextView num_lab = (TextView) findViewById(R.id.textView2);
		TextView email_lab = (TextView) findViewById(R.id.textView21);
		TextView issue_lab = (TextView) findViewById(R.id.textView4);
		TextView comm_lab = (TextView) findViewById(R.id.textView41);
		TextView qatarid_lab = (TextView) findViewById(R.id.textView42);
		Typeface tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
		num_lab.setTypeface(tf);
		email_lab.setTypeface(tf);
		issue_lab.setTypeface(tf);
		comm_lab.setTypeface(tf);
		qatarid_lab.setTypeface(tf);
		num.setTypeface(tf);
		email.setTypeface(tf);
		issue.setTypeface(tf);
		comm.setTypeface(tf);
		// qatarid.setTypeface(tf);
		sp.setTypeface(tf);
		ViewResizing.setPageTextResizing(this);
		listMenu = (ListView) findViewById(R.id.listMenu1);
		this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
		// final String [] values = get_values();//{"hii","hii1","hii2"};
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(this,R.layout.aligned_right, values);
		// this.listMenu.setAdapter(adapter);
		/*
		 * this.listMenu.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * final int position, long id) {
		 * 
		 * if(position==0 || position ==10){
		 * 
		 * } else{ Intent intent = new Intent(getApplicationContext(),
		 * reportActivity.class); //use Bundle to send store data to be send to
		 * StockListActivity Bundle bundle=new Bundle(); Event[] a =
		 * get_values_all(); Event event=null;// = a[position-1];
		 * if(position>9){ event = a[position-2]; } else{ event = a[position-1];
		 * }
		 * 
		 * // Event event=(Event)listMenu.getItemAtPosition(position);
		 * bundle.putInt("eventId", event.getId());
		 * bundle.putString("eventName", event.getName());
		 * bundle.putString("eventDescription", event.getDescription());
		 * bundle.putString("eventDate",event.getDate());
		 * bundle.putString("caller", "eventslist");
		 * bundle.putString("eventLocation", event.getLocation());
		 * bundle.putString("eventImageUrl", event.getImageUrl());
		 * bundle.putString("activity", "EventsListActivity"); //adding the
		 * bundle to the intent intent.putExtras(bundle); startActivity(intent);
		 * } } });
		 */

		num.setText(mbundle.getString("mobilenum"));
		email.setText(mbundle.getString("email"));
		issue.setText(mbundle.getString("issue"));
		comm.setText(mbundle.getString("comments"));
		sp.setText(mbundle.getString("spname"));

		this.num1 = mbundle.getString("mobilenum");
		this.qatarid = mbundle.getString("qatariID");
		this.email = mbundle.getString("email");
		this.issueid = mbundle.getString("issueid");
		this.spid = mbundle.getString("sp");
		this.date = mbundle.getString("date");
		this.comm = mbundle.getString("comments");
		this.locx = mbundle.getString("locx");
		this.locy = mbundle.getString("locy");
		this.countid = mbundle.getString("countrycode");
		this.id_rep = mbundle.getString("idrep1");
		this.id_rep1 = mbundle.getInt("idrep");
		// Launching mLaunching=new Launching();
		// mLaunching.execute();

		Button can;
		if (lang.equals(MyApplication.ENGLISH)) {
			can = (Button) findViewById(R.id.cancel_button_rep_detail);
		} else {
			can = (Button) findViewById(R.id.send_button_rep_detail);
		}

		can.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReportDetailsActivity.this.finish();
			}
		});
	}

	public void goToSettings(View v) {
		Actions.goToSettings(this);
	}

	public void backTo(View v) {
		Actions.backTo(this);
	}

	public String getnum() {
		String a = "";

		try {

			URL url = new URL(
					""
							+ app.link
							+ "GeneralServices.asmx/GetSMSGateway?language=en&password=");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("string");
			Element nameElement = (Element) nodeList.item(0);
			nodeList = nameElement.getChildNodes();
			a = ((Node) nodeList.item(0)).getNodeValue();
			/** Assign textview array lenght by arraylist size */
			// name = new TextView[nodeList.getLength()];
			// website = new TextView[nodeList.getLength()];
			// category = new TextView[nodeList.getLength()];

		} catch (Exception e) {
			System.out.println("gg " + e);
			a = "-1";
		}

		return a;
	}

	public String splitres(String res) {
		String[] a = res.split(">");
		String[] b = a[2].split("</");
		return b[0];
	}

//	public void doSomethingUseful() {
//		try {
//			int k = 0;
//			TCTDbAdapter source = new TCTDbAdapter(ReportDetailsActivity.this);
//			source.open();
//
//			conn = new Connection("" + app.link + "PostData.asmx/SendReport");
//			String s = conn.executeMultipartPost_event("" + app.link
//					+ "PostData.asmx/SendReport3", "", this.num1, this.num1,
//					qatarid, email, issueid, spid, "1", date, date, comm, locx,
//					locy, "165", qatarid, "6", "", "", "false", "", "1/1/1900",
//					"", "1/1/1900", "false", "false", "", "1", "0", "", "");
//			String m = splitres(s);
//			try {
//				k = Integer.parseInt(m);
//			} catch (Exception e) {
//				k = 0;
//			}
//
//			if (k != 0) {
//				try {
//					// boolean a = source.deleteMail_off(date);
//
//					source.updateReportt("sended", id_rep1);
//					// long ln = source.createReports_off(num, num, email,
//					// qatarid, "English", comm, "Beirut-Lebanon",
//					// date,date,nn[i].getissuename(),"sended",nn[i].getissueid(),nn[i].getspid(),nn[i].getlocx(),nn[i].getlocy(),nn[i].getcountid(),nn[i].getaffecqatarid());
//					Intent intent = new Intent(ReportDetailsActivity.this,
//							ReportsListActivity.class);
//
//					startActivity(intent);
//					// if (!(ln > -1)) {// case row not inserted, may be
//					// duplicated
//					// throw new Exception();
//					// }
//
//					// case saved
//					// Actions.onCreateDialog(this,
//					// "Event saved successfully.",false);
//
//				} catch (Exception e) {// case not saved
//					// Actions.onCreateDialog(this,
//					// "Event had been previously saved.",false)
//					// ;
//					System.out.println("d");
//					String url1 = "" + app.link
//							+ "PostData.asmx/VerifyRegistration";
//					Connection conn = new com.ids.ict.classes.Connection(url1);
//
//					try {
//						String error_return = conn
//								.executeMultipartPost_Send_Error(this
//										.getClass().getSimpleName(), Actions
//										.getDeviceName(), "1", e.getMessage());
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
//			}
//			source.close();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			String url1 = "" + app.link + "PostData.asmx/VerifyRegistration";
//			Connection conn = new com.ids.ict.classes.Connection(url1);
//
//			try {
//				String error_return = conn.executeMultipartPost_Send_Error(this
//						.getClass().getSimpleName(), Actions.getDeviceName(),
//						"1", e.getMessage());
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//
//	}

	public String[] get_values() {

		int k = 1;
		TCTDbAdapter source = new TCTDbAdapter(this);
		source.open();
		// source.de
		ArrayList<Event> arr1 = source.getissue_Type("1");
		ArrayList<Event> arr2 = source.getissue_Type("2");
		source.close();
		String[] values = new String[arr1.size() + arr2.size() + 2];
		if (arr1.size() > 0) {
			Event[] n = arr1.toArray(new Event[arr1.size()]);
			values[0] = "Mobile";

			for (int i = 0; i < n.length; i++) {
				values[i + 1] = n[i].getName();
				k++;
			}
		}

		// ArrayList<Event> arr2 = source.getissue_Type("2");
		values[k] = "Fixed Line";
		if (arr2.size() > 0) {
			Event[] n1 = arr2.toArray(new Event[arr2.size()]);

			for (int i = 0; i < n1.length; i++) {
				values[k + 1] = n1[i].getName();
				k++;
			}
		}

		return values;
	}

	public Event[] get_values_all() {
		Event[] n = null;
		Event[] n1 = null;
		int k = 0;
		TCTDbAdapter source = new TCTDbAdapter(this);
		source.open();
		ArrayList<Event> arr1 = source.getissue_Type("1");
		ArrayList<Event> arr2 = source.getissue_Type("2");
		Event[] values = new Event[arr1.size() + arr2.size() + 2];
		if (arr1.size() > 0) {
			n = arr1.toArray(new Event[arr1.size()]);
			// values[0] = "Mobile";
		}
		if (arr2.size() > 0) {
			n1 = arr2.toArray(new Event[arr2.size()]);
			// values[0] = "Mobile";
		}

		for (int i = 0; i < n.length; i++) {
			values[i] = n[i];
			k++;
		}
		for (int i = 0; i < n1.length; i++) {
			values[k] = n1[i];
			k++;
		}
		// ArrayList<Event> arr2 = source.getissue_Type("2");

		return values;
	}

	public void openCloseMenu(View view) {
		if (!this.open) {
			this.open = true;
			MenuEventController.open(this.context, this.layout);
			MenuEventController.closeKeyboard(this.context, view);
		} else {
			this.open = false;
			MenuEventController.close(this.context, this.layout);
			MenuEventController.closeKeyboard(this.context, view);
		}
	}

	protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
		com.ids.ict.Error error;
		Event[] nn, nn1, nn2;

		@Override
		protected void onPreExecute() {

			// mProgressBar.setVisibility(View.VISIBLE);
			// mProgressBar.bringToFront();

		}

		@Override
		protected Integer doInBackground(Void... params) {
			Log.d("launching", "passed here");
			String eventsSource = "" + app.link
					+ "GeneralServices.asmx/GetIssueTypes?";

			AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);
			try {
				String lan;
				eventsSource = eventsSource
						+ "language=en&password=&mainIssueTypeId=1";
				Log.d("eventsource", eventsSource);
				error = Actions.readEvents(ref, eventsSource);
				nn2 = ref.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
			try {
				String lan;

				eventsSource = ""
						+ app.link
						+ "GeneralServices.asmx/GetIssueTypes?language=en&password=&mainIssueTypeId=2";
				Log.d("eventsource", eventsSource);
				error = Actions.readEvents(ref, eventsSource);
				nn1 = ref.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			nn = new Event[nn1.length + nn2.length + 2];
			int k = 0;
			for (int i = 0; i < nn2.length; i++) {
				nn[i] = nn2[i];
				k++;
			}
			for (int i = 0; i < nn1.length; i++) {
				nn[k] = nn1[i];
				k++;
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// if(error.getState()){
			// Actions.onCreateDialog(EventsListActivity.this,
			// error.getMessage(),false);
			// }
			// else{
			// EventsListArrayAdapterMenu adapter = new
			// EventsListArrayAdapterMenu( ReportDetailsActivity.this ,nn);
			// listMenu.setAdapter(adapter);
			// }
			// mProgressBar.setVisibility(View.GONE);
		}

	}

	/*
	 * protected class Launching extends AsyncTask<Void,Void,Integer>{
	 * 
	 * @Override protected void onPreExecute(){ // ImageView
	 * mv=(ImageView)findViewById(R.idr.img); // mv.setVisibility(View.VISIBLE);
	 * }
	 * 
	 * @Override protected Integer doInBackground(Void... a){
	 * Actions.setScreenWidthHeight(CodeActivity.this);
	 * 
	 * Intent intent=new Intent(CodeActivity.this,MnActivity.class); //
	 * intent.putExtra("footerButton", R.id.paper);
	 * 
	 * startActivity(intent);
	 * 
	 * return 1;
	 * 
	 * }
	 */
	protected class LaunchingEvent1 extends AsyncTask<Void, Void, Integer> {
		com.ids.ict.Error error;
		Event[] nn, nn1, nn2;

		@Override
		protected void onPreExecute() {

			// mProgressBar.setVisibility(View.VISIBLE);
			// mProgressBar.bringToFront();

		}

		@Override
		protected Integer doInBackground(Void... params) {
			Log.d("launching", "passed here");
			TCTDbAdapter source = new TCTDbAdapter(ReportDetailsActivity.this);
			source.open();
			AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);

			String lan;
			String eventsSource = "" + app.link
					+ "GeneralServices.asmx/GetIssueTypes?";
			eventsSource = eventsSource
					+ "language=en&password=&mainIssueTypeId=1";
			Log.d("eventsource", eventsSource);

			// error=Actions.readEvents(ref,eventsSource);
			// nn2 = ref.get();

			AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
			// String lan;

			eventsSource = ""
					+ app.link
					+ "GeneralServices.asmx/GetIssueTypes?language=en&password=&mainIssueTypeId=2";
			Log.d("eventsource", eventsSource);
			// error=Actions.readEvents(ref,eventsSource);
			// nn1 = ref.get();

			ArrayList<Event> arr = source.getissue_Type("2");
			nn2 = arr.toArray(new Event[arr.size()]);

			ArrayList<Event> arr1 = source.getissue_Type("1");
			nn1 = arr1.toArray(new Event[arr1.size()]);
			source.close();

			nn = new Event[nn1.length + nn2.length + 2];
			int k = 0;
			for (int i = 0; i < nn1.length; i++) {
				nn[i] = nn1[i];
				k++;
			}
			for (int i = 0; i < nn2.length; i++) {
				nn[k] = nn2[i];
				k++;
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// if(error.getState()){
			// Actions.onCreateDialog(EventsListActivity.this,
			// error.getMessage(),false);
			// }
			// else{
			// EventsListArrayAdapterMenu adapter = new
			// EventsListArrayAdapterMenu( ReportDetailsActivity.this ,nn);
			// listMenu.setAdapter(adapter);
			// }
			// mProgressBar.setVisibility(View.GONE);
		}

	}
}
