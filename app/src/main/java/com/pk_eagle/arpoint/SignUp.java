package com.pk_eagle.arpoint;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Eagle on 25-Mar-16.
 */
public class SignUp extends Fragment {

    EditText etFirstName,etLastName,etEmail,etPassword;
    CheckBox chkLetter;
    Button btnSignUp;

    String FirstName,LastName,Email,Password,NewsLetter;


    StringBuilder stringBuilder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up,
                container, false);


        etFirstName=(EditText) view.findViewById(R.id.etFirstName);
        etLastName=(EditText) view.findViewById(R.id.etLastName);
        etEmail=(EditText) view.findViewById(R.id.etEmailAddress);
        etPassword=(EditText) view.findViewById(R.id.etPassword);

        chkLetter=(CheckBox) view.findViewById(R.id.checkBox);

        btnSignUp=(Button) view.findViewById(R.id.btnSignUp);


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
                            NewsLetter = "1";
                        } else {
                            NewsLetter = "0";
                        }
                        new SignupUser().execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please Enter Valid Email Address.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Please Provide Complete Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    public class SignupUser extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Processing...", false);
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
            nameValuePairs.add(new BasicNameValuePair("password", Password));
            nameValuePairs.add(new BasicNameValuePair("newsletter", NewsLetter));
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
                        AppUtils.showToast(getActivity().getApplicationContext(), j.getString("message"), 2000);


                    } else {

                        String uid = j.getString("user_id");
                        String FirstName = j.getString("first_name");
                        String LastName = j.getString("last_name");
                        String Email = j.getString("email");



                        UtilSharedPref.saveKeyToSharedPreferences(getActivity(), UtilSharedPref.USERID, uid);
                        UtilSharedPref.U_ID = uid;
                        UtilSharedPref.saveKeyToSharedPreferences(getActivity(), UtilSharedPref.USERFIRSTNAME, FirstName);
                        UtilSharedPref.U_NAME = FirstName;
                        UtilSharedPref.saveKeyToSharedPreferences(getActivity(), UtilSharedPref.USERLASTNAME, LastName);
                        UtilSharedPref.U_FULLNAME = LastName;
                        UtilSharedPref.saveKeyToSharedPreferences(getActivity(), UtilSharedPref.USEREMAIL, Email);
                        UtilSharedPref.U_STATE = Email;
                        FragmentManager fragmentManager = getFragmentManager();

                        MainPlaySongs rFragment = new MainPlaySongs();

                        // Creating a fragment transaction
                        FragmentTransaction ft = fragmentManager.beginTransaction();

                        // Adding a fragment to the fragment transaction
                        ft.replace(R.id.content_frame, rFragment, "MainPlay");

                        // Committing the transaction
                        ft.commit();
                        MainActivity.CurrentFragment="MainPlay";
                        AppUtils.showToast(getActivity().getApplicationContext(), "You are Signup Successfully", 2000);
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
