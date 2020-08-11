package com.ids.ict.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.ReportDetailsActivity;

import org.shipp.util.MenuEventController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReportsListActivity extends Activity {
	final String eventsSource = "http://72.167.40.209/indyact/IndyAct.WebServices/eventservices.asmx/GetEventsListing?top=10";
	ArrayList<Mail_OFF> events;
	ListView eventList;
	ProgressBar mProgressBar;
	String lang = "";
	boolean launching;
	private boolean open = false;
	private final Context context = this;
	private ListView listMenu;
	private RelativeLayout layout;
	// the footer button corresponding to this activity
	int footerButton;

	MyApplication app;
	private boolean isInFront;
	View mainBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lang = Actions.setLocal(this);
		setContentView(R.layout.reportslist);
		Actions.loadMainBar(this);
		ViewResizing.setPageTextResizing(this);
		// footerButton= this.getIntent().getIntExtra("footerButton",
		// R.id.home);
		// Actions.setFooterButtonState(this,footerButton);

		footerButton = this.getIntent().getIntExtra("footerButton", R.id.home);
		ScheduledExecutorService scheduleTaskExecutor = Executors
				.newScheduledThreadPool(5);

		// This schedule a runnable task every 2 minutes
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				TCTDbAdapter datasource = new TCTDbAdapter(
						ReportsListActivity.this);
				datasource.open();
				ArrayList<Profile> arr11 = datasource.getAllProfiles();
				datasource.close();
				if (arr11.size() > 0) {
					if (isNetworkAvailable() && isInFront) {
						doSomethingUseful();

						// adapter.notifyDataSetChanged();
					}

				}
			}

		}, 0, 3, TimeUnit.SECONDS);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// adapter.notifyDataSetChanged();
				handler.postDelayed(this, 4000);
			}
		}, 60 * 1000);

		startMyActivity();

	}

	public void goToSettings(View v) {
		Actions.goToSettings(this);
	}

	public void doSomethingUseful() {
		try {
			Connection conn;
			int k = 0;
			TCTDbAdapter source = new TCTDbAdapter(ReportsListActivity.this);
			source.open();
			ArrayList<Mail_OFF> arr = source.getAllReports_off();
			Mail_OFF[] nn;
			conn = new Connection("" + app.link + "PostData.asmx/SendReport");
			nn = new Mail_OFF[arr.size()];
			nn = arr.toArray(nn);
			for (int i = 0; i < nn.length; i++) {
				if (!nn[i].getstatus().equals("sended")) {
					String gettok = gettoken(nn[i].getId());
					String s = conn.executeMultipartPost_event("" + app.link
							+ "PostData.asmx/SendReport3", gettok,
							nn[i].getnum(), nn[i].getnum(), nn[i].getqatarID(),
							nn[i].getemail(), nn[i].getissueid(),
							nn[i].getspid(), "1", nn[i].getdate(),
							nn[i].getdate(), nn[i].getcomm(), nn[i].getlocx(),
							nn[i].getlocy(), "165", nn[i].getaffecqatarid(),
							"6", "", "", "false", "", "1/1/1900", "",
							"1/1/1900", "false", "false", "", "1", "0",
							"false", "","","0","0","","","","");
					String m = splitres(s);
					try {
						k = Integer.parseInt(m);
					} catch (Exception e) {
						k = 0;
					}

					if (k != 0) {
						try {
							boolean a = source.deleteMail_offf(nn[i]
									.getid_rep());
							String locc = getcountrycode1(
									Double.parseDouble(nn[i].getlocx()),
									Double.parseDouble(nn[i].getlocy()));
							long ln = source.createReports_off(nn[i].getId(),
									nn[i].getnum(), nn[i].getemail(),
									nn[i].getqatarID(), nn[i].getlang(),
									nn[i].getcomm(), locc, nn[i].getdate(),
									nn[i].getdate_aff(), nn[i].getissuename(),
									"sended", nn[i].getissueid(),
									nn[i].getspid(), nn[i].getlocx(),
									nn[i].getlocy(), nn[i].getcountid(),
									nn[i].getaffecqatarid(), nn[i].getspname());
							LaunchingEvent eventLaunching = new LaunchingEvent();
							eventLaunching.execute();
							if (!(ln > -1)) {// case row not inserted, may be
												// duplicated
								throw new Exception();
							}

							// case saved
							// Actions.onCreateDialog(this,
							// "Event saved successfully.",false);

						} catch (Exception e) {// case not saved
							// Actions.onCreateDialog(this,
							// "Event had been previously saved.",false)
							// ;
							System.out.println("d");
						}
					}

				}

			}
			source.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getcountrycode1(double locx, double locy) {
		String countryName = "";
		Geocoder gcd = new Geocoder(this, Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(locx, locy, 1);
			if (addresses.size() > 0) {
				// countryName=addresses.get(0).getCountryName();
				countryName = addresses.get(0).getAddressLine(0) + " "
						+ addresses.get(0).getAddressLine(1) + " "
						+ addresses.get(0).getAdminArea() + " "
						+ addresses.get(0).getSubAdminArea();
				countryName = countryName.replaceAll("null", "");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Qatar";
		}

		return countryName;
	}

	public String splitres(String res) {
		try {
			String[] a = res.split(">");
			String[] b = a[2].split("</");
			return b[0];
		} catch (Exception e) {
			return "";
		}
	}

	public String gettoken(String pass) {
		String name = "";
		try {

			URL url = new URL("" + app.link
					+ "PostData.asmx/StartSendingReport?password=" + pass);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("string");
			Element nameElement = (Element) nodeList.item(0);
			nodeList = nameElement.getChildNodes();
			name = ((Node) nodeList.item(0)).getNodeValue();
			/** Assign textview array lenght by arraylist size */
			// name = new TextView[nodeList.getLength()];
			// website = new TextView[nodeList.getLength()];
			// category = new TextView[nodeList.getLength()];

		} catch (Exception e) {
			System.out.println("gg " + e);
		}
		return name;
	}

	@Override
	public void onResume() {
		super.onResume();
		isInFront = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		isInFront = false;
	}

	public void backTo(View v) {
		Actions.backTo(this);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

	private void startMyActivity() {

		initializeViews();
		// footer button set checked state
		launching = true;
		eventsList();
		LaunchingEvent eventLaunching = new LaunchingEvent();
		eventLaunching.execute();
	}

	private void initializeViews() {
		app = (MyApplication) getApplicationContext();
		launching = true;
		mainBar = (View) findViewById(R.id.main_bar);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar1);
		final View mainBar = findViewById(R.id.main_bar);
		// ViewResizing.setHeightViewResizingListener(this, mainBar);
		final View footerBar = findViewById(R.id.footer);
		// ViewResizing.setHeightViewResizingListener(this, footerBar);
	}

	private void eventsList() {
		// newslist
		eventList = (ListView) findViewById(R.id.reports_list);

		eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (lang.equals(MyApplication.ENGLISH)) {
					Intent intent = new Intent(getApplicationContext(),
							ReportDetailsActivity.class);
					// use Bundle to send store data to be send to
					// StockListActivity
					Bundle bundle = new Bundle();
					Mail_OFF event = (Mail_OFF) eventList
							.getItemAtPosition(position);

					bundle.putString("mobilenum", event.getnum());
					bundle.putString("email", event.getemail());
					bundle.putString("qatariID", event.getqatarID());
					bundle.putString("comments", event.getcomm());
					bundle.putString("date", event.getdate());
					String eng_name = "";
					if (event.getlang().equals("Arabic")) {
						TCTDbAdapter source = new TCTDbAdapter(context);
						source.open();

						eng_name = source.getissue_det_name_en(""
								+ event.getissueid());

						source.close();
					} else {
						eng_name = event.getissuename();
					}
					bundle.putString("issue", eng_name);
					bundle.putString("sp", event.getspid());
					bundle.putString("spname", event.getspname());
					bundle.putString("status", event.getstatus());
					bundle.putString("issueid", event.getissueid());
					bundle.putString("locx", event.getlocx());
					bundle.putString("locy", event.getlocy());
					bundle.putString("countrycode", event.getcountid());
					bundle.putString("affid", event.getaffecqatarid());
					bundle.putString("idrep1", event.getId());
					bundle.putInt("idrep", event.getid_rep());
					// adding the bundle to the intent
					intent.putExtras(bundle);
					startActivity(intent);

				} else {
					Intent intent = new Intent(getApplicationContext(),
							ReportDetailsActivity.class);
					// use Bundle to send store data to be send to
					// StockListActivity
					Bundle bundle = new Bundle();
					Mail_OFF event = (Mail_OFF) eventList
							.getItemAtPosition(position);

					bundle.putString("mobilenum", event.getnum());
					bundle.putString("email", event.getemail());
					bundle.putString("qatariID", event.getqatarID());
					bundle.putString("comments", event.getcomm());
					bundle.putString("date", event.getdate());
					String eng_name = "";
					if (event.getlang().equals("English")) {
						TCTDbAdapter source = new TCTDbAdapter(context);
						source.open();

						eng_name = source.getissue_det_name_ar(""
								+ event.getissueid());

						source.close();
					} else {
						eng_name = event.getissuename();
					}
					bundle.putString("issue", eng_name);
					// bundle.putString("issue", event.getissuename());
					bundle.putString("sp", event.getspid());
					bundle.putString("spname", event.getspname());
					bundle.putString("status", event.getstatus());
					bundle.putString("issueid", event.getissueid());
					bundle.putString("locx", event.getlocx());
					bundle.putString("locy", event.getlocy());
					bundle.putString("countrycode", event.getcountid());
					bundle.putString("affid", event.getaffecqatarid());
					bundle.putInt("idrep", event.getid_rep());
					bundle.putString("idrep1", event.getId());
					// adding the bundle to the intent
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

	}

	protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
		Error error;
		Mail_OFF[] nn;

		@Override
		protected void onPreExecute() {

			mProgressBar.setVisibility(View.GONE);

		}

		@Override
		protected Integer doInBackground(Void... params) {
			Log.d("launching", "passed here");
			AtomicReference<Mail_OFF[]> ref = new AtomicReference<Mail_OFF[]>(
					null);
			TCTDbAdapter source = new TCTDbAdapter(ReportsListActivity.this);
			source.open();
			ArrayList<Mail_OFF> arr = source.getAllReports_off();
			nn = new Mail_OFF[arr.size()];
			nn = arr.toArray(nn);
			// nn = (Mail_OFF[]) arr.toArray();

			// nn = ref.get();
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (lang.equals(MyApplication.ENGLISH)) {
				ReportsListArrayAdapter adapter = new ReportsListArrayAdapter(
						ReportsListActivity.this, nn);
				eventList.setAdapter(adapter);
			} else {
				ArReportsListArrayAdapter adapter = new ArReportsListArrayAdapter(
						ReportsListActivity.this, nn);
				eventList.setAdapter(adapter);
			}
			// }
			// mProgressBar.setVisibility(View.GONE);
		}

	}

	public void search(View v) {
		onSearchRequested();
	}
}
