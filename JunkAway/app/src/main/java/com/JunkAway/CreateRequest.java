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
import android.widget.Spinner;
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

public class CreateRequest extends AppCompatActivity {
    private EditText mDropOffLocation;
    private EditText mPickupLocation;
    private Spinner mNumItems;
    private EditText mPrice;
    private EditText mDescription;
    private EditText mDate;
    private Button submit;
    private View mRequestForm;
    private View mProgressView;
    private View mNumItemsView;
    private User user;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String pickup;
    String dropoff;
    String description;
    String date;
    double price;
    int numItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");
        getSupportActionBar().hide();
        mDropOffLocation = (EditText) findViewById(R.id.dropoffLocation);
        mPickupLocation = (EditText) findViewById(R.id.pickupLocation);
        mNumItems = (Spinner) findViewById(R.id.numItems);
        mPrice = (EditText) findViewById(R.id.price);
        mDescription = (EditText) findViewById(R.id.description);
        mDate = (EditText) findViewById(R.id.dateRequested);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRequestCreate();
            }
        });
        mRequestForm = findViewById(R.id.request_form);
        mProgressView = findViewById(R.id.login_progress);
        mNumItemsView = findViewById(R.id.SelectNum);
    }
    public void attemptRequestCreate()
    {// Reset errors.
        mDropOffLocation.setError(null);
        mPickupLocation.setError(null);
        //mNumItems.setError(null);
        mPrice.setError(null);
        mDescription.setError(null);
        mDate.setError(null);
        // Store values at the time of the login attempt.
        pickup=mPickupLocation.getText().toString();
        dropoff=mDropOffLocation.getText().toString();
        description=mDescription.getText().toString();
        String stringprice= mPrice.getText().toString();
        date=mDate.getText().toString();

        View focusView = null;
        boolean cancel = false;
        /*if(mNumItems.isSelected()) {
            numItems = Integer.parseInt(mNumItems.getSelectedItem().toString());

        }
        else
        {
            focusView = mNumItemsView;
            cancel=true;
        }*/
        // Check for a valid password, if the user entered one.
        numItems = Integer.parseInt(mNumItems.getSelectedItem().toString());
        if (TextUtils.isEmpty(dropoff)) {
            mDropOffLocation.setError("Please enter a Drop-off Location");
            focusView = mDropOffLocation;
            cancel = true;
        }
        if (TextUtils.isEmpty(pickup)) {
        mPickupLocation.setError("Please enter a Pickup Location");
        focusView = mPickupLocation;
        cancel = true;
        }
        if (TextUtils.isEmpty(date)) {
            mDate.setError("Please enter a date.");
            focusView = mDate;
            cancel = true;
        }
        if (TextUtils.isEmpty(Double.toString(price)))
        {
            mPrice.setError("Please enter an offered price.");
            focusView = mPrice;
            cancel = true;
        }
        else
        {
            try
            {
                price = Double.parseDouble(stringprice); // Make use of autoboxing.  It's also easier to read.
            }
            catch (NumberFormatException e)
            {
                mPrice.setError("Please a valid price, including decimal, and cents");
                focusView = mPrice;
                cancel = true;
            }
        }
        if (TextUtils.isEmpty(description))
        {
            mDescription.setError("Please give your request a description.");
            focusView = mDescription;
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
            String Itemcount = Integer.toString(numItems);
            String GivenPrice = Double.toString(price);
            new CreateRequest.AsyncCreateRequest().execute(user.get_email(),pickup,dropoff,Itemcount,GivenPrice,date,description);//,numItems,price);

        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRequestForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mRequestForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRequestForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRequestForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private class AsyncCreateRequest extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CreateRequest.this);
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
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/CreateRequest.php");

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
                        .appendQueryParameter("Email", params[0])
                        .appendQueryParameter("Pickup", params[1])
                        .appendQueryParameter("Dropoff", params[2])
                        .appendQueryParameter("ItemCount", params[3])
                        .appendQueryParameter("Price", params[4])
                        .appendQueryParameter("Date", params[5])
                        .appendQueryParameter("Description", params[6]);
                String query = builder.build().getEncodedQuery();
                //user = new User(params[0]);

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();//broken
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

            if (result.equalsIgnoreCase("false")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(CreateRequest.this, "Database create issue.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateRequest.this, CreateRequest.class);
                startActivity(intent);
                CreateRequest.this.finish();

            }else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(CreateRequest.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateRequest.this,LoginActivity.class);
                startActivity(intent);
                CreateRequest.this.finish();
            } else //if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful"))
            {
                Toast.makeText(CreateRequest.this, "User Creation Successful", Toast.LENGTH_LONG).show();
                CreateRequest.this.finish();
            }

        }
    }



}
