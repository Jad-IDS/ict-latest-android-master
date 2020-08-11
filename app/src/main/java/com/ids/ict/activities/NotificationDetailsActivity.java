package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import org.shipp.util.MenuEventController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class NotificationDetailsActivity extends Activity {

    Connection conn;
    private boolean open = false;
    AtomicReference<Integer> counterRef;
    private final Context context = this;
    private ListView listMenu;
    MyApplication app;
    private RelativeLayout layout;
    String num, qatarid, email, issueid, spid, date, comm, locx, locy, countid,
            affqatarid;

    private WebView wvDetials;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
       // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.news_details);
        ViewResizing.setPageTextResizing(this);
        Actions.loadMainBar(this);
        app = (MyApplication) getApplicationContext();
        TextView title = (TextView) findViewById(R.id.news_title);
        TextView date = (TextView) findViewById(R.id.news_date);
        TextView detail = (TextView) findViewById(R.id.news_details);
        wvDetials = (WebView) findViewById(R.id.wvDetials);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            final ScrollView scroll = (ScrollView) findViewById(R.id.ScrollView01);
            scroll.setBackgroundColor(getResources()
                    .getColor(R.color.nightBlue));
            date.setTextColor(getResources().getColor(R.color.white));
            title.setTextColor(getResources().getColor(R.color.white));
        }

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        }

        counterRef = new AtomicReference<Integer>();
        counterRef.set(0);
        final Bundle mbundle = this.getIntent().getExtras();
        Log.d("add favorite", "passed here");
        title.setText(mbundle.getString("notificationName"));

        String notificationDate = mbundle.getString("notificationDate");

        date.setText(notificationDate);//.substring(0,notificationDate.indexOf("T")));
        date.setTypeface(MyApplication.facePolarisMedium);
        detail.setText(Html.fromHtml(mbundle
                .getString("notificationDescription")));

        wvDetials.setHorizontalScrollBarEnabled(true);
        wvDetials.setVerticalScrollBarEnabled(true);
        wvDetials.setBackgroundColor(0x00000000);
        wvDetials.getSettings().setJavaScriptEnabled(true);
        Actions.LoadWebViewWithCustomFontNotifications(mbundle.getString("notificationDescription"), wvDetials);
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public void onToggle(View v) {
        Actions.detailsFooter(v, this, counterRef);
    }

    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
    }

//	public void doSomethinsgUseful() {
//		try {
//			int k = 0;
//			TCTDbAdapter source = new TCTDbAdapter(
//					NotificationDetailsActivity.this);
//			source.open();
//
//			conn = new Connection("" + app.link + "PostData.asmx/SendReport");
//			String s = conn
//					.executeMultipartPost_event("" + app.link
//							+ "PostData.asmx/SendReport3", "", this.num,
//							this.num, qatarid, email, issueid, spid, "1", date,
//							date, comm, locx, locy, "165", qatarid, "6", "",
//							"", "false", "", "1/1/1900", "", "1/1/1900",
//							"false", "false", "", "1", "0", "false", "");
//
//			String m = splitres(s);
//			try {
//				k = Integer.parseInt(m);
//			} catch (Exception e) {
//				k = 0;
//			}
//
//			if (k != 0) {
//				try {
//					Intent intent = new Intent(
//							NotificationDetailsActivity.this,
//							ReportsListActivity.class);
//
//					startActivity(intent);
//				} catch (Exception e) {// case not saved
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
        }
        if (arr2.size() > 0) {
            n1 = arr2.toArray(new Event[arr2.size()]);
        }

        for (int i = 0; i < n.length; i++) {
            values[i] = n[i];
            k++;
        }
        for (int i = 0; i < n1.length; i++) {
            values[k] = n1[i];
            k++;
        }

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
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            TCTDbAdapter source = new TCTDbAdapter(
                    NotificationDetailsActivity.this);
            source.open();
            AtomicReference<Event[]> ref = new AtomicReference<Event[]>(null);

            String lan;
            String eventsSource = "" + app.link
                    + "GeneralServices.asmx/GetIssueTypes?";
            eventsSource = eventsSource + "language=en&password="
                    + "+app.pass+" + "&mainIssueTypeId=1";
            Log.d("eventsource", eventsSource);

            AtomicReference<Event[]> ref1 = new AtomicReference<Event[]>(null);
            // String lan;

            eventsSource = ""
                    + app.link
                    + "GeneralServices.asmx/GetIssueTypes?language=en&password="
                    + "+app.pass+" + "&mainIssueTypeId=2";
            Log.d("eventsource", eventsSource);

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
        }

    }

    public void topBarBack(View v) {
        NotificationDetailsActivity.this.finish();
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(NotificationDetailsActivity.this,
                        MoreActivity.class);
                NotificationDetailsActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(NotificationDetailsActivity.this,
                        HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                NotificationDetailsActivity.this.startActivity(intent);
                NotificationDetailsActivity.this.finish();
                break;
            }

        }
    }

}
