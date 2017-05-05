//written by Collin Duddy and Wes Adams
//tested by Collin Duddy and Wes Adams
//Debugged by all members


package com.JunkAway;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.List;

public class ViewRequest extends AppCompatActivity {
    private TextView mReqID;
    private Request request;
    private TextView mUser;
    private TextView mPickupAdd;
    private TextView mDropOffAdd;
    private TextView mNumItems;
    private TextView mPrice;
    private TextView mDriver;
    private TextView mStatus;
    private TextView mDescription;
    private String callingClass;
    private Button mainButton;
    private static final int REQUEST_CODE = 100;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        Intent i = getIntent();
        request = (Request) i.getSerializableExtra("Request");
        callingClass = (String) i.getSerializableExtra("Calling Activity");
        user = (User) i.getSerializableExtra("User");
        mReqID = (TextView) findViewById(R.id.requestNum);
        mUser = (TextView) findViewById(R.id.UserName);
        mPickupAdd = (TextView) findViewById(R.id.PickupAdd);
        mDropOffAdd = (TextView) findViewById(R.id.dropoffAdd);
        mNumItems = (TextView) findViewById(R.id.numItems);
        mPrice = (TextView) findViewById(R.id.priceCost);
        mDriver = (TextView) findViewById(R.id.driverAccepted);
        mStatus = (TextView) findViewById(R.id.Status);
        mDescription = (TextView) findViewById(R.id.description);
        mainButton = (Button) findViewById(R.id.mainButton);
        if(callingClass.equals("Active Requests"))
        {
            mUser.setVisibility(View.GONE);
            mainButton.setText("Cancel this Request");
        }
        else if (callingClass.equals("Past Requests"))
        {
            mUser.setVisibility(View.GONE);
            mainButton.setVisibility(View.INVISIBLE);
        }
        else if (callingClass.equals("Open Jobs"))
        {
            mDriver.setVisibility(View.GONE);
            mainButton.setText("Accept this Job");
        }
        else if (callingClass.equals("Current Jobs"))
        {
            mDriver.setVisibility(View.GONE);
            mainButton.setText("Mark this Job Completed");
        }
        else if (callingClass.equals("Completed Jobs"))
        {
            mDriver.setVisibility(View.GONE);
            mainButton.setVisibility(View.INVISIBLE);
        }
        mReqID.setText("Request ID: " +request.getRequestID());
        mUser.setText("User requested: " +request.getUser());
        mPickupAdd.setText("Pickup Address: " +request.getPickupAddress());
        mDropOffAdd.setText("Dropoff Address: " +request.getDropOffAddress());
        mNumItems.setText("Number of Items: "+Integer.toString(request.getNumItems()));
        mPrice.setText("Price: " +request.getPrice().toString());
        if(!request.getDriverName().equals("false")) {
            mDriver.setText("Driver: " + request.getDriverName());
        }
        else
        {
            mDriver.setText("There is no driver currently assigned to this request");
        }
        mStatus.setText("Current Status: " + request.getCurrStatus());
        mDescription.setText("Short Description: " +request.getDescription());
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTask();
            }
        });

        final TextView pickUpLink = (TextView) findViewById(R.id.PickupAdd);                    //written by Wes Adams
        pickUpLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                 //tested by Wes Adams
                Uri location = Uri.parse("geo:0,0?q=" + request.getPickupAddress());            //Debugged by Wes Adams
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(mapIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(mapIntent);
                }
            }
        });

        final TextView dropOffLink = (TextView) findViewById(R.id.dropoffAdd);                  //written by Wes Adams
        dropOffLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                               //tested by Wes Adams
                Uri location = Uri.parse("geo:0,0?q=" + request.getDropOffAddress());           //Debugged by Wes Adams
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(mapIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(mapIntent);
                }
            }
        });
    }
    private void performTask()
    {
        if(callingClass.equals("Active Requests"))
        {
            new AsyncCancelRequest().execute(request.getRequestID());
        }
        else if (callingClass.equals("Open Jobs"))
        {
            new AsyncAcceptJob().execute(request.getRequestID(),user.get_email());
        }
        else if (callingClass.equals("Current Jobs"))
        {
            new AsyncCompleteJob().execute(request.getRequestID());
        }
    }
    private class AsyncCompleteJob extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ViewRequest.this);
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
            try{

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                //System.out.print(ip);
                // Enter URL address where your php file resides
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/CompleteJob.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("REQID", params[0]);
                String query = builder.build().getEncodedQuery();

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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            //System.out.println(result.toString());
            pdLoading.dismiss();
            //showProgress(false);
            Log.d("Result:",result);
            if (result.equals("true")){
                Toast.makeText(ViewRequest.this, "Job Marked As Completed", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(1,returnIntent);//returning a 1 means the listview needs to update
                ViewRequest.this.finish();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(ViewRequest.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(0,returnIntent);
                ViewRequest.this.finish();
            }else
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(ViewRequest.this,"Job not completed. Try again." , Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(0,returnIntent);
                ViewRequest.this.finish();
            }
        }

    }
    private class AsyncAcceptJob extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ViewRequest.this);
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
            try{

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                //System.out.print(ip);
                // Enter URL address where your php file resides
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/AcceptJob.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("REQID", params[0])
                        .appendQueryParameter("Driver", params[1]);
                String query = builder.build().getEncodedQuery();

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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            //System.out.println(result.toString());
            pdLoading.dismiss();
            //showProgress(false);
            Log.d("Result:",result);
            if (result.equals("true")){
                Toast.makeText(ViewRequest.this, "Job Accepted", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(1,returnIntent);
                ViewRequest.this.finish();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(ViewRequest.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(0,returnIntent);
                ViewRequest.this.finish();
            }else
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(ViewRequest.this,"Job is no longer available. Sorry." , Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(1,returnIntent);//1 means listview must update
                ViewRequest.this.finish();
            }
        }

    }
    private class AsyncCancelRequest extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ViewRequest.this);
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
            try{

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                //System.out.print(ip);
                // Enter URL address where your php file resides
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/CancelRequest.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("REQID", params[0]);
                String query = builder.build().getEncodedQuery();

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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            //System.out.println(result.toString());
            pdLoading.dismiss();
            //showProgress(false);
            Log.d("Result:",result);
            if (result.equals("true")){
                Toast.makeText(ViewRequest.this, "Request Cancelled", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(1,returnIntent);
                ViewRequest.this.finish();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(ViewRequest.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ViewRequest.this,NormalUser_HomeScreen.class);
                //startActivity(intent);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(0,returnIntent);
                ViewRequest.this.finish();
            }else
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Toast.makeText(ViewRequest.this, "Request Not Cancelled. Try Again.", Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(0,returnIntent);
                ViewRequest.this.finish();
            }
        }

    }
}
