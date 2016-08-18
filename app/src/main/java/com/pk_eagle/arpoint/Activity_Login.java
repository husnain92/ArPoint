package com.pk_eagle.arpoint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Activity_Login extends AppCompatActivity {


    EditText etEmail, etPassword;
    Button btnLogin;

    StringBuilder stringBuilder;
    String Email, Password;

    TextView tvRegNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        etEmail = (EditText) findViewById(R.id.etLoginEmail);
        etPassword = (EditText) findViewById(R.id.etLoginPass);

        tvRegNow = (TextView) findViewById(R.id.tvRegNow);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        String htmlString = "<u>Not Registered?\nSign Up Now!</u>";
        tvRegNow.setText(Html.fromHtml(htmlString));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().length() > 0 && etPassword.getText().length() > 0) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                        Email = etEmail.getText().toString();
                        Password = etPassword.getText().toString();
                        new UserLogin().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Provide Complete Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvRegNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Activity_Login.this, Activity_SignUp.class));

            }
        });
    }


    public class UserLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(Activity_Login.this, "Logging In...", false);
                AppUtils.progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("signin", "true"));

            nameValuePairs.add(new BasicNameValuePair("email", Email));
            nameValuePairs.add(new BasicNameValuePair("password", Password));

            ServiceHandler sh = new ServiceHandler();
            stringBuilder = new StringBuilder();
            stringBuilder = sh.makeServiceCall("http://arpointapp.com/api/signin.php", ServiceHandler.POST, nameValuePairs);


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
                        AppUtils.showToast(getApplicationContext(), j.getString("message"), 2000);


                    } else {

                        String uid = j.getString("user_id");
                        String FirstName = j.getString("first_name");
                        String LastName = j.getString("last_name");
                        String Email = j.getString("email");


                        UtilSharedPref.saveKeyToSharedPreferences(getApplicationContext(), UtilSharedPref.USERID, uid);
                        UtilSharedPref.U_ID = uid;
                        UtilSharedPref.saveKeyToSharedPreferences(getApplicationContext(), UtilSharedPref.USERFIRSTNAME, FirstName);
                        UtilSharedPref.U_NAME = FirstName;
                        UtilSharedPref.saveKeyToSharedPreferences(getApplicationContext(), UtilSharedPref.USERLASTNAME, LastName);
                        UtilSharedPref.U_FULLNAME = LastName;
                        UtilSharedPref.saveKeyToSharedPreferences(getApplicationContext(), UtilSharedPref.USEREMAIL, Email);
                        UtilSharedPref.U_STATE = Email;

                        Intent i = new Intent(Activity_Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        AppUtils.showToast(getApplicationContext(), "You are Login Successfully", 2000);
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
