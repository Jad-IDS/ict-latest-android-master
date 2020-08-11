package com.ids.ict.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CoomplaintActivityOld extends Activity {

	WebView  mWebView;
	private ProgressDialog pd;
	int footerButton;
	String lang="";
	MyApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		app=(MyApplication) getApplicationContext();

		//TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
		lang= Actions.setLocal(this);
	if (savedInstanceState != null) {


	}
	setContentView(R.layout.complaint);
	Actions.loadMainBar(this);
	ViewResizing.setPageTextResizing(this);
	String url = get_consumerlink();
	WebView webview = (WebView)this.findViewById(R.id.complaint_webView);
	webview.setVerticalScrollBarEnabled(true);
	webview.setHorizontalScrollBarEnabled(true);

	String useag = getResources().getString(R.string.useragent);
	webview.getSettings().setUserAgentString(useag);
	webview.getSettings().setLoadWithOverviewMode(true);
	webview.getSettings().setUseWideViewPort(true);  
	webview.getSettings().setJavaScriptEnabled(true);
	webview.loadUrl(url);
	final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
	webview.setWebChromeClient(new WebChromeClient() {
		@Override

		public void onProgressChanged(WebView view, int newProgress) {

			super.onProgressChanged(view, newProgress);

			loadingProgressBar.setProgress(newProgress);
			if (newProgress == 100) {

				loadingProgressBar.setVisibility(View.INVISIBLE);

			} else {

				loadingProgressBar.setVisibility(View.VISIBLE);

			}

		}

	});
	Bundle bundle;
	bundle = this.getIntent().getExtras();

	footerButton= this.getIntent().getIntExtra("footerButton", R.id.home); 



	}
	public void goToSettings(View v){
		Actions.goToSettings(this);
	}
	public void backTo(View v){
		Actions.backTo(this);
	}



	private String get_consumerlink(){
		String name = null;
		try{
			URL url;
			if(lang.equals(MyApplication.ENGLISH)){
				url = new URL(""+app.link+"GeneralServices.asmx/GetSocailMediaLinks?password="+app.pass+"&language=en");
			}else{
				url = new URL(""+app.link+"GeneralServices.asmx/GetSocailMediaLinks?password="+app.pass+"&language=ar");

			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("SocialMedia");
			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);



				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt.getElementsByTagName("Name");
				Element nameElement = (Element) nameList.item(0);
				nameList = nameElement.getChildNodes();
				String id =  ((Node) nameList.item(0)).getNodeValue();

				NodeList websiteList = fstElmnt.getElementsByTagName("Link");
				Element websiteElement = (Element) websiteList.item(0);
				websiteList = websiteElement.getChildNodes();
				String title ="";
				try{
					title =  ((Node) websiteList.item(0)).getNodeValue();
				}catch(Exception e){
					String  url1 = ""+app.link+"PostData.asmx/VerifyRegistration";
					Connection conn = new Connection(url1);

					try {
						String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if(id.equals("ComplaintForm")){
					name = title + "&token=" + Actions.create_token();
					break;
				}
			}

		}catch(Exception e){
			System.out.println("gg "+ e);
			String  url1 = ""+app.link+"PostData.asmx/VerifyRegistration";
			Connection conn = new Connection(url1);

			try {
				String error_return = conn.executeMultipartPost_Send_Error(this.getClass().getSimpleName(), Actions.getDeviceName(), "1", e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return name;
	}

	@Override
	public void onStart()
	{
		super.onStart();  	
	}  

	private void webpageSettings(){
		mWebView=(WebView)findViewById(R.id.speed_webView);
		mWebView.clearCache(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			boolean loadingFinished = true;
			boolean redirect = false;

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!loadingFinished) {
					redirect = true;
				}

				loadingFinished = false;

				view.loadUrl(url);

				return false;

			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				loadingFinished = false;
				ProgressDialog test=pd;
				if(!pd.isShowing())
					try{
						pd = ProgressDialog.show(CoomplaintActivityOld.this, "", CoomplaintActivityOld.this.getString(R.string.loading), true, true);
					}catch(Exception e){

					}
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				if(!redirect){
					loadingFinished = true;
				}
				if(loadingFinished && !redirect){
					pd.dismiss();
				} else{
					redirect = false; 
				}

			}

		} );
		mWebView.getSettings().setDomStorageEnabled(true); 
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Actions.setLocal(this);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();;

	}
	
	
	protected class LaunchingEvent extends AsyncTask<Void,Void,Integer>{
		com.ids.ict.Error error;
		Event[] nn,nn1,nn2;
		@Override
		protected void onPreExecute(){
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			Log.d("launching","passed here");
			TCTDbAdapter source = new TCTDbAdapter(CoomplaintActivityOld.this);
			source.open();
			AtomicReference <Event[]> ref=new AtomicReference<Event[]>(null);

			String lan;
			String eventsSource = ""+app.link+"GeneralServices.asmx/GetIssueTypes?";
			eventsSource = eventsSource + "language=en&password=&mainIssueTypeId=1";
			Log.d("eventsource", eventsSource);


			AtomicReference <Event[]> ref1=new AtomicReference<Event[]>(null);

			eventsSource = ""+app.link+"GeneralServices.asmx/GetIssueTypes?language=en&password=&mainIssueTypeId=2";
			Log.d("eventsource", eventsSource);

			ArrayList<Event> arr = source.getissue_Type("2");
			nn2 = arr.toArray(new Event[arr.size()]);

			ArrayList<Event> arr1 = source.getissue_Type("1");
			nn1 = arr1.toArray(new Event[arr1.size()]);
			source.close();

			nn = new Event[nn1.length + nn2.length+2];
			int k=0;
			for(int i = 0;i<nn1.length;i++){
				nn[i] = nn1[i];
				k++;
			}
			for(int i=0;i<nn2.length;i++){
				nn[k] = nn2[i];
				k++;
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result){
		}

	}
}