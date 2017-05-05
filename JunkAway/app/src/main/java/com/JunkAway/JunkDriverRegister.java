//written by Collin Duddy
//tested by Collin Duddy
//Debugged by all members


package com.JunkAway;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JunkDriverRegister extends AppCompatActivity {

    private EditText mPhone;
    private EditText mAddress;
    private Button submit;
    private View mResetFormView;
    private View mProgressView;
    private User user;
    private CheckBox checkCar;
    private CheckBox checkTruck;
    private CheckBox checkSUV;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String email;
    String fname;
    String lname;
    String vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junk_driver_register);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("User");
        getSupportActionBar().hide();
        email = user.get_email();
        fname = user.get_fname();
        lname = user.get_lname();
        mPhone = (EditText) findViewById(R.id.phonenumber);
        mAddress = (EditText) findViewById(R.id.address);
        checkCar = (CheckBox) findViewById(R.id.checkCar);
        checkCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSUV.setChecked(false);
                checkTruck.setChecked(false);
            }
        });
        checkTruck = (CheckBox) findViewById(R.id.checkTruck);
        checkTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSUV.setChecked(false);
                checkCar.setChecked(false);
            }
        });
        checkSUV = (CheckBox) findViewById(R.id.checkSUV);
        checkSUV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCar.setChecked(false);
                checkTruck.setChecked(false);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVehicleType();
                attemptJunkDriverRegister();
            }
        });
        mResetFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    public void attemptJunkDriverRegister() {// Reset errors.
        mAddress.setError(null);
        mPhone.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhone.getText().toString();
        String address = mAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhone.setError("Invalid phone number entered");
            focusView = mPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(address)) {
            mAddress.setError(getString(R.string.error_field_required));
            focusView = mAddress;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //user.registerUser(fname,lname,email,password);
            // Initialize  AsyncLogin() class with email and password
            new JunkDriverRegister.AsyncJunkDriverRegister().execute(fname, lname, email, address, phone,vehicle);

        }


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mResetFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mResetFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mResetFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mResetFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isPhoneValid(String phone) {
        if (phone.length() == 10) {
            return true;
        } else {
            return false;
        }
    }
    private void setVehicleType()
    {
        if(checkCar.isChecked())
        {
            vehicle="Car";
        }
        else if (checkSUV.isChecked())
        {
            vehicle="SUV";
        }
        else
        {
            vehicle="Truck";
        }
    }

    private class AsyncJunkDriverRegister extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(JunkDriverRegister.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... params) {
            try {

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                System.out.print(ip);
                // Enter URL address where your php file resides
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/RegisterJunkDriver.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("FirstName", params[0])
                        .appendQueryParameter("LastName", params[1])
                        .appendQueryParameter("Email", params[2])
                        .appendQueryParameter("Phone", params[3])
                        .appendQueryParameter("Address", params[4])
                        .appendQueryParameter("Vehicle", params[5]);
                String query = builder.build().getEncodedQuery();
                //user = new User(params[0]);

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            System.out.println(result);
            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(JunkDriverRegister.this, "Junk Driver Profile Creation Successful", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(JunkDriverRegister.this, LoginActivity.class);
                startActivity(intent);
                JunkDriverRegister.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(JunkDriverRegister.this, "Database issue, try becoming a driver later", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(JunkDriverRegister.this,LoginActivity.class);
                startActivity(intent);
                JunkDriverRegister.this.finish();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(JunkDriverRegister.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(JunkDriverRegister.this,LoginActivity.class);
                startActivity(intent);
                JunkDriverRegister.this.finish();
            }

        }
    }
}
