package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.NotificationListArrayAdapter;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.ParserConfigurationException;

public class NotificationListActivity extends Activity {
    ArrayList<Event> events;
    ListView eventList;
    boolean launching;
    int issue_id = 0;
    Bundle bundle;
    String lang;
    int footerButton;
    ArrayList<Notifications> notifications = new ArrayList<Notifications>();
    MyApplication app;

    // View mainBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.notificationlist);
        Actions.loadMainBar(this);

        ViewResizing.setPageTextResizing(this);
        TCTDbAdapter datasoure = new TCTDbAdapter(this);
        datasoure.open();
        app = (MyApplication) getApplicationContext();
        eventList = (ListView) findViewById(R.id.events_list);
        eventList.setSelector(R.drawable.transperent_selector);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            eventList.setBackgroundColor(getResources().getColor(R.color.nightBlue));
        }

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        }

        ArrayList<Profile> arr = datasoure.getAllProfiles();

        startMyActivity();

    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public void startMyActivity() {

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
        final View mainBar = findViewById(R.id.main_bar);
        final View footerBar = findViewById(R.id.footer);
    }

    private void eventsList() {
        // newslist

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                if (lang.equals(MyApplication.ENGLISH))
                    intent = new Intent(getApplicationContext(),
                            NotificationDetailsActivity.class);
                else
                    intent = new Intent(getApplicationContext(),
                            NotificationDetailsActivity.class);
                Bundle bundle = new Bundle();
                Notifications event = (Notifications) eventList
                        .getItemAtPosition(position);
                bundle.putInt("eventId", event.getId());
                bundle.putString("notificationName", event.getName());
                bundle.putString("notificationDescription",
                        event.getDescription());
                bundle.putString("notificationDate", event.getDate());

                // adding the bundle to the intent
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {

        com.ids.ict.Error error;
        Notifications[] nn;
        String dbLang = "";
        TCTDbAdapter source;

        @Override
        protected void onPreExecute() {

            if (lang.equals(MyApplication.ENGLISH))
                dbLang = "English";
            else
                dbLang = "Arabic";

            source = new TCTDbAdapter( NotificationListActivity.this);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("launching", "passed here");
            AtomicReference<Notifications[]> ref = new AtomicReference<Notifications[]>(
                    null);
            if (isNetworkAvailable()) {
                try {
                    String lan;

                    String eventsSource = MyApplication.link + MyApplication.general + "GetNews?language=" + dbLang + "&password=" + app.pass;

                    Log.d("eventsource", eventsSource);
                    //error = Actions.readNotification(ref, eventsSource);
                    //nn = ref.get();


                    notifications = Actions.ReadNews(eventsSource);

                    source.open();


                    source.deleteAllNotifications("");

                    for (int i = 0; i < notifications.size(); i++) {
                        source.createissue_notification(notifications.get(i).getId(),
                                notifications.get(i).getName(),notifications.get(i).getDescription(),
                                notifications.get(i).getDate(), dbLang);
                    }

                    // }
                    source.close();
                    return 1;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return 0;
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return 0;
                } catch (Exception e) {
                    return 0;
                }

            } else {

                source.open();
                ArrayList<Notifications> arr = source.getnotification(dbLang);
                source.close();
                if (arr.size() == 0) {
                    return 0;
                } else {
                    notifications.addAll(arr);
                    return 1;
                }
            }

        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                NotificationListArrayAdapter adapter = new NotificationListArrayAdapter(
                        NotificationListActivity.this, notifications);
                eventList.setAdapter(adapter);
            }
        }

    }

    public void topBarBack(View v) {
        NotificationListActivity.this.finish();
    }

    public void search(View v) {
        onSearchRequested();
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(NotificationListActivity.this, MoreActivity.class);
                NotificationListActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {
                intent.setClass(NotificationListActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                NotificationListActivity.this.startActivity(intent);
                NotificationListActivity.this.finish();
                break;
            }

        }
    }

}
