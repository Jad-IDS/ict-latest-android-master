package com.ids.ict.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PopUpImageActivity extends Activity {

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(true);

        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        setContentView(R.layout.image_layout);


        String path = getIntent().getExtras().getString("img");

        Log.wtf("path",path);

        if(path.contains("pdf")){

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.height = (int) (MyApplication.screenHeight / 1.2);
            params.width = (int) (MyApplication.screenWidth / 1.2);
            WebView mWebView=new WebView(PopUpImageActivity.this);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+path);
            setContentView(mWebView);
        }else{
            WindowManager.LayoutParams params = getWindow().getAttributes();

            params.height = MyApplication.screenHeight / 2;
            params.width = MyApplication.screenWidth - 30;
            ImageView img = (ImageView) findViewById(R.id.imggg);
            imageLoader.displayImage(path, img);
        }
    }
}
