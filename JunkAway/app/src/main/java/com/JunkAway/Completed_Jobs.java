package com.JunkAway;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Completed_Jobs extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    private View mProgressView;
    // Creating JSON Parser object
    ArrayList<HashMap<String, String>> requestsList;

    // url to get all products list
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "requests";

    private Request request;
    private User user;
    private TextView nothingfound;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    ArrayList<Request> arrayOfRequests=new ArrayList<Request>();
    RequestAdapter adapter;
    // products JSONArray
    //JSONArray requests = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active__requests);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");
        // Loading products in Background Thread
        String email = user.get_email();
        nothingfound = (TextView) findViewById(R.id.nothingFoundMSG);
        mProgressView = findViewById(R.id.request_progress);

        adapter = new RequestAdapter(this,arrayOfRequests);
        new AsyncGetRequests().execute(email);
        ListView listView = (ListView) findViewById(R.id.RequestList);
        listView.setAdapter(adapter);
        //nothingfound.setText(Integer.toString(listView.getCount()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Request req = arrayOfRequests.get(position);//.getItem(position);
                //Log.d("REQ num:",req.getRequestID());
                // Starting new intent
                Intent in = new Intent(Completed_Jobs.this,
                        ViewRequest.class);
                // sending pid to next activity
                in.putExtra("Request", req);
                in.putExtra("Calling Activity","Completed Jobs");
                in.putExtra("User",user);
                // starting new activity and expecting some response back
                startActivity(in);
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mForgotCreate.setVisibility(show ? View.GONE : View.VISIBLE);
            mForgotCreate.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mForgotCreate.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/
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
            // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            //mForgotCreate.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private class AsyncGetRequests extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Completed_Jobs.this);
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
                url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/CompletedJobs.php");

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
                        .appendQueryParameter("email", params[0]);
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
            showProgress(false);
            if (result.equals("")){
                nothingfound.setVisibility(View.VISIBLE);

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(Completed_Jobs.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Completed_Jobs.this,NormalUser_HomeScreen.class);
                startActivity(intent);
                Completed_Jobs.this.finish();
            }else
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Toast.makeText(Completed_Jobs.this, "Retrieved Previous Jobs", Toast.LENGTH_LONG).show();
                String[] requests = result.split("!");
                String[] split;
                String[][] requestssplit = new String[requests.length][11];
                if(requests.length>0) {
                    for(int i=0;i<requests.length;i++)
                    {
                        requestssplit[i]=requests[i].split(":");
                        Request newREQ= new Request(requestssplit[i][0],requestssplit[i][1],requestssplit[i][2],requestssplit[i][3],requestssplit[i][4],requestssplit[i][5],requestssplit[i][6],requestssplit[i][7],requestssplit[i][8],requestssplit[i][9],requestssplit[i][10]);
                        arrayOfRequests.add(newREQ);
                        Log.d("Request Created:",result);
                        Log.d("Request Created:",requestssplit[i][1]);
                        Log.d("Array Length:",arrayOfRequests.toString());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }
}
