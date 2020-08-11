package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.ArEventsListArrayAdapter;
import com.ids.ict.classes.ArEventsListArrayAdapter_off;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.EventsListArrayAdapter;
import com.ids.ict.classes.EventsListArrayAdapter_off;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.adapters.TicketArrayAdapter;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.Ticket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ComplaintActivity extends Activity {

    ListView eventList;
    Bundle bundle;
    String eventsSource = "", IssueType;
    String lang, category;
    ArrayList<Event> events;
    ArrayList<Ticket> ticketarray = new ArrayList<Ticket>();
    ArrayList<Ticket> filteredTicket = new ArrayList<Ticket>();
    int issue_id = 0;
    Typeface tf;
    Button complaint, inquiry, tickets;
    protected Button close, open;
    int tickettype = 1;// 1 for open 2 for closed 3 for mobile
    TicketArrayAdapter adapterticket;
    public String issuenamear;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        // TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.activity_complaint);
        //ViewResizing.setPageTextResizing(this);

        eventsSource = "" + MyApplication.link + MyApplication.general + "GetIssueTypes?";
        eventList = (ListView) findViewById(R.id.events_list);
        eventList.setSelector(R.drawable.transperent_selector);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final RelativeLayout ll = (RelativeLayout) findViewById(R.id.mainlayout);
            ll.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
        }

        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            tf = MyApplication.facePolarisMedium;
            buttop.setImageResource(R.drawable.back_btn_en);
        } else {
            tf = MyApplication.faceDinar;
        }

        final TextView titlebar = (TextView) findViewById(R.id.title);
        titlebar.setVisibility(View.GONE);
        final ImageView logoBAr = (ImageView) findViewById(R.id.minlogo_button);
        logoBAr.setVisibility(View.VISIBLE);

        final LinearLayout lineBtnLayout = (LinearLayout) findViewById(R.id.lineSelectLayout);
        final LinearLayout barLayout = (LinearLayout) findViewById(R.id.topBarLayout);
        final LinearLayout tickettop = (LinearLayout) findViewById(R.id.tickettop);
        final LinearLayout headertitleticket = (LinearLayout) findViewById(R.id.headerticket);
        final TextView categorytv = (TextView) findViewById(R.id.category);
        categorytv.setTypeface(tf);
        final TextView sendingdatetv = (TextView) findViewById(R.id.sendingdate);
        sendingdatetv.setTypeface(tf);
        final TextView cratv = (TextView) findViewById(R.id.cra);
        cratv.setTypeface(tf);
        final TextView updatedatetv = (TextView) findViewById(R.id.updatedate);
        updatedatetv.setTypeface(tf);

        complaint = (Button) findViewById(R.id.compBtn);
        complaint.setTypeface(tf);
        inquiry = (Button) findViewById(R.id.inqBtn);
        inquiry.setTypeface(tf);
        tickets = (Button) findViewById(R.id.ticketBtn);
        tickets.setTypeface(tf);

        final Button mob = (Button) findViewById(R.id.mob_button);
        mob.setText(getResources().getString(R.string.mobile));
        mob.setTypeface(tf);
        final Button fixed = (Button) findViewById(R.id.fixed_button);
        fixed.setText(getResources().getString(R.string.phone));
        fixed.setTypeface(tf);
        setTextButton();
        ticketarray = new ArrayList<Ticket>();
        filteredTicket = new ArrayList<Ticket>();

        if (Actions.isNetworkAvailable(ComplaintActivity.this)) {
            getTickets get = new getTickets(false);
            get.execute();
        }

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();
                Object objecttt = (Object) eventList.getItemAtPosition(position);

                if (objecttt instanceof Event) {
                    Intent intent = new Intent(getApplicationContext(), ComplainFormActivity.class);
                    Event event = (Event) eventList.getItemAtPosition(position);
                    bundle.putInt("eventId", event.getId());
                    Log.wtf("ComplaintActeventId", ""+event.getId());

                    bundle.putInt("MainIssueId", issue_id);
                    bundle.putString("ServiceProviderTransfer", event.getServiceProviderTransfer());
                    bundle.putString("showOnMap", event.getShowMap());
                    bundle.putString("IssueType", IssueType);
                    bundle.putString("eventCateg", category);

                    Log.wtf("ComplaintActIssueType", ""+IssueType);
                    Log.wtf("ComplainteventCatege", ""+category);

                    bundle.putString("eventName", event.getName());
                    bundle.putString("eventDescription", event.getDescription());
                    bundle.putString("eventDate", event.getDate());

                    bundle.putString("caller", "eventslist");
                    bundle.putString("eventLocation", event.getLocation());
                    bundle.putString("activity", "EventsListActivity");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Ticket event = (Ticket) eventList
                            .getItemAtPosition(position);
                    Intent intent = new Intent(ComplaintActivity.this,
                            SendReportActivity.class);
                    // unused
                    bundle.putString("from", "ticket");
                    bundle.putString("ticket", "1");
                    bundle.putString("gettok", "-1");
                    bundle.putString("qatariID", "");
                    bundle.putString("iniqatarid", "");
                    bundle.putString("password", "");
                    bundle.putString("spid", "");
                    bundle.putString("locx", "" + "");
                    bundle.putString("locy", "");
                    bundle.putString("countryid", "");
                    bundle.putString("date", "");
                    bundle.putString("date", "");
                    bundle.putString("sendingdate", "");
                    bundle.putString("date_aff", "");
                    bundle.putString("eventNameAr", event.getEventNameAr());
                    bundle.putString("eventNameEn",  event.getEventNameEn());

                    bundle.putString("issueid", event.getId() + "");
                    bundle.putString("mobilenum", event.getMobilenumber());
                    bundle.putString("affmobilenum",
                            event.getAffectedMobileNumber());
                    bundle.putString("callNum", event.getCRA());
                    bundle.putString("email", event.getEmail());
                    bundle.putString("comments", event.getComments());
                    bundle.putString("spComplaint", event.getSpComplaintId());
                    bundle.putString("longWait", event.getLongWait());
                    bundle.putString("cmplNum", event.getCmplRefNum());
                    bundle.putString("cmplDate", event.getCmplDate());
                    bundle.putString("calldate", event.getCalldatee());
                    bundle.putString("creationdate", event.getSendingDate());
                    bundle.putString("locat", event.getLocality());
                    if (event.getCRNum().length() > 0) {
                        bundle.putString("isIndividual", "flase");
                        bundle.putString("isCorporate", "true");
                        bundle.putString("CRNumber", event.getCRNum());
                    } else {
                        bundle.putString("isIndividual", "true");
                        bundle.putString("isCorporate", "flase");
                        bundle.putString("CRNumber", "");
                        bundle.putString("iniqatarid",
                                event.getaffectedqatarid());
                    }
                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ENGLISH)) {
                        bundle.putString("issue", event.getIssueDetailNameEn());
                        bundle.putString("sp", event.getServiceProvider());
                        bundle.putString("waitTime", event.getwaitTime());
                    } else {
                        bundle.putString("issue", event.getIssueDetailNameAr());
                        bundle.putString("sp", event.getServiceProviderAr());
                        bundle.putString("waitTime", event.getwaitTimeAr());
                    }
                    // form variables
                    if (event.getType().equals("Inquiry ")) {
                        bundle.putString("IssueType", "2");// inquiry-complaint
                    }
                    bundle.putString("status", "sended");// new
                    // lang
                    // ticket variables
                    bundle.putString("detailNameEn",
                            event.getIssueDetailNameEn());// inquiry-complaint
                    bundle.putString("detailNameAr",
                            event.getIssueDetailNameAr());
                    bundle.putInt("eventId", event.getId());

                    intent.putExtras(bundle);
                    startActivity(intent);
                    //	finish();
                }
            }
        });

        filterTicket();

        adapterticket = new TicketArrayAdapter(ComplaintActivity.this, ticketarray);

        complaint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventList.setAdapter(null);
                tickettop.setVisibility(View.GONE);
                IssueType = "1";
                if (MyApplication.nightMod) {
                    complaint.setTextColor(getResources().getColor(
                            R.color.nightBlue));
                    inquiry.setTextColor(getResources().getColor(R.color.white));
                    tickets.setTextColor(getResources().getColor(R.color.white));
                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_right_nt);
                    } else {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_left_nt);
                    }
                } else {
                    complaint.setTextColor(getResources().getColor(
                            R.color.bordo));
                    inquiry.setTextColor(getResources().getColor(R.color.white));
                    tickets.setTextColor(getResources().getColor(R.color.white));
                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_right);
                    } else {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_left);
                    }
                }
                lineBtnLayout.setVisibility(View.VISIBLE);
                headertitleticket.setVisibility(View.GONE);
                LaunchingEvent eventLaunching = new LaunchingEvent();
                eventLaunching.execute();
            }
        });

        inquiry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventList.setAdapter(null);
                tickettop.setVisibility(View.GONE);
                headertitleticket.setVisibility(View.GONE);
                IssueType = "2";
                if (MyApplication.nightMod) {
                    complaint.setTextColor(getResources().getColor(
                            R.color.white));
                    inquiry.setTextColor(getResources().getColor(
                            R.color.nightBlue));
                    tickets.setTextColor(getResources().getColor(R.color.white));
                    barLayout
                            .setBackgroundResource(R.drawable.comp_tab_center_nt);
                } else {

                    complaint.setTextColor(getResources().getColor(
                            R.color.white));
                    inquiry.setTextColor(getResources().getColor(R.color.bordo));
                    tickets.setTextColor(getResources().getColor(R.color.white));
                    barLayout.setBackgroundResource(R.drawable.comp_tab_center);
                }
                lineBtnLayout.setVisibility(View.VISIBLE);
                LaunchingEvent eventLaunching = new LaunchingEvent();
                eventLaunching.execute();
            }
        });

        tickets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventList.setAdapter(null);
                if (MyApplication.nightMod) {
                    complaint.setTextColor(getResources().getColor(
                            R.color.white));
                    inquiry.setTextColor(getResources().getColor(R.color.white));
                    tickets.setTextColor(getResources().getColor(
                            R.color.nightBlue));
                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_left_nt);
                    } else {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_right_nt);
                    }
                } else {

                    complaint.setTextColor(getResources().getColor(
                            R.color.white));
                    inquiry.setTextColor(getResources().getColor(R.color.white));
                    tickets.setTextColor(getResources().getColor(R.color.bordo));
                    if (MyApplication.Lang
                            .equalsIgnoreCase(MyApplication.ARABIC)) {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_left);
                    } else {
                        barLayout
                                .setBackgroundResource(R.drawable.comp_tab_right);
                    }
                }
                IssueType = "3";
                //tickettop.setVisibility(View.VISIBLE);
                Log.wtf("setLocal","SetLocal");
                Actions.setLocal(ComplaintActivity.this);
                headertitleticket.setVisibility(View.VISIBLE);
                lineBtnLayout.setVisibility(View.GONE);
                setTextViewticketText();
                open = (Button) findViewById(R.id.open);
                open.setTypeface(tf);
                close = (Button) findViewById(R.id.close);
                close.setTypeface(tf);
                open.performClick();
                SharedPreferences prefsEn = getSharedPreferences("PrefEng",
                        Context.MODE_PRIVATE);
                try {
                    if (lang.equals(MyApplication.ENGLISH)) {
                        open.setText(getLookup(prefsEn, "26").nameen);
                        close.setText(getLookup(prefsEn, "28").nameen);
                    } else {
                        open.setText(getLookup(prefsEn, "26").namear);
                        close.setText(getLookup(prefsEn, "28").namear);
                    }
                } catch (Exception e) {
                    open.setText(getResources().getString(R.string.open));
                    close.setText(getResources().getString(R.string.close));
                }
                eventList.setAdapter(adapterticket);
            }
        });

        if (MyApplication.lunched == 0) {// 0 from main -1 from thank you
            // complaint -2 from thank you
            // inquiry
            complaint.performClick();
            MyApplication.lunched++;
        } else if (MyApplication.lunched == -1 || MyApplication.lunched == -2) {
            tickets.performClick();
        }

        mob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.nightMod) {
                    fixed.setBackgroundColor(getResources().getColor(
                            R.color.nightBlue_unselected));
                    mob.setBackgroundColor(getResources().getColor(
                            R.color.nightBlue));
                    fixed.setTextColor(getResources().getColor(R.color.white));
                    mob.setTextColor(getResources().getColor(R.color.white));

                } else {
                    fixed.setBackgroundColor(getResources().getColor(
                            R.color.gray_light));
                    mob.setBackground(getResources().getDrawable(
                            R.drawable.background));

                }
                issue_id = 1;
                category = getResources().getString(R.string.mobile);

                LaunchingEvent eventLaunching = new LaunchingEvent();
                eventLaunching.execute();
            }
        });

        fixed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.nightMod) {
                    fixed.setBackgroundColor(getResources().getColor(
                            R.color.nightBlue));
                    mob.setBackgroundColor(getResources().getColor(
                            R.color.nightBlue_unselected));
                    fixed.setTextColor(getResources().getColor(R.color.white));
                    mob.setTextColor(getResources().getColor(R.color.white));

                } else {

                    fixed.setBackground(getResources().getDrawable(
                            R.drawable.background));
                    mob.setBackgroundColor(getResources().getColor(
                            R.color.gray_light));
                }
                issue_id = 2;
                category = getResources().getString(R.string.phone);
                LaunchingEvent eventLaunching = new LaunchingEvent();
                eventLaunching.execute();
            }
        });

        mob.performClick();
    }

