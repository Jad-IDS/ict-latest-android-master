package com.ids.ict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;

import java.sql.Connection;


public class FailedReportActivity extends Activity {

    Connection conn;
    private boolean open = false;
    private final Context context = this;
    String number, email1, qatarid1, comm1, date1, issue1, sp1, locx, locy, countid, status, roam_c;
    int rep_id;
    MyApplication app;
    String lang = "";
    private ListView listMenu;
    private RelativeLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Actions.setLocal(this);
        setContentView(R.layout.successreportpgtest);
        Actions.loadMainBar(this);
        ViewResizing.setPageTextResizing(this);
        app = (MyApplication) getApplicationContext();
        //    app.face=Typeface.createFromAsset(getAssets(), "fonts/tahomabd.ttf");
        final Bundle mbundle = this.getIntent().getExtras();
        Log.d("add favorite", "passed here");
        TextView num = (TextView) findViewById(R.id.fil_num_edit_rep);
        TextView email = (TextView) findViewById(R.id.fill_email_edit_rep);
        TextView issue = (TextView) findViewById(R.id.spinCountry_rep);
        TextView comm = (TextView) findViewById(R.id.comment_edit_rep);
        TextView qatarid = (TextView) findViewById(R.id.spinSP_rep);
        TextView sp = (TextView) findViewById(R.id.qatarid_rep);

        TextView roam;
        if (lang.equals(MyApplication.ENGLISH)) {
            //  			  roam= (TextView) findViewById(R.id.spinSP_roam);
        } else {
            //  				  roam= (TextView) findViewById(R.id.spinCountry_roam);
        }
        TextView num_lab = (TextView) findViewById(R.id.textView2);
        //  			TextView email_lab = (TextView) findViewById(R.id.textView2_email);
        //  			TextView comm_lab = (TextView) findViewById(R.id.textView2_comm);
        //  			TextView qatarid_lab = (TextView) findViewById(R.id.textView2_qatarid);
        //  			TextView  sp_lab= (TextView) findViewById(R.id.textView2_sp);
        //  			TextView  roam_lab= (TextView) findViewById(R.id.textView2_roam);
        Typeface tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        //  			num_lab.setTypeface(tf);
        //  			email_lab.setTypeface(tf);
        //  			issue_lab.setTypeface(tf);
        //  			comm_lab.setTypeface(tf);
        //  			qatarid_lab.setTypeface(tf);
        //  			sp_lab.setTypeface(tf);
        num.setTypeface(tf);
        email.setTypeface(tf);
        issue.setTypeface(tf);
        comm.setTypeface(tf);
        qatarid.setTypeface(tf);
        sp.setTypeface(tf);
        //  			roam.setTypeface(tf);
        //  			roam_lab.setTypeface(tf);


        num.setText(mbundle.getString("mobilenum"));
        number = mbundle.getString("mobilenum");
        email.setText(mbundle.getString("email"));
        email1 = mbundle.getString("email");
        issue.setText(mbundle.getString("issue"));
        issue1 = mbundle.getString("issue");
        comm.setText(mbundle.getString("comments"));
        comm1 = mbundle.getString("comments");
        sp.setText(mbundle.getString("sp"));
        sp1 = mbundle.getString("sp");
        roam_c = mbundle.getString("roam_country");

        if (!roam_c.equals("")) {
            //  				roam.setVisibility(View.VISIBLE);
            //  				roam_lab.setVisibility(View.VISIBLE);

            //  				roam.setText("       "+roam_c);
        }
        qatarid.setText(mbundle.getString("qatariID"));
        qatarid1 = mbundle.getString("qatariID");
        date1 = mbundle.getString("date");
        locx = mbundle.getString("locx");
        locy = mbundle.getString("locy");
        countid = mbundle.getString("countid");
        status = mbundle.getString("status");
        rep_id = mbundle.getInt("idrep");
        //   Launching mLaunching=new Launching();
        //   mLaunching.execute();
        Button send = (Button) findViewById(R.id.send_button_rep);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FailedReportActivity.this, ReportFailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mobilenum", number);
                bundle.putString("email", email1);
                bundle.putString("qatariID", qatarid1);
                bundle.putString("comments", comm1);
                bundle.putString("date", date1);
                bundle.putString("issue", issue1);
                bundle.putString("sp", sp1);
                bundle.putString("locx", locx);
                bundle.putString("locy", locy);
                bundle.putString("countid", countid);
                bundle.putString("status", status);
                bundle.putInt("idrep", rep_id);
                //adding the bundle to the intent
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        Button can = (Button) findViewById(R.id.cancel_button_rep);
        can.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FailedReportActivity.this.finish();
            }
        });
    }

    public void goToSettings(View v) {
        Actions.goToSettings(this);
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }


}



