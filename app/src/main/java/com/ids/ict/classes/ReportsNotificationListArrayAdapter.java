package com.ids.ict.classes;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ids.ict.R;


public class ReportsNotificationListArrayAdapter  extends ArrayAdapter<Mail_OFF> {
	private final Activity context;
	private final Mail_OFF[] events;
	com.ids.ict.MyApplication app;
	public ReportsNotificationListArrayAdapter(Activity context, Mail_OFF[] newEvents) {
		super(context, com.ids.ict.R.layout.reportsnotlist_item,newEvents );
		this.context = context;
		this.events = newEvents;
	//	app=(MyApplication) context.getApplicationContext();
	}
	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class

	static class ViewHolder {
		public TextView titleTextView;
		//public String thumbnailUrl;
		//public ImageView imageView;
		public TextView bodyTextView;
	//	public TextView dateTextView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row layout
		ViewHolder holder;
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		// code for latest news feed
		Log.d("position",""+position);
		if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.reportsnotlist_item, null, true);
				holder = new ViewHolder();
				holder.titleTextView = (TextView) rowView.findViewById(R.id.title_textView_repnot);
				ViewResizing.textResize(context, holder.titleTextView,(int) holder.titleTextView.getTextSize());
				holder.bodyTextView=(TextView)rowView.findViewById(R.id.description_textView_repnot);
				ViewResizing.textResize(context, holder.bodyTextView,(int) holder.bodyTextView.getTextSize());
			//	holder.imageView=(ImageView)rowView.findViewById(R.id.news_bullet);
			//	holder.dateTextView= (TextView)rowView.findViewById(R.id.date_textView_rep);
			//	ViewResizing.textResize(context, holder.dateTextView,(int) holder.dateTextView.getTextSize());
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}
			holder.titleTextView.setText(events[position].getdate_aff());
			//String text  = events[position].getDate();
		//	if(!events[position].getLocation().equals("")) text = events[position].getLocation()+ "   " + text;
		//	holder.dateTextView.setText(text);
		//	holder.thumbnailUrl=events[position].getThumbnailUrl();
			//holder.imageView.setImageResource(drawable.redcircle);
			if(events[position].getstatus().equals("sended")){
				holder.bodyTextView.setText("Message sent successfully!");
			}else {
				holder.bodyTextView.setText("A message could not be sent!");
			}
			
			
			
			
		return rowView;
	}
	
	
	
	   
}
