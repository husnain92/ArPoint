package com.pk_eagle.arpoint;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Eagle on 25-Mar-16.
 */
public class Products extends Fragment {

    SearchView svProduct;
    ListView listProducts;

    StringBuilder stringBuilder;
    ArrayList<ModelProducts> productListData=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_list,
                container, false);

        svProduct = (SearchView) view.findViewById(R.id.etSearchQuery);
        listProducts = (ListView) view.findViewById(R.id.listProducts);



        new getProducts().execute();




        return view;
    }


    public class getProducts extends AsyncTask<Void, Void, Void> {
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
            nameValuePairs.add(new BasicNameValuePair("app_id", "1"));
            nameValuePairs.add(new BasicNameValuePair("hint", ""));
            nameValuePairs.add(new BasicNameValuePair("limit", ""));
            nameValuePairs.add(new BasicNameValuePair("offset", ""));

            ServiceHandler sh = new ServiceHandler();
            stringBuilder = new StringBuilder();
            stringBuilder = sh.makeServiceCall("http://arpointapp.com/api/get-products.php", ServiceHandler.POST, nameValuePairs);


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
                    else
                    {

                        if(productListData!=null)
                            productListData.clear();

                        for(int x=0;x<jarray.length();x++)
                        {
                            j=jarray.getJSONObject(x);
                            ModelProducts m=new ModelProducts();
                            m.setProduct_ID(j.getString("product_id"));
                            m.setProduct_Name(j.getString("product_name"));
                            m.setProduct_Image("http://"+j.getString("images"));
                            m.setProduct_Price(j.getString("product_price"));
                            m.setProduct_Details(j.getString("product_detail"));
                            productListData.add(m);
                        }
                        Adapter_Products AdapterProd = new Adapter_Products(getActivity(), productListData);
                        listProducts.setAdapter(AdapterProd);
                    }

                }
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            } catch (Exception e) {
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            }


        }
    }

}
