package com.ids.ict.classes;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ids.ict.MyApplication;


public class ViewResizing {

    public static void setListRowTextResizing(View view, Activity activity) {
        final ViewGroup mContainer = (ViewGroup) view.getRootView();
        try {
            ViewResizing.setViewGroupContentTextSize(activity, mContainer/*, app.XLARGE*/);

        } catch (NoSuchFieldError e) {

        }
    }

    ///from generalized
    private static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }

    public static void RadioButtonResize(Activity activity, RadioButton textView, int sizePx) {

        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        //calculate perscentage according 480 px width screen
        double px = convertPixelsToDp(sizePx, activity) * 1.5;
        double percentage = (double) ((double) px / 480);
        //float x=textView.getTextSize();
        int width = MyApplication.screenWidth;
        Typeface face;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            face = MyApplication.facePolarisMedium;
        } else {
            face = MyApplication.faceDinar;
        }
        SharedPreferences mshared = PreferenceManager.getDefaultSharedPreferences(activity);


        if (width == 0 || face == null) {

            if (width == 0) {

                width = mshared.getInt("widthscreen", 0);
            }
            if (face == null)
                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                    face = MyApplication.faceDinar;
                } else {
                    face = MyApplication.facePolarisMedium;
                }

        }
        textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * percentage));
        textView.getTextSize();
        //selectedLang = MyApplication.Lang;
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            face = MyApplication.facePolarisMedium;
        } else {
            face = MyApplication.faceDinar;
        }
        textView.setTypeface(face);

    }

    public static void textResize(Activity activity, TextView textView, int sizePx) {

        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        //calculate perscentage according 480 px width screen
        double px = convertPixelsToDp(sizePx, activity) * 1.5;
        double percentage = (double) ((double) px / 480);
        //float x=textView.getTextSize();
        int width = MyApplication.screenWidth;
        Typeface face;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            face = MyApplication.facePolarisMedium;
        } else {
            face = MyApplication.faceDinar;
        }
        SharedPreferences mshared = PreferenceManager.getDefaultSharedPreferences(activity);


        if (width == 0 || face == null) {

            if (width == 0) {

                width = mshared.getInt("widthscreen", 0);
            }
            if (face == null)
                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                    face = MyApplication.faceDinar;
                } else {
                    face = MyApplication.facePolarisMedium;
                }

        }
        textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * percentage));
        textView.getTextSize();
        //selectedLang = MyApplication.Lang;
        if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
            face = MyApplication.facePolarisMedium;
        } else {
            face = MyApplication.faceDinar;
        }
        textView.setTypeface(face);

    }

    public static void setChildViewsSize(Activity activity, int parentLayoutId) {

        ViewGroup layout = (ViewGroup) activity.findViewById(parentLayoutId);
        for (int j = 0; j < layout.getChildCount(); j++) {
            try {
                final TextView text = (TextView) layout.getChildAt(j);
                int size = (int) text.getTextSize();
                textResize(activity, text, size);
            } catch (Exception e) {

            }
        }
    }

    public static void setViewGroupContentTextSize(Activity activity, ViewGroup view) {
        ViewGroup viewGroup = view;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                setViewGroupContentTextSize(activity, (ViewGroup) viewGroup.getChildAt(i));
            } else if (viewGroup.getChildAt(i) instanceof TextView) {
                try {

                    final TextView text = (TextView) viewGroup.getChildAt(i);
                    int size = (int) text.getTextSize();
                    textResize(activity, text, size);
                } catch (Exception e) {

                }
            } else if (viewGroup.getChildAt(i) instanceof RadioButton) {
                try {

                    final RadioButton text = (RadioButton) viewGroup.getChildAt(i);
                    int size = (int) text.getTextSize();
                    RadioButtonResize(activity, text, size);
                } catch (Exception e) {

                }
            }
        }
    }

    public static void setDialogViewGroupContentTextSize(Activity activity, Dialog dialog, ViewGroup view) {
        ViewGroup viewGroup = view;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                setDialogViewGroupContentTextSize(activity, dialog, (ViewGroup) viewGroup.getChildAt(i));
            } else {
                try {

                    final TextView text = (TextView) viewGroup.getChildAt(i);
                    int size = (int) text.getTextSize();
                    textResize(activity, text, size);
                } catch (Exception e) {

                }
            }
        }
    }

    public static void setLayoutHeight(MyApplication app, View v, int hight) {

        //int vv=v.getHeight();
        float percentage = (float) hight / v.getHeight();//
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int) (app.screenHeight / percentage);
        v.setLayoutParams(params);


    }

    public static void setLayoutHeightXL(MyApplication app, View v) {

        //int vv=v.getHeight();
        float percentage = (float) 1200 / v.getHeight();//
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int) (app.screenHeight / percentage);
        v.setLayoutParams(params);


    }

    public static void setViewSize(Activity activity, View v) {
        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        if (app.screenHeight > 470) {//change size for large screens only
            float hPercentage = (float) 800 / v.getHeight();//
            float wPercentage = (float) 480 / v.getHeight();//
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = (int) (app.screenHeight / hPercentage);
            params.width = (int) (app.screenWidth / wPercentage);
            v.setLayoutParams(params);
        }

    }

    public static int getHeight(int screenHeight, int height) {
        float percentage = (float) 800 / height;
        //int a=(int) (screenHeight/percentage);
        return (int) (screenHeight / percentage);
    }


    public static void setViewResizingListener(final Activity activity, final View v) {
        ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            boolean measured = false;

            public void onGlobalLayout() {
                if (!measured) {
                    ViewResizing.setViewSize(activity, v);

                    measured = true;
                }
            }
        });
    }

    public static void ToggleResize(Activity activity, RadioButton textView, int sizePx) {

        MyApplication app;
        app = (MyApplication) activity.getApplicationContext();
        double px = convertPixelsToDp(sizePx, activity) * 1.5;
        double percentage = (double) ((double) px / 480);
        int width = MyApplication.screenWidth;
        Typeface face = app.face;
        SharedPreferences mshared = PreferenceManager.getDefaultSharedPreferences(activity);


        if (width == 0 || face == null) {

            if (width == 0) {

                width = mshared.getInt("widthscreen", 0);
            }
            if (face == null)


                if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
                    face = MyApplication.faceDinar;
                } else {
                    face = MyApplication.facePolarisMedium;
                }

        }
        textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * percentage));
        textView.getTextSize();
        textView.setTypeface(face);
    }


    private static void setViewHeightSizeNormal(Activity activity, View view) {
        MyApplication app = (MyApplication) activity.getApplicationContext();
        int dimen = (int) (app.screenHeight / 10.5);//~76px in 800 height screen
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = dimen;

        view.setLayoutParams(params);
    }

    public static void setTradesButtonSize(Activity activity, View view) {
        MyApplication app = (MyApplication) activity.getApplicationContext();

        int width = (int) (app.screenWidth / 8);//~60px in 480 width screen
        int height = (int) (app.screenHeight / 16);//~50px in 800 height screen
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        params.width = width;
        view.setLayoutParams(params);
    }

    public static void setChangeButtonSize(Activity activity, View view) {
        MyApplication app = (MyApplication) activity.getApplicationContext();

        int width = (int) (app.screenWidth / 5.6);//~86px in 480 width screen
        int height = (int) (app.screenHeight / 14.3);//~56px in 800 height screen
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        params.width = width;
        view.setLayoutParams(params);
    }

    public static void setViewHeightSizeSmall(Activity activity, View view) {
        MyApplication app = (MyApplication) activity.getApplicationContext();
        int dimen = (int) (app.screenHeight / 14.81);//~54px in 800 height screen
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = dimen;

        view.setLayoutParams(params);
    }

    public static void setViewHeightSizeXSmall(Activity activity, View view) {
        MyApplication app = (MyApplication) activity.getApplicationContext();
        int dimen = (int) (app.screenHeight / 16.67);//~48px in 800 height screen
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = dimen;

        view.setLayoutParams(params);
    }


    public static void setPageTextResizing(Activity activity) {
        final ViewGroup mContainer = (ViewGroup) activity.findViewById(
                android.R.id.content).getRootView();
        try {
            ViewResizing.setViewGroupContentTextSize(activity, mContainer/*, app.XLARGE*/);

        } catch (NoSuchFieldError e) {

        }
    }

    public static void setDialogTextResizing(Activity activity, Dialog dialog) {
        final ViewGroup mContainer = (ViewGroup) dialog.findViewById(
                android.R.id.content).getRootView();
        try {
            ViewResizing.setDialogViewGroupContentTextSize(activity, dialog, mContainer/*, app.XLARGE*/);

        } catch (NoSuchFieldError e) {

        }
    }


}
