package com.pk_eagle.arpoint;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by Eagle on 12/28/2015.
 */
public class AppUtils {
    public static boolean STARTAPP = false;
    String url = "";
    int method = 0;
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    StringBuilder stringBuilder;
    public static ProgressDialog progressDialog;
    public static Callback callback;



    public static void showDialog(Context mContext, String strMessage) {
        try {
            ProgressDialog pgdialog = new ProgressDialog(mContext);
            if (pgdialog != null)
                if (pgdialog.isShowing())
                    pgdialog.dismiss();
            pgdialog = ProgressDialog.show(mContext, "", strMessage, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ProgressDialog createDialog(Context context, String message, boolean check) {
        ProgressDialog prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage(message);
        // Set Cancelable as False
        prgDialog.setCancelable(check);

        return prgDialog;

    }

    public static void closeDialog() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Throwable e) {
            }
        }
    }


    public static String resizeBase64Image(String base64image){
        byte [] encodeByte=Base64.decode(base64image.getBytes(),Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 400, 400, false);

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }



    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static Toast showToast(Context context, String text, final int LENGTH) {
        Toast toast = Toast.makeText(context, text, LENGTH == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
//        Activity activity = (Activity) context;

//        LayoutInflater inflater = activity.getLayoutInflater();
//
//        View view = inflater.inflate(R.layout.custom_toast, null);
//
//        ((TextView) view.findViewById(R.id.tv)).setText(text);
//
//        toast.setView(view);
        toast.show();
        return toast;
    }


///////////////////////


    public static ArrayList<String> getNumber(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        ArrayList<String> aa=new ArrayList<String>();


        while (phones.moveToNext())
        {
//            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
           String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(".................." + phoneNumber);
            aa.add(phoneNumber);

        }
        phones.close();// close cursor
      return aa;
        //display contact numbers in the list
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    ////////////////////////////////////////////////////////

    public static void setListViewHeightBasedOnChildren(ListView listView) throws Exception {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
    public interface Callback {
        public void onShowHideBar(boolean show);
    }

}


