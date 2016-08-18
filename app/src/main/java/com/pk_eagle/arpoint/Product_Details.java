package com.pk_eagle.arpoint;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Eagle on 16-Apr-16.
 */
public class Product_Details extends Fragment {


    StringBuilder stringBuilder;
    ViewPager viewPager;
    TextView tvProdTitle,tvProdDetails, tvProdPrice;
    Button btnBuyNow;
    ImageView imgBack;

    String ProdID="";



    int REQUEST_PAYPAL_PAYMENT=11;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID = "AfWLJRjc5D3f46Z1lWI0xfPrlcT1878qAjf2l-EOiiozL14vciEuPWrJj6yI8GG53LY8dSnjvEiLmckb";


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_details,
                container, false);

        viewPager=(ViewPager) view.findViewById(R.id.prodPager);
        tvProdTitle=(TextView) view.findViewById(R.id.tvProdTitle);
        tvProdDetails=(TextView) view.findViewById(R.id.tvProdDetails);
        tvProdPrice=(TextView) view.findViewById(R.id.tvProdPrice);

        imgBack=(ImageView) view.findViewById(R.id.btnBackDetails);

        btnBuyNow=(Button) view.findViewById(R.id.btnBuyNow);

        Bundle args = getArguments();
        ProdID = args.getString("ProdID");

        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);



        new getProdDetails().execute();


        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1),"USD", "androidhub4you.com",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getActivity(), PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);


                startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);



            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee"+confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj=new JSONObject(confirm.toJSONObject().toString());

                        String paymentId=jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-=="+paymentId);
                        Toast.makeText(getActivity(), paymentId, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }




    public class getProdDetails extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Loading...", false);
                AppUtils.progressDialog.show();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("get", "true"));
            nameValuePairs.add(new BasicNameValuePair("product_id", ProdID));

            ServiceHandler sh = new ServiceHandler();
            stringBuilder = new StringBuilder();
            stringBuilder = sh.makeServiceCall("http://arpointapp.com/api/get-product-detail.php", ServiceHandler.POST, nameValuePairs);



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {

                if (stringBuilder != null || !stringBuilder.equals("")) {

                    JSONObject jobj = new JSONObject(stringBuilder.toString());
                    JSONArray jarray = jobj.getJSONArray("response");


                    JSONObject j = jarray.getJSONObject(0);
                    if (j.has("status")) {
                        if (AppUtils.progressDialog != null) {
                            AppUtils.closeDialog();
                            AppUtils.progressDialog = null;
                        }
                        AppUtils.showToast(getActivity().getApplicationContext(), j.getString("message"), 2000);


                    }
                    else {

                        tvProdTitle.setText(j.getString("product_name"));
                        tvProdDetails.setText(j.getString("product_detail"));
                        tvProdPrice.setText("$"+j.getString("product_price")+"/piece");

JSONArray jr=new JSONArray();
                        jr=j.getJSONArray("images");
                       String[] imgs=new String[jr.length()];
                        for(int c=0;c<jr.length();c++)
                        {
//                    String img=jr.getString(c);
//                    imageLoader.displayImage(img, MainImg);
                            imgs[c]="http://"+jr.getJSONObject(c).getString("image");
                        }
                        PagerAdapter adapter = new CustomPager(getActivity(),imgs);
                        viewPager.setAdapter(adapter);

                    }

                    }
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            }
            catch (Exception e)
            {
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            }

        }
    }

}
