package com.ids.ict.adapters;




import java.util.ArrayList;

import com.ids.ict.Actions;
import com.ids.ict.R;
import com.ids.ict.classes.InitialMapSettings;
import com.ids.ict.classes.MapLabel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	/**
	 * 
	 * @author manish.s
	 *
	 */
	public class PinMeterListArrayAdapter extends ArrayAdapter<MapLabel> {
	 Activity context;
	
	 ArrayList<MapLabel> list = new ArrayList<MapLabel>();

	 public PinMeterListArrayAdapter(Activity context, ArrayList<MapLabel> list) {
		 
	  super(context, R.layout.pin_meter_list_item, list);
	
	  this.context = context;
	  this.list = list;
	 }

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	  View row = convertView;
	  RecordHolder holder = null;

	  if (row == null) {
	   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	   row = inflater.inflate(R.layout.pin_meter_list_item, parent, false);

	   holder = new RecordHolder();
	   holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
	   holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
	   row.setTag(holder);
	  } else {
	   holder = (RecordHolder) row.getTag();
	  }

	  MapLabel item = list.get(position);
	  holder.txtTitle.setText(item.getLabel());
	// int i=context.getResources()
     //        .getIdentifier(item.getImageName(), "drawable", context.getPackageName());
	  holder.imageItem.setImageDrawable(Actions.getImageFromResources(context, item.getImageName()));
	  return row;

	 }

	 static class RecordHolder {
	  TextView txtTitle;
	  ImageView imageItem;

	 }
	}

