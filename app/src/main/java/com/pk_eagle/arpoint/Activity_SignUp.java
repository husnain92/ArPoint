package com.pk_eagle.arpoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Activity_SignUp extends AppCompatActivity {


    EditText etFirstName,etLastName,etEmail,etPassword;
    CheckBox chkLetter;
    Button btnSignUp;

    String FirstName,LastName,Email,Password;
    String isLetter="false";

    StringBuilder stringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sign_up);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        etFirstName=(EditText) findViewById(R.id.etFirstName);
        etLastName=(EditText) findViewById(R.id.etLastName);
        etEmail=(EditText) findViewById(R.id.etEmailAddress);
        etPassword=(EditText)findViewById(R.id.etPassword);

        chkLetter=(CheckBox) findViewById(R.id.checkBox);

        btnSignUp=(Button) findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etFirstName.getText().length()>0&&etLastName.getText().length()>0&&etPassword.getText().length()>0&&etEmail.getText().length()>0)
                {

                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                        FirstName = etFirstName.getText().toString();
                        LastName = etLastName.getText().toString();
                        Email = etEmail.getText().toString();
                        Password = etPassword.getText().toString();
                        if (chkLetter.isChecked()) {
                            isLetter = "1";
                        } else {
                            isLetter = "0";
                        }
                        new SignupUser().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Provide Complete Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class SignupUser extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(Activity_SignUp.this, "Processing...", false);
                AppUtils.progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("signup", "true"));
            nameValuePairs.add(new BasicNameValuePair("fname", FirstName));
            nameValuePairs.add(new BasicNameValuePair("lname", LastName));
            nameValuePairs.add(new BasicNameValuePair("email", Email));
            nameValuePairs.add(new BasicNameValuePair("newsletter", isLetter));
            nameValuePairs.add(new BasicNameValuePair("password", Password));
            nameValuePairs.add(new BasicNameValuePair("device", "android"));
            nameValuePairs.add(new BasicNameValuePair("token", "asdasdasd234234"));

            ServiceHandler sh = new ServiceHandler();
            stringBuilder = new StringBuilder();
            stringBuilder = sh.makeServiceCall("http://arpointapp.com/api/signup.php", ServiceHandler.POST, nameValuePairs);



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
                        Intent i = new Intent(Activity_SignUp.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        AppUtils.showToast(getApplicationContext(), "You are Login Successfully", 2000);
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