//	@Override
//	protected void onResume() {
//		super.onResume();
//		if(IssueType.equals("3")) {
//			getTickets get = new getTickets(true);
//			get.execute();
//		}
//	}

    public void setTextViewticketText() {
        TextView category = (TextView) findViewById(R.id.category);
        TextView date = (TextView) findViewById(R.id.sendingdate);
        TextView cra = (TextView) findViewById(R.id.cra);
        TextView updatedate = (TextView) findViewById(R.id.updatedate);
        updatedate.setText(getString(R.string.status));
        SharedPreferences prefsEn = getSharedPreferences("PrefEng",
                Context.MODE_PRIVATE);
        try {
            if (lang.equals(MyApplication.ENGLISH)) {
                updatedate.setText(getString(R.string.status));
                category.setText(getLookup(prefsEn, "32").nameen);
                date.setText(getLookup(prefsEn, "31").nameen);
                cra.setText(getLookup(prefsEn, "30").nameen);
                //updatedate.setText(getLookup(prefsEn, "29").nameen);
            } else {
                updatedate.setText(getString(R.string.status));
                category.setText(getLookup(prefsEn, "32").namear);
                date.setText(getLookup(prefsEn, "31").namear);
                cra.setText(getLookup(prefsEn, "30").namear);
                //updatedate.setText(getLookup(prefsEn, "29").namear);
            }
        } catch (Exception e) {

            //category.setText(getResources().getString(R.string.category));
            category.setText(getResources().getString(R.string.category2));
            date.setText(getResources().getString(R.string.date));
            cra.setText(getResources().getString(R.string.cra));
            updatedate.setText(getResources().getString(R.string.updatedate));
        }
    }


    public void open(View v) {
        if (MyApplication.nightMod) {
            open.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            close.setBackgroundColor(getResources().getColor(
                    R.color.nightBlue_unselected));
            close.setTextColor(getResources().getColor(R.color.white));
            open.setTextColor(getResources().getColor(R.color.white));

        } else {
            close.setBackgroundColor(getResources()
                    .getColor(R.color.gray_light));
            open.setBackground(getResources()
                    .getDrawable(R.drawable.background));

        }
        tickettype = 1;
        //filteredTicket.clear();
        filterTicket();
        adapterticket.notifyDataSetChanged();

    }


    public void close(View v) {
        if (MyApplication.nightMod) {
            close.setBackgroundColor(getResources().getColor(R.color.nightBlue));
            open.setBackgroundColor(getResources().getColor(
                    R.color.nightBlue_unselected));
            close.setTextColor(getResources().getColor(R.color.white));
            open.setTextColor(getResources().getColor(R.color.white));

        } else {
            open.setBackgroundColor(getResources().getColor(R.color.gray_light));
            close.setBackground(getResources().getDrawable(
                    R.drawable.background));

        }
        tickettype = 2;
        filteredTicket.clear();
        filterTicket();
        adapterticket.notifyDataSetChanged();
    }


    public void setTextButton() {
        SharedPreferences prefsEn = getSharedPreferences("PrefEng",
                Context.MODE_PRIVATE);
        try {
            if (lang.equals(MyApplication.ENGLISH)) {
                complaint.setText(getLookup(prefsEn, "23").nameen);
                inquiry.setText(getLookup(prefsEn, "24").nameen);
                tickets.setText(getLookup(prefsEn, "25").nameen);
            } else {
                complaint.setText(getLookup(prefsEn, "23").namear);
                inquiry.setText(getLookup(prefsEn, "24").namear);
                tickets.setText(getLookup(prefsEn, "25").namear);
            }
        } catch (Exception e) {
            complaint.setText(getResources().getString(R.string.complaint));
            inquiry.setText(getResources().getString(R.string.inquiry));
            tickets.setText(getResources().getString(R.string.report));
        }
    }


    public LookUp getLookup(SharedPreferences mshaPreferences, String id) {
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }


    public void backTo(View v) {
        Actions.backTo(this);
    }


    public void filterTicket() {
        for (int i = 0; i < ticketarray.size(); i++) {
            if (ticketarray.get(i).getType().equalsIgnoreCase("Feedback") || ticketarray.get(i).getStatus() .equalsIgnoreCase("Opened")
                    || ticketarray.get(i).getStatus().equals("Under Process")) {
                if (tickettype == 1)// opened
                    filteredTicket.add(ticketarray.get(i));
            } else if (ticketarray.get(i).getStatus().equals("Closed")) {
                if (tickettype == 2)// closed
                    filteredTicket.add(ticketarray.get(i));
            }
        }
    }


    public class getTickets extends AsyncTask<Void, Void, Void> {

        String token = "";
        boolean showticket;

        public getTickets(boolean showticket) {
            this.showticket = showticket;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            token = Actions.create_token_new();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            //Connection.disableSSLCertificateChecking();

            try {
                TCTDbAdapter source = new TCTDbAdapter(ComplaintActivity.this);
                source.open();
                ArrayList<Profile> arr = source.getAllProfiles();

                String phone = arr.size() > 0 ? arr.get(0).getnum() : "";
                Log.wtf("ComplaintActivity","ArrayList<Profile> arr = source.getAllProfiles() Count = " + arr.size());
                Log.wtf("ComplaintActivity","phone : " + phone);

                url = new URL(MyApplication.link + "PostData.asmx/GetTickets?mobile=" + phone + "&token=" + token);
                Log.wtf("ComplaintActivity","getTickets url : " + url.toString());

                source.close();
                DocumentBuilderFactory dbf = DocumentBuilderFactory .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("ReportedIssue");

                Log.wtf("getTickets","doc : " + doc);
                Log.wtf("getTickets","doc getElementsByTagName(ReportedIssue): " + doc.getElementsByTagName("ReportedIssue"));

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt.getElementsByTagName("Id");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String id = ((Node) nameList.item(0)).getNodeValue();

                    String issuenamear = null, issuenameen = null, updateDate = "";
                    NodeList IssueDetailL = fstElmnt
                            .getElementsByTagName("IssueDetail");
                    for (int j = 0; j < IssueDetailL.getLength(); j++) {
                        Node node2 = IssueDetailL.item(j);
                        Element IssueDetail = (Element) node2;

                        NodeList isfortransList4 = IssueDetail
                                .getElementsByTagName("Name");
                        Element isfortransElemen4 = (Element) isfortransList4
                                .item(0);
                        isfortransList4 = isfortransElemen4.getChildNodes();
                        issuenameen = ((Node) isfortransList4.item(0))
                                .getNodeValue();

                        NodeList isfortransList5 = IssueDetail
                                .getElementsByTagName("NameAr");
                        Element isfortransElemen5 = (Element) isfortransList5
                                .item(0);
                        isfortransList5 = isfortransElemen5.getChildNodes();
                        issuenamear = ((Node) isfortransList5.item(0))
                                .getNodeValue();

                        NodeList isfortransList22 = fstElmnt
                                .getElementsByTagName("LastUpdateDate");
                        Element isfortransElemen22 = (Element) isfortransList22
                                .item(0);
                        isfortransList22 = isfortransElemen22.getChildNodes();
                        try {
                            updateDate = ((Node) isfortransList22.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            updateDate = "";
                        }
                    }

                    String waitTimeAr = null, waitTime = null, eventNameEn = null, eventNameAr = null;
                    NodeList IssueTypenL = fstElmnt
                            .getElementsByTagName("IssueType");
                    for (int l = 0; l < IssueTypenL.getLength(); l++) {
                        Node node2 = IssueTypenL.item(l);
                        Element IssueType = (Element) node2;

                        NodeList isfortransListIt1 = IssueType
                                .getElementsByTagName("Duration");
                        Element isfortransElemenIt1 = (Element) isfortransListIt1
                                .item(0);
                        isfortransListIt1 = isfortransElemenIt1.getChildNodes();
                        String duration = ((Node) isfortransListIt1.item(0))
                                .getNodeValue();

                        NodeList isfortransListIt22 = IssueType
                                .getElementsByTagName("Name");
                        Element isfortransElemenIt22 = (Element) isfortransListIt22
                                .item(0);
                        isfortransListIt22= isfortransElemenIt22.getChildNodes();
                        eventNameEn = ((Node) isfortransListIt22.item(0))
                                .getNodeValue();

                        NodeList isfortransListIt23 = IssueType
                                .getElementsByTagName("NameAr");
                        Element isfortransElemenIt23 = (Element) isfortransListIt23
                                .item(0);
                        isfortransListIt23 = isfortransElemenIt23.getChildNodes();
                        eventNameAr = ((Node) isfortransListIt23.item(0))
                                .getNodeValue();

                        NodeList isfortransListIt2 = IssueType
                                .getElementsByTagName("Unit");
                        Element isfortransElemenIt2 = (Element) isfortransListIt2
                                .item(0);
                        isfortransListIt2 = isfortransElemenIt2.getChildNodes();
                        try {
                            waitTime = duration
                                    + " "
                                    + ((Node) isfortransListIt2.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            waitTime = "";
                        }

                        NodeList isfortransListIt3 = IssueType
                                .getElementsByTagName("UnitAr");
                        Element isfortransElemenIt3 = (Element) isfortransListIt3
                                .item(0);
                        isfortransListIt3 = isfortransElemenIt3.getChildNodes();
                        try {
                            waitTimeAr = duration
                                    + " "
                                    + ((Node) isfortransListIt3.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            waitTimeAr = "";
                        }

                    }

                    String locat = null;
                    NodeList AddressnL = fstElmnt
                            .getElementsByTagName("Address");
                    for (int m = 0; m < AddressnL.getLength(); m++) {
                        Node node2 = AddressnL.item(m);
                        Element Address = (Element) node2;

                        NodeList isfortransListIt1 = Address
                                .getElementsByTagName("Country");
                        Element isfortransElemenIt1 = (Element) isfortransListIt1
                                .item(0);
                        isfortransListIt1 = isfortransElemenIt1.getChildNodes();
                        String Country;
                        try {
                            Country = ((Node) isfortransListIt1.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            Country = "";
                        }

                        NodeList isfortransListIt2 = Address
                                .getElementsByTagName("Route");
                        Element isfortransElemenIt2 = (Element) isfortransListIt2
                                .item(0);
                        isfortransListIt2 = isfortransElemenIt2.getChildNodes();
                        String Route;
                        try {
                            Route = ((Node) isfortransListIt2.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            Route = "";
                        }

                        NodeList isfortransListIt3 = Address
                                .getElementsByTagName("Locality");
                        Element isfortransElemenIt3 = (Element) isfortransListIt3
                                .item(0);
                        isfortransListIt3 = isfortransElemenIt3.getChildNodes();
                        String Locality;
                        try {
                            Locality = ((Node) isfortransListIt3.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            Locality = "";
                        }

                        NodeList isfortransListIt4 = Address
                                .getElementsByTagName("Province");
                        Element isfortransElemenIt4 = (Element) isfortransListIt4
                                .item(0);
                        isfortransListIt4 = isfortransElemenIt4.getChildNodes();
                        String Province;
                        try {
                            Province = ((Node) isfortransListIt4.item(0))
                                    .getNodeValue();
                        } catch (Exception e) {
                            Province = "";
                        }

                        locat = Route + " ," + Locality + " ," + Province
                                + " ," + Country;

                    }

                    NodeList websiteList = fstElmnt
                            .getElementsByTagName("MobileNumber");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    String number = "";

                    try{
                        number = ((Node) websiteList.item(0)).getNodeValue();
                    }
                    catch(Exception e){
                        Log.wtf("websiteList = websiteElement.getChildNodes(); error","e : "  +e.getMessage());
                    }

                    NodeList isfortransList = fstElmnt
                            .getElementsByTagName("AffectedMobileNumber");
                    Element isfortransElement = (Element) isfortransList
                            .item(0);
                    isfortransList = isfortransElement.getChildNodes();
                    String affected = ((Node) isfortransList.item(0))
                            .getNodeValue();

                    NodeList isfortransList2 = fstElmnt
                            .getElementsByTagName("SendingDate");
                    Element isfortransElement2 = (Element) isfortransList2
                            .item(0);
                    isfortransList2 = isfortransElement2.getChildNodes();
                    String sendingdate = ((Node) isfortransList2.item(0))
                            .getNodeValue();

                    NodeList isfortransList3 = fstElmnt
                            .getElementsByTagName("Status");
                    Element isfortransElemen3 = (Element) isfortransList3
                            .item(0);
                    isfortransList3 = isfortransElemen3.getChildNodes();
                    String status = ((Node) isfortransList3.item(0))
                            .getNodeValue();

                    NodeList isfortransList7 = fstElmnt
                            .getElementsByTagName("CalledFromNumber");
                    Element isfortransElemen7 = (Element) isfortransList7
                            .item(0);
                    isfortransList7 = isfortransElemen7.getChildNodes();
                    String cra = "";
                    try {
                        cra = ((Node) isfortransList7.item(0)).getNodeValue();
                    } catch (Exception e) {
                        cra = "";
                    }

                    NodeList isfortransList8 = fstElmnt
                            .getElementsByTagName("Email");
                    Element isfortransElemen8 = (Element) isfortransList8
                            .item(0);
                    isfortransList8 = isfortransElemen8.getChildNodes();
                    String email = "";
                    try {
                        email = ((Node) isfortransList8.item(0)).getNodeValue();
                    } catch (Exception e) {
                        email = "";
                    }

                    NodeList isfortransList9 = fstElmnt
                            .getElementsByTagName("Comments");
                    Element isfortransElemen9 = (Element) isfortransList9
                            .item(0);
                    isfortransList9 = isfortransElemen9.getChildNodes();
                    String comments = "";
                    try {
                        comments = ((Node) isfortransList9.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        comments = "";
                    }

                    String spnamear = null, spnameen = null;
                    NodeList ServiceProvidernL = fstElmnt
                            .getElementsByTagName("ServiceProvider");
                    for (int k = 0; k < ServiceProvidernL.getLength(); k++) {
                        Node node3 = ServiceProvidernL.item(k);
                        Element ServiceProvidernEL = (Element) node3;

                        NodeList isfortransListSP1 = ServiceProvidernEL
                                .getElementsByTagName("Name");
                        Element isfortransElemenSP1 = (Element) isfortransListSP1
                                .item(0);
                        isfortransListSP1 = isfortransElemenSP1.getChildNodes();
                        spnameen = ((Node) isfortransListSP1.item(0)) .getNodeValue();

                        NodeList isfortransListSP2 = ServiceProvidernEL .getElementsByTagName("NameAr");
                        Element isfortransElemenSP2 = (Element) isfortransListSP2 .item(0);
                        isfortransListSP2 = isfortransElemenSP2.getChildNodes();
                        spnamear = ((Node) isfortransListSP2.item(0))
                                .getNodeValue();
                    }

                    NodeList isfortransList12 = fstElmnt
                            .getElementsByTagName("DidYouComplainToSP");
                    Element isfortransElemen12 = (Element) isfortransList12
                            .item(0);
                    isfortransList12 = isfortransElemen12.getChildNodes();
                    String spComplaint;
                    try {
                        spComplaint = ((Node) isfortransList12.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        spComplaint = "";
                    }

                    NodeList isfortransList13 = fstElmnt
                            .getElementsByTagName("LogWaitTime");
                    Element isfortransElemen13 = (Element) isfortransList13
                            .item(0);
                    isfortransList13 = isfortransElemen13.getChildNodes();
                    String longWait = ((Node) isfortransList13.item(0))
                            .getNodeValue();

                    NodeList isfortransList14 = fstElmnt
                            .getElementsByTagName("SPComplaintRefNumber");
                    Element isfortransElemen14 = (Element) isfortransList14
                            .item(0);
                    isfortransList14 = isfortransElemen14.getChildNodes();
                    String cmplNum = "";
                    try {
                        cmplNum = ((Node) isfortransList14.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        cmplNum = "";
                    }

                    NodeList isfortransList15 = fstElmnt
                            .getElementsByTagName("SendingDate");
                    Element isfortransElemen15 = (Element) isfortransList15
                            .item(0);
                    isfortransList15 = isfortransElemen15.getChildNodes();
                    String cmplDate = ((Node) isfortransList15.item(0))
                            .getNodeValue();

                    NodeList isfortransList16 = fstElmnt
                            .getElementsByTagName("CallDate");
                    Element isfortransElemen16 = (Element) isfortransList16
                            .item(0);
                    isfortransList16 = isfortransElemen16.getChildNodes();
                    String calldate = ((Node) isfortransList16.item(0))
                            .getNodeValue();

                    NodeList isfortransList17 = fstElmnt
                            .getElementsByTagName("CreationDate");
                    Element isfortransElemen17 = (Element) isfortransList17
                            .item(0);
                    isfortransList17 = isfortransElemen17.getChildNodes();
                    String creationdate = ((Node) isfortransList17.item(0))
                            .getNodeValue();

                    NodeList isfortransList18 = fstElmnt
                            .getElementsByTagName("DidYouComplainToSPLookup");
                    Element isfortransElemen18 = (Element) isfortransList18
                            .item(0);
                    isfortransList18 = isfortransElemen18.getChildNodes();
                    String spComplaintId = ((Node) isfortransList18.item(0))
                            .getNodeValue();

                    NodeList isfortransList19 = fstElmnt
                            .getElementsByTagName("Type");
                    Element isfortransElemen19 = (Element) isfortransList19
                            .item(0);
                    isfortransList19 = isfortransElemen19.getChildNodes();
                    String type = ((Node) isfortransList19.item(0))
                            .getNodeValue();

                    NodeList isfortransList20 = fstElmnt
                            .getElementsByTagName("CRNumber");
                    Element isfortransElemen20 = (Element) isfortransList20
                            .item(0);
                    isfortransList20 = isfortransElemen20.getChildNodes();
                    String CRNum = "";
                    try {
                        CRNum = ((Node) isfortransList20.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        CRNum = "";
                    }

                    NodeList isfortransList21 = fstElmnt
                            .getElementsByTagName("AffectedQatarId");
                    Element isfortransElemen21 = (Element) isfortransList21
                            .item(0);
                    isfortransList21 = isfortransElemen21.getChildNodes();
                    String AffqId = "";
                    try {
                        AffqId = ((Node) isfortransList21.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        AffqId = "";
                    }

                    Ticket t = new Ticket();
                    t.setAffectedMobileNumber(affected);
                    t.setEventNameAr(eventNameAr);
                    t.setEventNameEn(eventNameEn);
                    t.setMobilenumber(number);
                    t.setSendingDate(sendingdate);
                    t.setIssueDetailNameAr(issuenamear);
                    t.setIssueDetailNameEn(issuenameen);
                    t.setwaitTime(waitTime);
                    t.setwaitTimeAr(waitTimeAr);
                    t.setStatus(status);
                    t.setId(Integer.parseInt(id));
                    t.setEmail(email);
                    t.setComments(comments);
                    t.setServiceProvider(spnameen);
                    t.setServiceProviderAr(spnamear);
                    t.setCRA(cra);
                    t.setSpComplaint(spComplaint);
                    t.setLocality(locat);
                    t.setSpComplaintId(spComplaintId);
                    t.setLongWait(longWait);
                    t.setCRNum(CRNum);
                    t.setaffectedqatarid(AffqId);
                    t.setCmplRefNum(cmplNum);
                    t.setCmplDate(cmplDate);
                    t.setCalldate(calldate);
                    t.setSendingDate(creationdate);
                    t.setUpdateDate(updateDate);
                    t.setType(type);
                    ticketarray.add(t);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.wtf("getTickets","MalformedURLException crash : " + e.getMessage());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Log.wtf("getTickets","ParserConfigurationException crash : " + e.getMessage());
            } catch (SAXException e) {
                e.printStackTrace();
                Log.wtf("getTickets","SAXException crash : " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.wtf("getTickets","IOException crash : " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Log.wtf("getTickets","Exception crash : " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (showticket) {
                filteredTicket.clear();
                filterTicket();
                adapterticket.notifyDataSetChanged();
            }

        }
    }


    protected class LaunchingEvent extends AsyncTask<Void, Void, Integer> {
        com.ids.ict.Error error;
        ProgressDialog progress;
        Event[] nn;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progress = new ProgressDialog(ComplaintActivity.this);
            progress.show();
            ;
            progress.setCanceledOnTouchOutside(false);
            //	progress.setMessage("الرجاء الانتظار...");*/

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (lang.equals(MyApplication.ENGLISH)) {
                TCTDbAdapter datasource = new TCTDbAdapter( ComplaintActivity.this);
                datasource.open();
                if (issue_id == 1) {
                    ArrayList<Event> arr = datasource.getissue_Type("1");
                    nn = new Event[arr.size()];
                    nn = arr.toArray(nn);
                } else {
                    ArrayList<Event> arr = datasource.getissue_Type("2");
                    nn = new Event[arr.size()];
                    nn = arr.toArray(nn);
                }

                datasource.close();
            } else {

                TCTDbAdapter datasource = new TCTDbAdapter( ComplaintActivity.this);
                datasource.open();
                if (issue_id == 1) {
                    ArrayList<Event> arr = datasource.getissue_Type("3");
                    nn = new Event[arr.size()];
                    nn = arr.toArray(nn);
                } else {
                    ArrayList<Event> arr = datasource.getissue_Type("4");
                    nn = new Event[arr.size()];
                    nn = arr.toArray(nn);
                }

                datasource.close();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (MyApplication.lunched == -1 || MyApplication.lunched == -2) {
                tickets.performClick();
                MyApplication.lunched = 2;
            } else {

                if (lang.equals(MyApplication.ENGLISH)) {
                    if (result == 0) {
                        EventsListArrayAdapter_off adapter = new EventsListArrayAdapter_off( ComplaintActivity.this, nn);
                        eventList.setAdapter(adapter);
                    } else {
                        EventsListArrayAdapter adapter = new EventsListArrayAdapter( ComplaintActivity.this, nn);
                        eventList.setAdapter(adapter);
                    }
                } else {
                    if (result == 0) {
                        ArEventsListArrayAdapter_off adapter = new ArEventsListArrayAdapter_off( ComplaintActivity.this, nn);
                        eventList.setAdapter(adapter);
                    } else {
                        ArEventsListArrayAdapter adapter = new ArEventsListArrayAdapter( ComplaintActivity.this, nn);
                        eventList.setAdapter(adapter);
                    }
                }
            }
            //progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }


    public void topBarBack(View v) {
        ComplaintActivity.this.finish();
    }


    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {
                intent.setClass(ComplaintActivity.this, MoreActivity.class);
                ComplaintActivity.this.startActivity(intent);
                break;
            }
            case R.id.home: {

                intent.setClass(ComplaintActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ComplaintActivity.this.startActivity(intent);
                ComplaintActivity.this.finish();
                break;
            }

        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(ComplaintActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ComplaintActivity.this.startActivity(intent);
        ComplaintActivity.this.finish();

    }

}