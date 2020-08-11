package com.ids.ict.classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.ids.ict.MyApplication;

import java.io.PrintWriter;
import java.io.StringWriter;
import com.crashlytics.android.Crashlytics;

public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;

    public MyExceptionHandler(Context context, Class<?> c) {

        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        Crashlytics.logException(exception);
        Crashlytics.log(Log.ERROR, "Exception: ", exception.getMessage());

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
        Log.e("crashing", exception.getMessage());

        Intent intent = new Intent(myContext, myActivityClass);
        String s = stackTrace.toString();

//you can use this String to know what caused the exception and in which Activity
        intent.putExtra("uncaughtException", "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        myContext.startActivity(intent);

//for restarting the Activity
//app.
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        MyApplication app;
        app = (MyApplication) myContext.getApplicationContext();
        app.screenWidth = 450;
        app.screenHeight = 960;
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}