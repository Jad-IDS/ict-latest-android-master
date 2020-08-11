package com.ids.ict.classes;



import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.activities.Event;
import com.ids.ict.activities.Notifications;

import android.R.color;
import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;



public class ArNotificationListArrayAdapter  extends ArrayAdapter<Notifications> {
	private final Activity context;
	private final Notifications[] events;
	MyApplication app;
	public ArNotificationListArrayAdapter(Activity context, Notifications[] newEvents) {
		super(context, R.layout.arnotification_item,newEvents );
		this.context = context;
		this.events = newEvents;
		//app=(MyApplication) context.getApplicationContext();
		
		
	}
	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class

	static class ViewHolder {
		public TextView titleTextView;
		public TextView detailsTextView;
		public TextView dateTextView;
	}

	@Override
	public Notifications getItem(int position) {
	    return events[position];
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //int theType = getItemViewType(position); 
        ViewHolder holder;
        if (view == null) {
         
         // LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(Context.LAYOUT_INFLATER_SERVICE);
       //   if (theType == 1) {
              // inflate the ordinary row
        	LayoutInflater vi = context.getLayoutInflater();
        	
              view = vi.inflate(R.layout.arnotification_item, null,true);
              holder = new ViewHolder();
              holder.titleTextView = (TextView)view.findViewById(R.id.title_textView); 
              holder.detailsTextView = (TextView)view.findViewById(R.id.description_textView); 
              holder.dateTextView = (TextView)view.findViewById(R.id.date_textView); 
          
         // } else if (theType == 0){
             // inflate the row for the last position
         //     view = vi.inflate(R.layout.eventslist_firstitem, null);
            //  holder.hsv = (HorizontalScrollView) view.findViewById(R.id.news_horizental1);
            //  holder.hsv.scrollTo(holder.hsv.getRight(), holder.hsv.getTop());
          //    holder.hsv = (HorizontalScrollView) view.findViewById(R.id.news_horizental1);
            //  new Handler().postDelayed(new Runnable(){   
            	//  @Override
            	  //public void run() {
            		  
            		//  holder.hsv.scrollTo( holder.hsv.getRight(),  holder.hsv.getTop());
            	      //}       

            	  //},100L);
           //   holder.viewfliper = (ViewFlipper) view.findViewById(R.id.view_flipper);
             // holder.gal = (Gallery) view.findViewById(R.id.gallery1);
           //   holder.titleTextView = (TextView)view.findViewById(R.id.myImageViewText1);
             // holder.imageView=(ImageView)view.findViewById(R.id.imageView1);
              
              //holder.titleTextView.setText(events[position].getName());
  			//holder.titleTextView.setTextColor(0xFFFFFFFF);
  		//	holder.thumbnailUrl=events[position].getThumbnailUrl();
  			/*try{
  				LoadImage_first mLoadImage=new LoadImage_first();
  				mLoadImage.onPreExecute(holder);
  				mLoadImage.execute(holder);
  			}
  			catch(Exception e){
  				
  			}
  			*/
      //    } 
          view.setTag(holder);
         }
        else{
        	holder = (ViewHolder) view.getTag();
        }
        holder.titleTextView.setText(events[position].getName());
        holder.detailsTextView.setText(Html.fromHtml(events[position].getDescription()));
        holder.dateTextView.setText(events[position].getDate());
		//holder.titleTextView.setTextColor(0xFFFFFFFF);
	//	holder.thumbnailUrl=events[position].getThumbnailUrl();
		/*
		try{
			LoadImage mLoadImage=new LoadImage();
			mLoadImage.onPreExecute(holder);
			mLoadImage.execute(holder);
		}
		catch(Exception e){
			
		}
        /*
            final ViewHolder holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(Context.LAYOUT_INFLATER_SERVICE);
         ///  if (theType == 1) {
                // inflate the ordinary row
                view = vi.inflate(R.layout.eventslist_item, null);
                holder.titleTextView = (TextView)view.findViewById(R.id.title_textView); 
            	holder.imageView=(ImageView)view.findViewById(R.id.news_image);
            	holder.titleTextView.setText(events[position].getName());
  			holder.titleTextView.setTextColor(0xFFFFFFFF);
  			holder.thumbnailUrl=events[position].getThumbnailUrl();
  			
  			try{
  				LoadImage mLoadImage=new LoadImage();
  				mLoadImage.onPreExecute(holder);
  				mLoadImage.execute(holder);
  			}
  			catch(Exception e){
  				
  			}
       //     } else if (theType == 0){
               // inflate the row for the last position
              //  view = vi.inflate(R.layout.gallerytest, null);
              //  holder.gal.setAdapter(new GalleryImageAdapter(this.context));
             //   viewpaper tempMyFriendPagerAdapter = new viewpaper();//(mFragmentManager, mItems.get(position).getFriendHabits());
         
                
               
            //    holder.titleTextView = (TextView)view.findViewById(R.id.myImageViewText1);
           //     holder.imageView=(ImageView)view.findViewById(R.id.imageView1);
            //    holder.titleTextView.setText(events[position].getName());
    		//	holder.titleTextView.setTextColor(0xFFFFFFFF);
    		//	holder.thumbnailUrl=events[position].getThumbnailUrl();
    			//try{
    		//		LoadImage mLoadImage=new LoadImage();
    		//		mLoadImage.onPreExecute(holder);
    		//		mLoadImage.execute(holder);
    		//	}
    		//	catch(Exception e){
    				
    		//	}
         //   } 
          //  view.setTag(holder);
           
        }
        */
   //   view.setBackgroundColor(position % 2 == 0 ? 0xFF380024: 0xFF7A0049);
      //  view.setBackgroundResource(position % 2 == 0 ? com.annahar.newspaper.R.drawable.darkerbg: com.annahar.newspaper.R.drawable.lighterbg);
        return view;
	}
      
