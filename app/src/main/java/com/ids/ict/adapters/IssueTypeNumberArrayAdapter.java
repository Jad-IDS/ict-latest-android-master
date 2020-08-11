package com.ids.ict.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ids.ict.Actions;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.classes.IssueNumber;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IssueTypeNumberArrayAdapter extends ArrayAdapter<IssueNumber> {
    Activity context;
    private String view;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ArrayList<IssueNumber> list = new ArrayList<IssueNumber>();

    public IssueTypeNumberArrayAdapter(Activity context,
                                       ArrayList<IssueNumber> list, String view) {

        super(context, R.layout.issuetype_list_item, list);

        this.context = context;
        this.list = list;
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.issuetype_list_item, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.issuename);
            holder.imageItem = (ImageView) row.findViewById(R.id.issueimage);
            holder.number = (TextView) row.findViewById(R.id.issuenumber);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            holder.txtTitle.setTypeface(MyApplication.facePolarisMedium);
        } else {
            holder.txtTitle.setTypeface(MyApplication.faceDinar);
        }
        IssueNumber item = list.get(position);

        holder.txtTitle.setText(item.getLabel());
        holder.number.setText(item.getNumber());


        /*if (MyApplication.nightMod)
            holder.number.setTextColor(context.getResources().getColor(
                    R.color.nightBlue));
        else
            holder.number.setTextColor(context.getResources().getColor(
                    R.color.bordo));*/


        if (view.equalsIgnoreCase("main")) {
            // new
            // DownloadImageTask((ImageView)row.findViewById(R.id.issueimage)).execute(item.getImageName());
            ImageLoader.getInstance().displayImage(item.getImageName(),
                    (ImageView) row.findViewById(R.id.issueimage), options,
                    animateFirstListener);
        } else if (view.equalsIgnoreCase("detailed")) {
            int i = context.getResources().getIdentifier(item.getImageName(),
                    "drawable", context.getPackageName());
            String cleaned = Actions.imageNameClean(item.getImageName());
            Drawable d = Actions.getImageFromResources(context, cleaned);

            holder.imageItem.setImageDrawable(d);
        }

        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
        TextView number;

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
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                // Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
