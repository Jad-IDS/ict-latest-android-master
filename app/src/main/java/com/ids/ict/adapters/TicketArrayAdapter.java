package com.ids.ict.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.activities.PopUpTicketActivity;
import com.ids.ict.classes.LookUp;
import com.ids.ict.classes.Ticket;

import java.util.ArrayList;

public class TicketArrayAdapter extends ArrayAdapter<Ticket> {

	private final Activity context;
	private final ArrayList<Ticket> tickets;
	Typeface tf;
SharedPreferences mprePreferences;
	String lang;

	public TicketArrayAdapter(Activity context, ArrayList<Ticket> tixketlist) {
		super(context, R.layout.satisfactionlist_item, tixketlist);
		this.context = context;
		this.tickets = tixketlist;
		tf= Actions.getFont();
		//this.mprePreferences =mPrefs;
		mprePreferences =  context.getSharedPreferences("PrefEng",
				Context.MODE_PRIVATE);

	}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class

	static class ViewHolder {
		public TextView category;
		public TextView date, CRA, updatedate;
		RelativeLayout img;
		ImageView imgticket,arrow;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout

		ViewHolder holder;
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.ticket_item, null, true);
			holder = new ViewHolder();

			holder.category = (TextView) rowView.findViewById(R.id.category);
			holder.category.setTypeface(tf);
			holder.date = (TextView) rowView.findViewById(R.id.sendingdate);
			//holder.date.setTypeface(tf);
			holder.CRA = (TextView) rowView.findViewById(R.id.cra);
			holder.imgticket = (ImageView) rowView.findViewById(R.id.ticketimg);
			holder.updatedate = (TextView) rowView
					.findViewById(R.id.updatedate);
			holder.arrow = (ImageView)rowView.findViewById(R.id.arrow);
			holder.img = (RelativeLayout) rowView.findViewById(R.id.img);
			ViewResizing.setListRowTextResizing(rowView, context);
			rowView.setTag(holder);

		} else {
			holder = (ViewHolder) rowView.getTag();

		}
		if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
			holder.category.setText(tickets.get(position)
					.getIssueDetailNameEn());
		} else {
			holder.category.setText(tickets.get(position)
					.getIssueDetailNameAr());
		}
		if (MyApplication.nightMod) {
			holder.imgticket.setImageResource(R.drawable.messagenight);

			if(MyApplication.Lang.equals(MyApplication.ENGLISH))
				holder.arrow.setImageResource(R.drawable.arrownight);
			else
				holder.arrow.setImageResource(R.drawable.arrownightleft);
		}
		else {
			holder.imgticket.setImageResource(R.drawable.messagemaroon);
			if(MyApplication.Lang.equals(MyApplication.ENGLISH))
				holder.arrow.setImageResource(R.drawable.arrowmaroon);
			else
				holder.arrow.setImageResource(R.drawable.arrowmaroonleft);
		}
		try {
			holder.date.setText(tickets.get(position).getSendingDate()
					.split("T")[0]);

		} catch (Exception e) {

		}
		holder.CRA.setText(tickets.get(position).getCmplRefNum());
		try {
			String ggg= tickets.get(position).getStatus();
			Log.d("eee",tickets.get(position).getStatus());
			if (tickets.get(position).getStatus().trim()
					.equalsIgnoreCase("Open")
					|| tickets.get(position).getStatus().trim().equals("Under Process")) {
				//opened
			if(MyApplication.Lang.equals(MyApplication.ENGLISH))
				holder.updatedate.setText(getLookup(mprePreferences, "6").nameen);
				else
				holder.updatedate.setText(getLookup(mprePreferences, "6").namear);

			} else if (tickets.get(position).getStatus().equals("Closed")) {
			//closed
				if(MyApplication.Lang.equals(MyApplication.ENGLISH))
					holder.updatedate.setText(getLookup(mprePreferences, "28").nameen);
				else
					holder.updatedate.setText(getLookup(mprePreferences, "28").namear);
			}

		} catch (Exception e) {

		}
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("id", tickets.get(position).getId());
				if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
					bundle.putString("title", tickets.get(position)
							.getIssueDetailNameEn());
				} else {
					bundle.putString("title", tickets.get(position)
							.getIssueDetailNameAr());
				}
				i.putExtras(bundle);

				i.setClass(context, PopUpTicketActivity.class);
				context.startActivity(i);

			}
		});
		holder.arrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
			holder.category.setTypeface(MyApplication.facePolarisMedium);
			holder.updatedate.setTypeface(MyApplication.facePolarisMedium);
			holder.date.setTypeface(MyApplication.facePolarisMedium);
		} else {
			holder.category.setTypeface(MyApplication.faceDinar);
			holder.updatedate.setTypeface(MyApplication.faceDinar);
			holder.date.setTypeface(MyApplication.facePolarisMedium);
		}

	//	ViewResizing.setListRowTextResizing(rowView, context);
		return rowView;

	}
	public LookUp getLookup(SharedPreferences mshaPreferences, String id) {
		LookUp look = new LookUp();
		Gson gson = new Gson();
		String json = mshaPreferences.getString(id, "");
		look = gson.fromJson(json, LookUp.class);

		return look;
	}

}