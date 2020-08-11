package com.ids.ict.classes;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.activities.Event;
import com.squareup.picasso.Picasso;



public class ArEventsListArrayAdapter_off  extends ArrayAdapter<Event> {
	private final Activity context;
	private final Event[] events;
	Typeface  tf;
	MyApplication app;
	public ArEventsListArrayAdapter_off(Activity context, Event[] newEvents) {
		super(context, R.layout.areventslist_item,newEvents );
		this.context = context;
		this.events = newEvents;
		tf = MyApplication.faceDinar;


	}

	static class ViewHolder {
		public TextView titleTextView;
		public String thumbnailUrl;
		public ImageView imageView;
		public byte[] img;
	}

	@Override
	public Event getItem(int position) {
		return events[position];
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater vi = context.getLayoutInflater();

			view = vi.inflate(R.layout.areventslist_item, null,true);
			holder = new ViewHolder();
			holder.titleTextView = (TextView)view.findViewById(R.id.title_textView); 
			holder.titleTextView.setTypeface(tf);
			holder.imageView=(ImageView)view.findViewById(R.id.news_image);
			if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {

				holder.titleTextView.setGravity(Gravity.LEFT);
			}else{
				holder.titleTextView.setGravity(Gravity.RIGHT);
			}

			view.setTag(holder);
		}
		else{
			holder = (ViewHolder) view.getTag();
		}
		holder.titleTextView.setText(events[position].getName());
		//		holder.thumbnailUrl=events[position].getThumbnailUrl();
		if (MyApplication.nightMod){
			holder.thumbnailUrl=events[position].getThumbnailUrlNight();
			holder.img=events[position].getimgNight();
		}else{
			holder.thumbnailUrl=events[position].getThumbnailUrlDayl();
			holder.img=events[position].getimgDay();
		}

		try {
			if(MyApplication.nightMod)
				Picasso.with(context)
						.load(events[position].getThumbnailUrlNight())
						//.placeholder(R.mipmap.icon)
						//.resize(250,200)
						//.rotate(90)
						.into(holder.imageView);
			else
				Picasso.with(context)
						.load(events[position].getThumbnailUrlDayl())
						//.placeholder(R.mipmap.icon)
						//.resize(250,200)
						//.rotate(90)
						.into(holder.imageView);
		}
		catch (Exception e)
		{

		}
		return view;
	}

	protected class LoadImage extends AsyncTask<ViewHolder,Void,Bitmap>{
		ViewHolder holder;

		protected void onPreExecute(ViewHolder h){
		}
		@Override
		protected Bitmap doInBackground(ViewHolder... holder){
			Bitmap img=null;
			this.holder=holder[0];

			img = BitmapFactory.decodeByteArray(this.holder.img , 0, this.holder.img .length);
			return img;

		}
		@Override
		protected void onPostExecute(Bitmap result){
			if(result!=null){
				holder.imageView.setImageBitmap(result);
			}
			else
				holder.imageView.clearAnimation();  	   
		}
	}

	protected class LoadImage_first extends AsyncTask<ViewHolder,Void,Bitmap>{
		ViewHolder holder;

		protected void onPreExecute(ViewHolder h){

		}
		@Override
		protected Bitmap doInBackground(ViewHolder... holder){
			Bitmap img=null;
			this.holder=holder[0];

			if(this.holder.thumbnailUrl!=null)
			{
				Connection conn=new Connection(this.holder.thumbnailUrl);

				img=conn.readImage();
			}

			return img;

		}
		@Override
		protected void onPostExecute(Bitmap result){
			if(result!=null){

				holder.imageView.setImageBitmap(result);
			}
			else
				holder.imageView.clearAnimation();  	   
		}
	}
}
