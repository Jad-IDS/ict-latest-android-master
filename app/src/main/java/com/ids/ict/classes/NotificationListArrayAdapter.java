package com.ids.ict.classes;


import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.activities.Notifications;

import java.util.ArrayList;


public class NotificationListArrayAdapter extends ArrayAdapter<Notifications> {
    private final Activity context;
    private final  ArrayList<Notifications>  events;
    MyApplication app;

    public NotificationListArrayAdapter(Activity context,  ArrayList<Notifications> newEvents) {
        super(context, R.layout.notification_item, newEvents);
        this.context = context;
        this.events = newEvents;

    }

    static class ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
    }

    @Override
    public Notifications getItem(int position) {
        return events.get(position);
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = context.getLayoutInflater();

            view = vi.inflate(R.layout.notification_item, null, true);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) view.findViewById(R.id.title_textView);
            holder.dateTextView = (TextView) view.findViewById(R.id.date_textView);

            ViewResizing.setListRowTextResizing(view, context);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (MyApplication.nightMod) {

            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.white));
            holder.dateTextView.setTextColor(context.getResources().getColor(R.color.white));
        }

        holder.titleTextView.setText(events.get(position).getName());
        holder.dateTextView.setTypeface(MyApplication.facePolarisMedium);
        holder.dateTextView.setText(events.get(position).getDate());//.split("T")[0]);

        if (MyApplication.Lang.equals(MyApplication.ARABIC)){
            holder.titleTextView.setGravity(Gravity.RIGHT);
            holder.dateTextView.setGravity(Gravity.RIGHT);
        }else{

            holder.titleTextView.setGravity(Gravity.LEFT);
            holder.dateTextView.setGravity(Gravity.LEFT);
        }

        return view;
    }
}
