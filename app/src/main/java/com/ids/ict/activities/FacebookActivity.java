package com.ids.ict.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.facebook.android.*;
import com.facebook.android.Facebook.DialogListener;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;

public class FacebookActivity extends Activity implements DialogListener,
        OnClickListener {

    private Facebook facebookClient;
    private LinearLayout facebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
        this.setContentView(R.layout.com_facebook_picker_title_bar);//my layout xml
        Actions.loadMainBar(this);
        facebookButton = (LinearLayout) this.findViewById(R.id.com_facebook_picker_activity_circle);
        getApplication().getPackageName();
    }

    public void backTo(View v) {
        Actions.backTo(this);
    }

    @Override
    public void onComplete(Bundle values) {

        if (values.isEmpty()) {
            //"skip" clicked ?
            return;
        }

        // if facebookClient.authorize(...) was successful, this runs
        // this also runs after successful post
        // after posting, "post_id" is added to the values bundle
        // I use that to differentiate between a call from
        // faceBook.authorize(...) and a call from a successful post
        // is there a better way of doing this?
        if (!values.containsKey("post_id")) {
            try {
                Bundle parameters = new Bundle();
                parameters.putString("message", "this is a test");// the message to post to the wall
                facebookClient.dialog(this, "stream.publish", parameters, this);// "stream.publish" is an API call
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
                String url1 = "www.google.com";
                Connection conn = new Connection(url1);

                try {
                    String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(DialogError e) {
        System.out.println("Error: " + e.getMessage());
    }

    @Override
    public void onFacebookError(FacebookError e) {
        System.out.println("Error: " + e.getMessage());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onClick(View v) {
        if (v == facebookButton) {
            //    facebookClient = new Facebook();
            // replace APP_API_ID with your own
            //  facebookClient.authorize(this, APP_API_ID,
            //  new String[] {"publish_stream", "read_stream", "offline_access"}, this);
        }
    }
}
