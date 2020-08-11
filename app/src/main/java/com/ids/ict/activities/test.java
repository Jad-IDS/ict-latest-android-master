package com.ids.ict.activities;

import android.app.Activity;
import android.os.Bundle;

import com.ids.ict.Actions;
import com.ids.ict.R;

/**
 * Created by Bushra on 1/3/2017.
 */
public class test extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Actions.setLocal(this);
        setContentView(R.layout.testactivity);


    }
}
