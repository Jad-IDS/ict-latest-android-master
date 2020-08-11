package com.ids.ict.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;

import java.util.ArrayList;

public class MoreActivity extends Activity {

    MyApplication app;
    int footerButton;
    View mainBar, footer;
    String lang = "";

    ArrayList<String> moreItemsList;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        lang = Actions.setLocal(this);
        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.morelist);
        ViewResizing.setPageTextResizing(this);
        moreItemsList = new ArrayList<String>();
        final ImageView home = (ImageView) findViewById(R.id.home);
        home.setImageResource(R.drawable.home);
        final ImageView morebtn = (ImageView) findViewById(R.id.morebtn);
        morebtn.setImageResource(R.drawable.more_new);

        if (MyApplication.nightMod) {
            final RelativeLayout buttop = (RelativeLayout) findViewById(R.id.mainbar);
            buttop.setBackgroundResource(R.drawable.footer_nt);
            final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
            footer.setBackgroundResource(R.drawable.footer_nt);
            final ImageView settings = (ImageView) findViewById(R.id.settings_guid_image);
            settings.setImageResource(R.drawable.setting_nt);
            final ImageView faq = (ImageView) findViewById(R.id.faq_image);
            faq.setImageResource(R.drawable.faq_nt);
            final ImageView terms = (ImageView) findViewById(R.id.terms_image);
            terms.setImageResource(R.drawable.conditions_nt);
        }

        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            final ImageView buttop = (ImageView) findViewById(R.id.backbtn);
            buttop.setImageResource(R.drawable.back_btn_en);
        }

    }

    public void goToSettings(View v) {
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    public void goTo(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings_menu_layout: {
                Actions.goToSettings(this);
                break;
            }
            case R.id.faq_layout: {
                //	MoreActivity.this.finish();
                Intent intent = new Intent(MoreActivity.this, FaqActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.terms_layout: {
                Intent intent = new Intent(MoreActivity.this, TermsActivity.class);
                intent.putExtra("fromMore", 1);
                startActivity(intent);
                break;
            }
        }


    }

    void getMoreList() {
        moreItemsList.add("Consumer Guide");
        moreItemsList.add("Terms And Conditions");
        moreItemsList.add("FAQ");
        moreItemsList.add("About US");

    }

    public void topBarBack(View v) {
        MoreActivity.this.finish();
    }

    public void footer(View v) {

        ImageButton mButton = (ImageButton) v;
        Intent intent = new Intent();
        switch (mButton.getId()) {
            case R.id.morebtn: {

                break;
            }
            case R.id.home: {
                intent.setClass(MoreActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                MoreActivity.this.startActivity(intent);
                MoreActivity.this.finish();
                break;
            }

        }
    }
}