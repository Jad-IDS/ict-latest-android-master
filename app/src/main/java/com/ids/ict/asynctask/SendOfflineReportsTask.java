package com.ids.ict.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.ids.ict.Actions;
import com.ids.ict.classes.Connection;
import com.ids.ict.classes.Mail_OFF;
import com.ids.ict.MyApplication;
import com.ids.ict.TCTDbAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by user on 12/23/2016.
 */

public class SendOfflineReportsTask extends AsyncTask<Void, Void, Integer>  {

    com.ids.ict.Error error;
    ArrayList<Mail_OFF> offlineReports;
    Mail_OFF topReport;
    TCTDbAdapter datasource;
    Activity activity;
    String s = "", gettok;
    int k = 0 ;

    public SendOfflineReportsTask(Activity activity) {

        this.activity = activity;
    }


    public String gettoken() {
        String name = "";
        try {
            URL url = new URL("" + MyApplication.link
                    + "PostData.asmx/StartSendingReport?password=" + topReport);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("string");
            Element nameElement = (Element) nodeList.item(0);
            nodeList = nameElement.getChildNodes();
            name = ((Node) nodeList.item(0)).getNodeValue();
        } catch (Exception e) {
            System.out.println("gg " + e);
            String url1 = "" + MyApplication.link
                    + "PostData.asmx/VerifyRegistration";
            Connection conn = new Connection(url1);
            try {
                String error_return = conn.executeMultipartPost_Send_Error(this
                                .getClass().getSimpleName(), Actions.getDeviceName(),
                        "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return name;
    }


    @Override
    public void onPreExecute() {
        datasource = new TCTDbAdapter(activity);
        datasource.open();
        offlineReports = datasource.getAllReports_off();
        topReport = offlineReports.get(0);

        gettok = gettoken();
    }



    @Override
    public Integer doInBackground(Void... params) {
        Log.d("launching", "passed here");
        try {
            Connection conn = null;
            conn = new Connection("" + MyApplication.link  + "PostData.asmx/SendReport");
            try {
                /*s ="SS" conn.executeMultipartPost_event("PostData.asmx/SendReport3", gettok, *//* mobilenumber *//* topReport.getnum(), affmobile,
                        *//* qatarid *//* topReport.getqatarID(), *//* email *//* topReport.getemail(), *//* issueid *//* topReport.getissueid(), *//* spid *//* topReport.getspid(),
                        "1", createdate, sendingdate, *//* comments *//* topReport.getcomm(),*//* locx *//* topReport.getlocx(),
                         *//* locy *//* topReport.getlocy(), *//* countryid *//* topReport.getcountid(), *//* affqatarid *//* topReport.getaffecqatarid(), *//* spname *//* topReport.getspname(),
                        IssueType, //issue type w nzol kelo msh mawjod
                        spComplaint, longWait, cmplNum, cmplDate, callNum,
                        calldate, isIndividual, isCorporate, cRNumber, "6",
                        channelusedid, isspecialneed, specialneednumber, dateOfRequest, areaId, subAreaId, street, building, zone);*/
                System.out.println("String  " + s);
                String m = splitres(s);
                try {
                    k = Integer.parseInt(m);
                } catch (Exception e) {
                    k = 0;
                }


            } catch (Exception e) {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    @Override
    public void onPostExecute(Integer result) {

            //dialog.dismiss();
            /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBarLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (result == 0) {
                IssueType = "-1";
            }
            Intent intent = new Intent(SendReportActivity.this,
                    SuccessReportConfirmActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("IssueType", IssueType);
            intent.putExtras(bundle);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SendReportActivity.this.finish();
        } catch (Exception e) {
            String url1 = "" + app.link
                    + "PostData.asmx/VerifyRegistration";
            Connection conn = new com.ids.ict.classes.Connection(url1);

            try {
                String error_return = conn.executeMultipartPost_Send_Error(
                        this.getClass().getSimpleName(),
                        Actions.getDeviceName(), "1", e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }*/
    }


    public String splitres(String res) {
        String[] a = res.split(">");
        String[] b = a[2].split("</");
        return b[0];
    }
}