/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ids.ict.classes;

//import static com.ids.otlib.fixot.CommonUtilities.SERVER_URL;

import static com.ids.ict.classes.CommonUtilities.TAG;

import com.google.android.gcm.GCMRegistrar;
import com.ids.ict.MyApplication;

import android.content.Context;
import android.util.Log;

import java.util.Random;





/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

   

  public  static boolean registerPushSharp(final Context context, final String regId) {
	  //String url="";
	  //String url=app.rootUrl+"id=1234&Tocken="+regId+
    	//		"&UserId="+app.mUserData.getUserId()+"&DeviceType=1";//&SenderID="+SENDER_ID+"&PackName="+context.getString(R.string.package_name);
	 // String url=context.getResources().getString(R.string.root_url)+context.getResources().getString(R.string.registerParam)+
		//	 "userID="+app.userEmail+"&deviceToken="+regId+"&DeviceTypeID=2";
//	  String url=context.getResources().getString(R.string.registerParam);
	  String url= MyApplication.link+MyApplication.post+"RegisterDevice";

       Connection conn=new Connection(context, url);
       
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
         
             //   displayMessage(context, context.getString(
             //           R.string.server_registering, i, MAX_ATTEMPTS));
             String result= conn.registerUnregisterPushNotification(regId);
           
               if (!conn.getError().getState()){
            	  
            		 String[] value=result.split("<.*?>");
            	
            	   try{
            		   int x=Integer.parseInt(value[2]);
            		   if(x>=0){
                       GCMRegistrar.setRegisteredOnServer(context, true);
                     //  String message = context.getString(R.string.server_registered);
                     //  CommonUtilities.displayMessage(context, message);
                       return true;
            		   } 
            	   }catch(Exception e){
            		 //not registered on server due to error in server database(eg. try to dublicate)
                	   return false;
            	   }
              
               }else{
             	  // String message = context.getString(R.string.server_register_error,
                   //         MAX_ATTEMPTS);
                   // CommonUtilities.displayMessage(context, message);
             	   try {
                        Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                        Thread.sleep(backoff);
                    } catch (InterruptedException e1) {
                        // Activity finished before we complete - exit.
                        Log.d(TAG, "Thread interrupted: abort remaining retries!");
                        Thread.currentThread().interrupt();
                        return false;
                    }
                    // increase backoff exponentially
                    backoff *= 2;
                   
                }
             
        }  
      //  if(!GCMRegistrar.isRegisteredOnServer(context)){
       // 	GCMRegistrar.unregister(context);
      //  }
     
       return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
      /*  Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            CommonUtilities.displayMessage(context, message);
        }*/
    }
    
  public  static boolean unregisterPushSharp(final Context context, final String regId) {
	  //to do code to unregister from server using webservice

//	  String url=context.getResources().getString(R.string.unregisterParam);
	  String url=MyApplication.link+MyApplication.post+"UnRegisterDevice";

      Connection conn=new Connection(context, url);
   
    	  String result= conn.registerUnregisterPushNotification(regId);
    	  if (!conn.getError().getState()){
       	   
              if(result.toLowerCase().contains("True".toLowerCase())){
            	  GCMRegistrar.setRegisteredOnServer(context, false);
            	//  String message = context.getString(R.string.server_unregistered);
               //  CommonUtilities.displayMessage(context, message);
               return true;
              }else{
           	   //not unregistered on server due to error in server database
           	   return false;
              }
        
    	  }   else{
    		  return false;
    	  }
      
  }
}
