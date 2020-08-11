package com.ids.ict.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ids.ict.R;

/**
 * Created by Amal on 10/28/2016.
 */
public class AndroidCustomDialogActivity extends Activity {

    void openCustomDialog(){
        final AlertDialog.Builder customDialog
                = new AlertDialog.Builder(AndroidCustomDialogActivity.this);
        customDialog.setTitle("Custom Dialog");

        LayoutInflater layoutInflater
                = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.about_us_popup,null);
//
//        WebView wv = (WebView) view.findViewById(R.id.wv);
//                                    TextView x = (TextView) view.findViewById(R.id.x);
//                                    TextView b = (TextView) view.findViewById(R.id.ok);
//
//                                    b.setVisibility(View.VISIBLE);
//                                    b.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            customDialog.dismiss();
//                                        }
//                                    });
//                                    x.setVisibility(View.GONE);
//                                    wv.getSettings().setJavaScriptEnabled(true);
//                                    wv.loadDataWithBaseURL("", "<html><body style=\"text-align:justify;\">" + newString + "</body></html>", "text/html", "UTF-8", "");
//                                    wv.setWebViewClient(new WebViewClient() {
//                                        @Override
//                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                            view.loadUrl(url);
//
//                                            return true;
//                                        }
//                                    });
//        customDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//
//            }});
//
//        customDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//
//            }});

        customDialog.setView(view);
        customDialog.show();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_popup);
        openCustomDialog();
    }
}
