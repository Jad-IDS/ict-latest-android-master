package com.ids.ict.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ids.ict.R;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.Satisfaction;

import java.util.ArrayList;


public class SatisfactionListArrayAdapter extends ArrayAdapter<Satisfaction> {

    private static int COLOR2 = 0;
    private final Activity context;
    private final ArrayList<Satisfaction> satisfactionList;
    int selectedPosition = 0;
    String lang;
    Satisfaction selected;

    public SatisfactionListArrayAdapter(Activity context, ArrayList<Satisfaction> satisfactionList) {
        super(context, R.layout.satisfactionlist_item, satisfactionList);
        this.context = context;
        this.satisfactionList = satisfactionList;
        selected = satisfactionList.get(0);

    }
    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class

    static class ViewHolder {
        public TextView title;
        public RadioButton value;
        RelativeLayout backgd;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row layout

        final ViewHolder holder;
        // Recycle existing view if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.satisfactionlist_item, null, true);
            holder = new ViewHolder();


            holder.title = (TextView) rowView.findViewById(R.id.title);
            /*if (MyApplication.nightMod)
                holder.title.setTextColor(context.getResources().getColor(R.color.nightBlue));*/
            holder.value = (RadioButton) rowView.findViewById(R.id.value);
            holder.backgd=(RelativeLayout)rowView.findViewById(R.id.backgd);
            ViewResizing.setListRowTextResizing(rowView, context);
            rowView.setTag(holder);

        } else {
            holder = (ViewHolder) rowView.getTag();


        }
        holder.title.setText(satisfactionList.get(position).getValue());

        holder.value.setChecked(position == selectedPosition);
        if(holder.value.isChecked()) {
//            COLOR2 = Color.parseColor("#FFFFFF");
//            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
//            Drawable d = ContextCompat.getDrawable(context, R.drawable.greenmark);
//            d.setColorFilter(COLOR2,mMode);
            holder.value.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context,R.drawable.greenmark), null);

        }
        else {
            holder.value.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        holder.value.setTag(position);

        holder.backgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer) holder.value.getTag();
                selected = satisfactionList.get(position);
                notifyDataSetChanged();
            }
        });
        return rowView;


    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public Satisfaction getSelectedSatisfaction() {
        return selected;
    }
}