 //other stuff here, keep in mind that you have a different layout for your last position so double check what are trying to initialize

	
	/*/
	protected class LoadImage extends AsyncTask<ViewHolder,Void,Bitmap>{
		ViewHolder holder;
		  
		   protected void onPreExecute(ViewHolder h){
		//	   if(h.thumbnailUrl == null) h.imageView.setVisibility(View.GONE);
		//	   else{
			   //Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
			   h.imageView.setImageResource(R.drawable.icon);
			//   h.imageView.startAnimation(rotate);
			//   }

				   
			    }
		   @Override
		   protected Bitmap doInBackground(ViewHolder... holder){
			   Bitmap img=null;
			   this.holder=holder[0];
			 
				if(this.holder.thumbnailUrl!=null)
				{
					//open connection to read image stored in news object
					Connection conn=new Connection(this.holder.thumbnailUrl);
					
					img=conn.readImage();
				}
			   //String b="";
			  		   
		return img;
			   
		   }
		   @Override
		   protected void onPostExecute(Bitmap result){
			if(result!=null){
			//	Bitmap resized = Bitmap.createScaledBitmap(result, 150, 150, true);
			holder.imageView.setImageBitmap(result);
			}
			else
				holder.imageView.setImageResource(R.drawable.icon);
			
			 holder.imageView.clearAnimation();  	   
		   }
	   }
	
	protected class LoadImage_first extends AsyncTask<ViewHolder,Void,Bitmap>{
		ViewHolder holder;
		  
		   protected void onPreExecute(ViewHolder h){
		//	   if(h.thumbnailUrl == null) h.imageView.setVisibility(View.GONE);
		//	   else{
			   //Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
			   h.imageView.setImageResource(R.drawable.icon);
			//   h.imageView.startAnimation(rotate);
			//   }

				   
			    }
		   @Override
		   protected Bitmap doInBackground(ViewHolder... holder){
			   Bitmap img=null;
			   this.holder=holder[0];
			 
				if(this.holder.thumbnailUrl!=null)
				{
					//open connection to read image stored in news object
					Connection conn=new Connection(this.holder.thumbnailUrl);
					
					img=conn.readImage();
				}
			   //String b="";
			  		   
		return img;
			   
		   }
		   @Override
		   protected void onPostExecute(Bitmap result){
			if(result!=null){
				
			holder.imageView.setImageBitmap(result);
			}
			else
				holder.imageView.setImageResource(R.drawable.icon);
			
			 holder.imageView.clearAnimation();  	   
		   }
		   */
	 //  }
}
