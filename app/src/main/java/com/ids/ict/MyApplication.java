package com.ids.ict;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.ids.ict.classes.Constants;
import com.ids.ict.classes.RoutineSchedule;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//used to keep global variables
public class MyApplication extends Application {

    public final static boolean sendLogs=true;


    // lang
    public final static String ENGLISH = "ar";
    public final static String ARABIC = "en";
    public final static int SCHEDULER_SECONDS = 60;
    public static String TAG = "Arsel Local Log";
    public static int qosLog = 1;
    public static String qosLogDevices = "";

    // used to calculate text size according to screen size
    public static int GPSCOUNTER = 0;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static Boolean nightMod = false;
    public static int nightMod2 = -1;
    public static boolean animated = false;
    public static boolean test = true;
    public static boolean fromSettings = false;
    public static boolean open = false;

    public static boolean isTesting = false;
    public static String testingTransactionId = "E63A59DB42DA6FED041870FC2E90F0C92CE4F6FF067C1C9D23A5144F51352168";
    public static String testingTransportKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjgA9xF+uYVxLE2BU0A65wAH19zsz5wiC7xF5cNlnLyRgnlZqN5AnyidcYf0BFtL6TWIHeu1YL1j05TID5G59NovNXclecV9csUsaQXiMc5DEtNu1gws+mT2U++X7n3B26LGFw6K1Ujz5yxQg3UZXd1NQQolM/fjg0X9ofUk3jFpjWFVqTWDnOnmEluZx4+WrSuXNYG6juv4VOHXP/MLCCK1UbscFeGUW/KLeQy4zqeYiavY2+ahXbjDSi6EPMufZCMFOAlOyU+tKWyXxp3p50u/rPXlrWkTeqaEx8KpoarommaH34yE693+xlgEcIWVIDx8fMrULR9lR6mbSLGB4WQIDAQAB";
    public static String testingRegId = "b950acdce7354f49ac64e737b8b37c1f";

    //public static boolean themeChanged = false;

    public String mnc = "";
    public String complaint = "";
    public String speedtest = "";
    public String onlinemap = "";
    public String arcomplaint = "";
    public String arspeedtest = "";
    public String aronlinemap = "";
    public static Typeface faceDinar;
    public static Typeface faceDinarLight;
    public static Typeface facePolarisMedium;
    public static String Lang = "";
    public static String appVersion;
    public static String forceUpdate;


    public static String link1 = "http://craservers.arsel.qa/arsel2017/arselservices/";
   // public static String link = "https://arsel.qa/ictservice2/";


      public static String link = "https://arsel.qa/arsel2019/arselservice/";
     // public static String link = "http://arsel.qa/arsel2019/arselservice_demo/";


   // public static String link = "http://arsel.qa/arselservicesstg/";
    public static String link3 = "http://www.arsel.qa/arsel2017/arselservices/";

    //public static String link = "http://136.243.155.109/ictservice/";
    public static String linkpostimage = "http:/arsel.qa/ArselServicesStg/";


    public static String ip = "";
    public static String isp = "";
    public static String connectionType = "";
    public static String post = "PostData.asmx/";
    public static String sms = "SMSService.asmx/";
    public static String general = "GeneralServices.asmx/";
    public static String linkmap2 = "http://136.243.155.109/ictservice/";
    public static String linkmap = "http://www.arsel.qa/arsel2017/arselservices/";
    public static String map = "onlinemapservices.asmx/";
    public static String pass = "";
    public static int lunched = 0;
    public static int UNIQUE_REQUEST_CODE = 0;
    public static Typeface face;


    public static boolean enableQOS=true;
    public static boolean goToQosActivity=true;
    public static RoutineSchedule routineSchedule = new RoutineSchedule();

    public static boolean isNotification=false;
    public static final int TYPE_NOT_CONNECTED = 3;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    public static final String SIGNAL_STRENGTH_TEST = "104";
    public static final String CALL_DISCONNECTION_TEST = "105";

    public static final String ROUTINE_TEST_TRIGGER = "106";
    public static final String SMART_TEST_TRIGGER = "107";
    public static final String HOC_TEST_TRIGGER = "108";

    //<editor-fold desc="async tasks pool section">
    public static boolean writeLog = false;
    public static int corePoolSize = 120;
    public static int  maximumPoolSize = Integer.MAX_VALUE;//100;
    public static int  keepAliveTime = 60;
    public static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    //</editor-fold>



    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")
    private void GetScreenDimesions() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        Log.d("screen width h",screenHeight + ", " + screenWidth);
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

    }
    public void onCreate() {
        if (Constants.Config.DEVELOPER_MODE
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyDeath().build());
        }
        FirebaseApp.initializeApp(this);
        faceDinar = Typeface.createFromAsset(getAssets(),
                "fonts/GE_Dinar_One_Medium.otf");

        faceDinarLight = Typeface.createFromAsset(getAssets(),
                "fonts/GE_Dinar_One_Light.otf");

        facePolarisMedium = Typeface.createFromAsset(getAssets(),
                "fonts/Galaxie_Polaris_Medium.otf");

        super.onCreate();
        //Fabric.with(this, new Crashlytics());

        initImageLoader(getApplicationContext());
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();

        Picasso.setSingletonInstance(built);
        GetScreenDimesions();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
