package com.ids.ict.activities;

import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.MyApplication;
import com.ids.ict.classes.Profile;
import com.ids.ict.R;
import com.ids.ict.TCTDbAdapter;
import com.ids.ict.classes.ViewResizing;
import com.ids.ict.classes.Comment;
import com.ids.ict.classes.LookUp;
import com.ids.ict.parser.SpeedTestResultParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PopUpTicketActivity extends Activity {

    String lang = "", address = "";
    int Ticketid;
    ArrayList<Comment> array = new ArrayList<Comment>();
    TextView title, tvRealPath;
    CommentListtAdapter adapter;
    ListView commentlist;
    private int RESULT_LOAD_IMAGE = 1;
    private String imageBytes = "";
    public String date;
    EditText feedback;
    ProgressDialog dialog;
    public String pathimg = "", filename = "";
    byte[] byteArray;
    RelativeLayout deletelayout;
    String image = "", filemanagerstring, selectedMediaPath, mediaPath = "";
    private int column_index;
    ImageLoader imageLoader = ImageLoader.getInstance();

    static LatLng location;
    Typeface tf;
    private float zoom, initialzoom;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public double latitude;
    public double longitude;
    boolean waitingForLocationUpdate = true, ctgSelected = false,
            lineSelected = false;
    private String provider;
    RelativeLayout progressBarLayout, rlPicName;
    ProgressBar progressBar;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor edit;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(true);

        //TestFairy.begin(this, "54311352c1c533467effe54ae7c064d3462cb224");
        lang = Actions.setLocal(this);
        setContentView(R.layout.ticket_popup);

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }

        } catch (Exception e) {

        }


        ViewResizing.setPageTextResizing(PopUpTicketActivity.this);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        rlPicName = (RelativeLayout) findViewById(R.id.rlPicName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        title = (TextView) findViewById(R.id.title);
        Bundle b = getIntent().getExtras();
        Ticketid = b.getInt("id");
        tvRealPath = (TextView) findViewById(R.id.tvRealPath);
        //title.setText(b.getString("title"));

        commentlist = (ListView) findViewById(R.id.comment_list);

        mSharedPreferences = getSharedPreferences("PrefEng", Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        deletelayout = (RelativeLayout) findViewById(R.id.deletelayout);

        // array.add(c);
        /*if (lang.equals(MyApplication.ENGLISH)) {
            dialog = ProgressDialog.show(PopUpTicketActivity.this, "",  "Loading..Wait..", true);
        } else {
            dialog = ProgressDialog.show(PopUpTicketActivity.this, "",
                    "الرجاء الانتظار", true);
        }*/


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        feedback = (EditText) findViewById(R.id.feedback);
        GetComments get = new GetComments();
        get.execute();
        if (MyApplication.nightMod) {
            Button send = (Button) findViewById(R.id.sendbutton);
            send.setBackground(ContextCompat.getDrawable(PopUpTicketActivity.this, R.drawable.button_night));
            RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
            header.setBackgroundColor(ContextCompat.getColor(this, R.color.nightBlue));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.height = MyApplication.screenHeight - 100;
        params.width = LayoutParams.MATCH_PARENT;
        PopUpTicketActivity.this.getWindow().setAttributes(params);

        registerLocationUpdates();


        tvRealPath.setTypeface(MyApplication.facePolarisMedium);
    }

    private class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        final List<String> displayedImages = Collections
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

    public void sendlocation(View v) {


        if (feedback.getText().toString().equals(null)) {
            Log.wtf("feedback", "null");
        }

        if (feedback.getText().toString().equals("")) {
            Log.wtf("feedback", "fade");
        } else {
            Log.wtf("feedback", feedback.getText().toString());

        }

        if (!feedback.getText().toString().equals("") || rlPicName.getVisibility() == View.VISIBLE) {
            sendComment2 send = new sendComment2();
            send.execute();
        }


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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    registerLocationUpdates();
                }
            }
        }
    }

    public void upload(View v) {


        /*Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);*/


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Log.d("select picture", "passed her");
                Uri selectedMediaUri = data.getData();

                String Fpath = selectedMediaUri.getPath();
                String Fpath2 = getPath(PopUpTicketActivity.this, selectedMediaUri);

                Log.d("Fpath", Fpath);
                Log.d("Fpath", Fpath2);

                filemanagerstring = selectedMediaUri.getPath();
                Log.d("select3", "passed her");
                Log.d("filemanagerstring", filemanagerstring);


                selectedMediaPath = getPath(PopUpTicketActivity.this, selectedMediaUri);//getPath(selectedMediaUri);
                Bitmap thumb;
                try {
                    if (selectedMediaPath.toString().endsWith(".jpg")
                            || selectedMediaPath.toString().endsWith(".png")) {
                        Log.d("passed here", "it is an image");
                        thumb = decodeUri(selectedMediaUri);
                    }
                    TextView path = (TextView) findViewById(R.id.path);
                    deletelayout.setVisibility(View.VISIBLE);
                    if (selectedMediaPath.length() > 10) {

                        //path.setText(selectedMediaPath.substring(0, 10));
                        tvRealPath.setText(selectedMediaPath.substring(selectedMediaPath.lastIndexOf("/") + 1));
                        int s = 1;
                        s += 2;
                    } else
                        tvRealPath.setText(selectedMediaPath);

                    mediaPath = selectedMediaPath;

                    rlPicName.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

    }

    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void viewPic(View v) {
        String pathFile = tvRealPath.getText().toString();

        if (pathFile.contains(".pdf")) {


            try {
                //Uri uriUrl = Uri.parse(selectedMediaPath);
                Uri uriUrl = Uri.fromFile(new File(selectedMediaPath));
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, uriUrl);
                intentUrl.setDataAndType(uriUrl, "application/pdf");
                intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentUrl);


            } catch (Exception e) {
                System.out.println(e);
                Toast.makeText(PopUpTicketActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
            }


        } else {

            Intent i = new Intent();
            i.setClass(PopUpTicketActivity.this,
                    PopUpImageActivity.class);
            Bundle b = new Bundle();

            String decodedImgUri = Uri.fromFile(new File(selectedMediaPath)).toString();
            b.putString("img", decodedImgUri);
            i.putExtras(b);
            startActivity(i);
        }


    }

    public void close(View v) {
        finish();
    }


    class IsBlocked extends AsyncTask<String, String, String> {

        String name = "";
        String res = "";
        String result = "";

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            try {
                Connection conn = new Connection(MyApplication.link + "GeneralServices.asmx/IsNumberBlocked?number=" + param[0] + "&password=" + MyApplication.pass);

                if (!conn.hasError()) {

                    SpeedTestResultParser parser = new SpeedTestResultParser();

                    res = parser.parse(conn.getInputStream2());

                    return res + "";
                }

            } catch (Exception e) {
                System.out.println("gg " + e);
                name = "-1";
            }
            return name;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);

            result = value;
            Log.wtf("result", result);


            if (result.contains("1")) { // blocked


                if (MyApplication.Lang.equals(MyApplication.ARABIC))
                    Actions.onCreateBlockedDialog(PopUpTicketActivity.this, getLookup("94").namear);
                else
                    Actions.onCreateBlockedDialog(PopUpTicketActivity.this, getLookup("94").nameen);
            } else { //not blocked

                if (feedback.getText().toString().equals("") && tvRealPath.getText().toString().equals("")) {
                    //Actions.onCreateDialog1(PopUpTicketActivity.this,getString(R.string.error_comment_empty));
                } else {
                    //  send
                    Log.wtf("will Send", "Comment");


                    if (feedback.getText().toString().equals(null)) {
                        Log.wtf("feedback", "null");
                    }

                    if (feedback.getText().toString().equals("")) {
                        Log.wtf("feedback", "fade");
                    } else {
                        Log.wtf("feedback", feedback.getText().toString());

                    }

                    if (!feedback.getText().toString().equals("") || !tvRealPath.getText().toString().equals("")) {

                        sendComment2 send = new sendComment2();
                        send.execute();

                    }

                }

            }


        }
    }

    public LookUp getLookup(String id) {
        SharedPreferences mshaPreferences = getSharedPreferences("PrefEng",
                Context.MODE_PRIVATE);
        LookUp look = new LookUp();
        Gson gson = new Gson();
        String json = mshaPreferences.getString(id, "");
        look = gson.fromJson(json, LookUp.class);

        return look;
    }

    public void send(View v) {


        TCTDbAdapter source = new TCTDbAdapter(PopUpTicketActivity.this);
        source.open();
        ArrayList<Profile> arr = source.getAllProfiles();
        String defMobile = arr.get(0).getnum();

        if (Actions.isNetworkAvailable(PopUpTicketActivity.this)) {

            new IsBlocked().execute(defMobile);

        } else {
            if (mSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("")) {

                if (feedback.getText().toString().equals("") && tvRealPath.getText().toString().equals("")) {
                    //Actions.onCreateDialog1(PopUpTicketActivity.this, getString(R.string.error_comment_empty));
                } else {
                    //  send
                    Log.wtf("will Send", "Comment");


                    if (feedback.getText().toString().equals(null)) {
                        Log.wtf("feedback", "null");
                    }

                    if (feedback.getText().toString().equals("")) {
                        Log.wtf("feedback", "fade");
                    } else {
                        Log.wtf("feedback", feedback.getText().toString());

                    }

                    if (!feedback.getText().toString().equals("") || !tvRealPath.getText().toString().equals("")) {

                        sendComment2 send = new sendComment2();
                        send.execute();

                    }
                }
            } else {
                if (mSharedPreferences.getString(getResources().getString(R.string.is_blocked), "").equals("true")) {
                    //alert blocked;
                    if (MyApplication.Lang.equals(MyApplication.ARABIC))
                        Actions.onCreateBlockedDialog(PopUpTicketActivity.this, getLookup("94").namear);
                    else
                        Actions.onCreateBlockedDialog(PopUpTicketActivity.this, getLookup("94").nameen);
                } else {

                    if (feedback.getText().toString().equals("") && tvRealPath.getText().toString().equals("")) {
                        //Actions.onCreateDialog1(PopUpTicketActivity.this, getString(R.string.error_comment_empty));
                    } else {
                        //  send
                        Log.wtf("will Send", "Comment");


                        if (feedback.getText().toString().equals(null)) {
                            Log.wtf("feedback", "null");
                        }

                        if (feedback.getText().toString().equals("")) {
                            Log.wtf("feedback", "fade");
                        } else {
                            Log.wtf("feedback", feedback.getText().toString());

                        }

                        if (!feedback.getText().toString().equals("") || !tvRealPath.getText().toString().equals("")) {

                            sendComment2 send = new sendComment2();
                            send.execute();

                        }
                    }
                }
            }
        }
    }

    public String getcurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", new Locale(
                "en"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public class GetComments extends AsyncTask<Void, Void, Void> {

        String token = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //dialog.show();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            token = Actions.create_token_new();

            Log.wtf("ticket id", Ticketid + "");
            Log.wtf("token id", token);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            URL url;
            try {
                url = new URL(MyApplication.link + "PostData.asmx/GetTicketFeedbacks?ticketId=" + Ticketid + "&token=" + token);


                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("TicketFeedback");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;

                    NodeList nameList = fstElmnt
                            .getElementsByTagName("CreationDate");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    String date = ((Node) nameList.item(0)).getNodeValue();
                    String username = "", message = "";
                    try {
                        NodeList websiteList = fstElmnt
                                .getElementsByTagName("UserName");
                        Element websiteElement = (Element) websiteList.item(0);
                        websiteList = websiteElement.getChildNodes();
                        username = ((Node) websiteList.item(0)).getNodeValue();
                    } catch (Exception e) {

                    }
                    try {
                        NodeList isfortransList = fstElmnt
                                .getElementsByTagName("Message");
                        Element isfortransElement = (Element) isfortransList
                                .item(0);
                        isfortransList = isfortransElement.getChildNodes();
                        message = ((Node) isfortransList.item(0))
                                .getNodeValue();
                    } catch (Exception e) {
                        message = "";
                    }
                    NodeList isfortransListi = fstElmnt
                            .getElementsByTagName("IsAdmin");
                    Element isfortransElementi = (Element) isfortransListi
                            .item(0);
                    isfortransListi = isfortransElementi.getChildNodes();
                    String isadmin = ((Node) isfortransListi.item(0))
                            .getNodeValue();
                    NodeList isfortransListisuser = fstElmnt
                            .getElementsByTagName("IsUser");
                    Element isfortransElementisuser = (Element) isfortransListisuser
                            .item(0);
                    isfortransListisuser = isfortransElementisuser
                            .getChildNodes();
                    String isuser = ((Node) isfortransListisuser.item(0))
                            .getNodeValue();
                    String filename = "";
                    try {
                        NodeList nodefilename = fstElmnt
                                .getElementsByTagName("FileName");
                        Element isfortransElementnme = (Element) nodefilename
                                .item(0);
                        nodefilename = isfortransElementnme.getChildNodes();
                        filename = ((Node) nodefilename.item(0)).getNodeValue();
                    } catch (Exception e) {
                        filename = "";
                    }
                    NodeList isforfilepath = fstElmnt
                            .getElementsByTagName("FilePath");
                    Element filepath = (Element) isforfilepath.item(0);
                    isforfilepath = filepath.getChildNodes();
                    String filepathurl = ((Node) isforfilepath.item(0))
                            .getNodeValue();
                    Comment t = new Comment();
                    t.setMessage(message);
                    t.setCreationDate(date);
                    t.setUsername(username);
                    t.setIsAdmin(isadmin);
                    t.setIsUser(isuser);
                    t.setFileName(filename);
                    t.setImageUrl(filepathurl);
//                    if(array.size()==0)
//                    array.add(t);
//                    else
                    array.add(0, t);
                    //  array.add(t);

                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Comment c = new Comment();
            c.setIsEditText(true);
            // array.add(c);
            adapter = new CommentListtAdapter(PopUpTicketActivity.this,
                    R.layout.comment_item, array);
            commentlist.setAdapter(adapter);
            setListViewHeightBasedOnChildren(commentlist);
            commentlist.post(new Runnable() {
                public void run() {
                    commentlist.setSelection(commentlist.getCount() - 1);
                }
            });

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);


            //dialog.dismiss();

        }

    }

    public void delete(View v) {
        TextView path = (TextView) findViewById(R.id.path);
        //path.setText(getString(R.string.delete));
        pathimg = "";
        deletelayout.setVisibility(View.GONE);
        rlPicName.setVisibility(View.GONE);
        mediaPath = "";
    }

    class CommentListtAdapter extends ArrayAdapter<Comment> {

        private ArrayList<Comment> Items;
        private final Activity context;
        ImageLoader imageLoader;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;

        public CommentListtAdapter(Activity context, int layout, ArrayList<Comment> items) {
            super(context, layout);

            this.context = context;
            this.Items = items;


            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).build();
        }

        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public Comment getItem(int i) {
            return Items.get(getCount() - i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = context.getLayoutInflater();
                v = inflater.inflate(R.layout.comment_item, null, true);


                holder.message = (TextView) v.findViewById(R.id.message);
                holder.img = (TextView) v.findViewById(R.id.imgUrl);
                holder.rel = (LinearLayout) v.findViewById(R.id.comment_back);
                holder.username = (TextView) v.findViewById(R.id.username);
                holder.userDate = (TextView) v.findViewById(R.id.userDate);
                holder.ivImage = (ImageView) v.findViewById(R.id.ivImage);
                v.setTag(holder);

            } else {
                holder = (ViewHolder) v.getTag();

            }

            holder.userDate.setText(Items.get(position).getCreationDate());
            holder.username.setText(Items.get(position).getUsername());
            holder.message.setText(Items.get(position).getMessage());


            Log.wtf("getImageUrl", Items.get(position).getImageUrl());
            if (!Items.get(position).getFileName().equals("") && !Items.get(position).getFileName().equals("-")) {

                if (Items.get(position).getImageUrl().contains(".pdf")) {
                    holder.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.attach));
                    holder.ivImage.setColorFilter(ContextCompat.getColor(context, R.color.black));
                    holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    imageLoader.displayImage(Items.get(position).getImageUrl(),
                            holder.ivImage, options, animateFirstListener);
                }

                holder.img.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent i = new Intent();
                        i.setClass(PopUpTicketActivity.this,
                                PopUpImageActivity.class);
                        Bundle b = new Bundle();
                        String g = Items.get(position).getImageUrl();
                        b.putString("img", g);
                        i.putExtras(b);
                        startActivity(i);

                    }
                });
                holder.img.setTag(Items.get(position).getImageUrl());
                try {
                    holder.img.setText(getResources().getString(
                            R.string.view_file));
                } catch (Exception e) {

                }
                if (MyApplication.nightMod)
                    holder.img.setTextColor(getResources().getColor(
                            R.color.nightBlue));
                else
                    holder.img.setTextColor(getResources().getColor(
                            R.color.bordo));

            } else {

                Log.wtf("ma fe", "kabse");
                holder.ivImage.setVisibility(View.GONE);
            }


            holder.ivImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent();
                    i.setClass(PopUpTicketActivity.this,
                            PopUpImageActivity.class);
                    Bundle b = new Bundle();
                    String g = Items.get(position).getImageUrl();
                    b.putString("img", g);

                    i.putExtras(b);
                    startActivity(i);

                }
            });


            if (Boolean.parseBoolean(Items.get(position).getIsAdmin())) {
                if (MyApplication.nightMod)
                    holder.rel.setBackgroundColor(context.getResources()
                            .getColor(R.color.lightblueforadmin));
                else
                    holder.rel.setBackgroundColor(context.getResources()
                            .getColor(R.color.rose));
                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    holder.username.setText(getString(R.string.admin) + " | ");
                    String date;
                    try {
                         date = new SimpleDateFormat("yyyy-MM-dd").format(Items.get(position).getCreationDate().split("T")[0]);
                    }catch (Exception e){
                        Log.wtf("chat_exception",e.toString());
                        date="";
                    }
                    holder.userDate.setText(date);
                } else {

                    try {
                        holder.username.setText(getString(R.string.admin) + " | ");
                        DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = inputFormatter1.parse(Items.get(position).getCreationDate().split("T")[0]);
                        String date = inputFormatter1.format(date1);
                        holder.userDate.setText(date);
                    }catch (Exception e){

                    }
                }
            } else {
                if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                    holder.username.setText(getString(R.string.you) + " | ");
                    holder.userDate.setText(Items.get(position).getCreationDate().split("T")[0]);
                } else {
                    try {
                        holder.username.setText(getString(R.string.you) + " | ");
                        DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = inputFormatter1.parse(Items.get(position).getCreationDate().split("T")[0]);
                        String date = inputFormatter1.format(date1);
                        holder.userDate.setText(date);
                    }catch (Exception e){

                    }

                }
                holder.rel.setBackgroundColor(context.getResources().getColor(
                        R.color.gray_light));
            }
            // ViewResizing.setListRowTextResizing(v, context);
            // ViewResizing.textResize(PopUpTicketActivity.this,holder.username,12);
            if (MyApplication.Lang.equals(MyApplication.ENGLISH)) {
                holder.username.setTypeface(MyApplication.facePolarisMedium);
                holder.message.setTypeface(MyApplication.facePolarisMedium);
                holder.userDate.setTypeface(MyApplication.facePolarisMedium);

            } else {
                holder.username.setTypeface(MyApplication.faceDinar);
                holder.message.setTypeface(MyApplication.faceDinar);
                holder.userDate.setTypeface(MyApplication.facePolarisMedium);
            }

            return v;
        }

        public class ViewHolder {
            TextView username, message;
            LinearLayout rel;
            TextView img;
            TextView userDate;
            ImageView ivImage;
        }

    }

    public String getcountrycode1(double lat, double longt) {
        String countryName = "";
        Locale locale;
        if (MyApplication.Lang.equalsIgnoreCase(MyApplication.ENGLISH)) {
            locale = new Locale("en");
        } else {
            locale = new Locale("ar");
        }
        Geocoder gcd = new Geocoder(this, locale);
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat, longt, 1);
            if (addresses.size() > 0) {
                countryName = addresses.get(0).getAddressLine(0) + " "
                        + addresses.get(0).getAddressLine(1) + " "
                        + addresses.get(0).getAdminArea() + " "
                        + addresses.get(0).getSubAdminArea();
                countryName = countryName.replaceAll("null", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return countryName;
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("hello im here", "the value are " + latitude + " "
                    + longitude);
            // loca = (TextView) findViewById(R.id.loclabeldet);
            // loca.setText(getcountrycode1(latitude, longitude));
            address = getcountrycode1(latitude, longitude);
            try {
                //dialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {

            }
            // address = loca.getText().toString();
            if (waitingForLocationUpdate) {
                waitingForLocationUpdate = false;
            }

            if (ActivityCompat.checkSelfPermission(PopUpTicketActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PopUpTicketActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);

        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
        }
    }

    void registerLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            return;
        } else {
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        Location oldLocation = locationManager.getLastKnownLocation(provider);

        if (oldLocation != null) {
            waitingForLocationUpdate = false;
        } else {
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView img;

        public DownloadImage(ImageView img) {
            // TODO Auto-generated constructor stub
            this.img = img;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            InputStream in = null;
            Bitmap bmp = null;

            int responseCode = -1;
            try {

                URL url = new URL(imageURL);// "http://192.xx.xx.xx/mypath/img1.jpg
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
                con.setDoInput(true);
                con.connect();
                responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // download
                    in = con.getInputStream();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();

                }

            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            img.setImageBitmap(result);
            // Close progressdialog

        }
    }

    public void DownloadImageFromPath(String path) {
        InputStream in = null;
        Bitmap bmp = null;
        ImageView iv = (ImageView) findViewById(R.id.img1);
        int responseCode = -1;
        try {

            URL url = new URL(path);// "http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                iv.setImageBitmap(bmp);
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    public void openimage(View v) {
        if (v.getTag() != null) {

            Intent i = new Intent();
            i.setClass(PopUpTicketActivity.this, PopUpImageActivity.class);
            Bundle b = new Bundle();
            String g = (String) v.getTag();
            b.putString("img", g.replaceAll("/", "//"));

            i.putExtras(b);
            startActivity(i);
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        mediaPath = cursor.getString(column_index);
        return cursor.getString(column_index);
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);

    }

    public class sendComment2 extends AsyncTask<Void, Void, String> {
        String token = "", result, feedbacktext = "";
        Comment c = new Comment();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            token = Actions.create_token_new();

            date = getcurrentDate();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Log.wtf("Send", "Comment PRE");

            //dialog.show();
            if (!feedback.getText().toString().equals(""))
                address = feedback.getText().toString();
            else
                address = "";
            feedbacktext = feedback.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String st = "";

            // ArrayList<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // imageBytes.replaceAll("\n", "");
            // nameValuePairs.add(new BasicNameValuePair("UploadedFile",
            // imageBytes));
            // nameValuePairs.add(new BasicNameValuePair("RecordDate",
            // date));
            //
            // nameValuePairs.add(new BasicNameValuePair("Message",
            // feedback.getText().toString()));
            // nameValuePairs.add(new BasicNameValuePair("FileName",
            // filename));
            // nameValuePairs.add(new BasicNameValuePair("token",
            // token));
            // nameValuePairs.add(new BasicNameValuePair(
            // "ReportedIssueId", String.valueOf(Ticketid)));
            // c.setCreationDate("just now");
            // c.setMessage(feedback.getText().toString());
            // c.setIsAdmin("false");
            // c.setIsUser("true");
            // c.setFileName(filename);
            // c.setImageUrl(MyApplication.link + "Files/" + filename);
            //
            // HttpClient httpclient = new DefaultHttpClient();
            // HttpPost httppost = new HttpPost(MyApplication.link
            // + "PostData.asmx/SaveTicketFeedback?");
            // MultipartEntity entity = new MultipartEntity(
            // HttpMultipartMode.STRICT);
            // byte[] bs = imageBytes.getBytes();
            // ByteArrayBody bab = null;
            // if (bs != null)
            //
            // bab = new ByteArrayBody(bs, "UploadedFile");
            //
            // // entity.addPart("UploadedFile", new StringBody(bab,
            // // ContentType.TEXT_PLAIN));
            // entity.addPart("UploadedFile", bab);
            // entity.addPart("RecordDate", new StringBody(date,
            // ContentType.TEXT_PLAIN));
            // entity.addPart("Message", new
            // StringBody(feedback.getText()
            // .toString(), ContentType.TEXT_PLAIN));
            // entity.addPart("FileName", new StringBody(filename,
            // ContentType.TEXT_PLAIN));
            // entity.addPart("token", new StringBody(token,
            // ContentType.TEXT_PLAIN));
            // entity.addPart("ReportedIssueId",
            // new StringBody(String.valueOf(Ticketid),
            // ContentType.TEXT_PLAIN));
            //
            // httppost.setEntity(new
            // UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));
            // HttpResponse response = httpclient.execute(httppost);
            // st = EntityUtils.toString(response.getEntity());
            try {
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 110241024;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(MyApplication.link
                        + "PostFeedback.aspx");
                ContentType contentType = ContentType.create(
                        HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

/* example for setting a HttpMultipartMode */
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (!mediaPath.equals("")) {
                    Log.wtf("fe", "file bg");
                    File file = new File(mediaPath);
                    FileInputStream fileInputStream = new FileInputStream(file);

                    bytesAvailable = fileInputStream.available();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {

                        bos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    fileInputStream.close();
                    byte[] data = bos.toByteArray();
                    if (mediaPath.contains(".jpg")) {
                        filename = "Media" + System.currentTimeMillis()
                                + ".jpg";
                    } else if (mediaPath.contains(".png"))

                        filename = "Media" + System.currentTimeMillis()
                                + ".png";
                    else if (mediaPath.contains(".pdf"))
                        filename = "Media" + System.currentTimeMillis() + ".pdf";
                    ByteArrayBody bab = new ByteArrayBody(data, filename);
                    builder.addPart("UploadedFile", bab);

                    c.setImageUrl(MyApplication.link + "Files/" + filename);


                } else {
                    builder.addTextBody("UploadedFile", "");
                    filename = "";

                }
                builder.addTextBody("FileName", filename);

                builder.addTextBody("Message", address, contentType);

                builder.addTextBody("token", token);
                builder.addTextBody("ReportedIssueId",
                        String.valueOf(Ticketid));
                builder.addTextBody("RecordDate", date);


                httppost.setEntity(builder.build());

                c.setCreationDate(date);
                c.setMessage(address);
                c.setIsAdmin("false");
                c.setIsUser("true");
                c.setFileName(filename);

                // DEBUG

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(response.getEntity());
                // mediaPath="";
                // DEBUG
                //	System.out.println(response.getStatusLine());
//				if (resEntity != null) {
//					System.out.println(EntityUtils.toString(resEntity));
//				} // end if

//				if (resEntity != null) {
//					resEntity.consumeContent();
//				} // end if

                httpclient.getConnectionManager().shutdown();
            } catch (Exception e) {
                Log.d("eee", e + "");

            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //dialog.dismiss();
            Log.wtf("Send", "Comment POST");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);


            feedback.setText("");
            array.add(c);
            adapter.notifyDataSetChanged();
            filename = "";
            image = "";
            commentlist.post(new Runnable() {
                public void run() {
                    commentlist.setSelection(commentlist.getCount() - 1);
                }
            });
            deletelayout.performClick();
            // setListViewHeightBasedOnChildren(commentlist);

        }

    }

}
