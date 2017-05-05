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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

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

import static android.content.Context.WIFI_SERVICE;

public class RegisterUser extends AppCompatActivity {
    private EditText mPassword;
    private EditText mEmail;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mRePassword;
    private Button submit;
    private View mResetFormView;
    private View mProgressView;
    private User user;
    private CheckBox checkbox;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String email ;
    String fname;
    String lname ;
    String isJunkDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();
        mPassword = (EditText) findViewById(R.id.password);
        mRePassword = (EditText) findViewById(R.id.repassword);
        mEmail = (EditText) findViewById(R.id.email);
        mFirstName = (EditText) findViewById(R.id.fname);
        mLastName = (EditText) findViewById(R.id.lname);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked())
                {
                    isJunkDriver="True";
                }
                else
                {
                    isJunkDriver="False";
                }
                attemptRegister();
            }
        });
        mResetFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);
        checkbox = (CheckBox) findViewById(R.id.junkdrivercheck);

    }
    public void attemptRegister()
    {// Reset errors.
        mPassword.setError(null);
        mRePassword.setError(null);
        mEmail.setError(null);
        mFirstName.setError(null);
        mLastName.setError(null);;
    // Store values at the time of the login attempt.
         email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String repassword = mRePassword.getText().toString();
         fname = mFirstName.getText().toString();
         lname = mLastName.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
        mPassword.setError(getString(R.string.error_invalid_password));
        focusView = mPassword;
        cancel = true;
    }
    if (TextUtils.isEmpty(email))
    {
        mEmail.setError(getString(R.string.error_field_required));
        focusView = mEmail;
        cancel = true;
    }
    else if (TextUtils.isEmpty(fname))
    {
        mFirstName.setError(getString(R.string.error_field_required));
        focusView = mFirstName;
        cancel = true;
    }
    else if (TextUtils.isEmpty(lname))
    {
        mLastName.setError(getString(R.string.error_field_required));
        focusView = mLastName;
        cancel = true;
    }
    else if (TextUtils.isEmpty(password))
    {
        mPassword.setError(getString(R.string.error_field_required));
        focusView = mPassword;
        cancel = true;
    }
    else if (TextUtils.isEmpty(repassword))
    {
        mRePassword.setError(getString(R.string.error_field_required));
        focusView = mRePassword;
        cancel = true;
    }
    // Check for a valid email address.
   if (!isEmailValid(email)) {
        mEmail.setError(getString(R.string.error_invalid_email));
        focusView = mEmail;
        cancel = true;
    }
    if (!(password.equals(repassword)))
    {
        mPassword.setError("Passwords don't match");
        focusView = mPassword;
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
        new RegisterUser.AsyncRegister().execute(fname,lname,email,password,isJunkDriver);

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
    private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
}
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
private class AsyncRegister extends AsyncTask<String, String, String> {
    ProgressDialog pdLoading = new ProgressDialog(RegisterUser.this);
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
            url = new URL("https://people.eecs.ku.edu/~cduddy/JunkAway/RegisterUser.php");

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
                    .appendQueryParameter("Password", params[3])
                    .appendQueryParameter("isJunkDriver", params[4]);
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
            Toast.makeText(RegisterUser.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterUser.this, RegisterUser.class);
            //user.set_fname(user.phpFname());//pulls the user's first name only in the case where a successful connection was acieved to SQL db
            //user.set_lname(user.phpLname());
            //intent.putExtra("User", user);
            startActivity(intent);
            RegisterUser.this.finish();

        } else if (result.equalsIgnoreCase("account exists")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
            Toast.makeText(RegisterUser.this, "An account for this E-mail Exists", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterUser.this, LoginActivity.class);

            RegisterUser.this.finish();
            startActivity(intent);
        } else //if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful"))
        {

            Toast.makeText(RegisterUser.this, "User Creation Successful", Toast.LENGTH_LONG).show();
            if (checkbox.isChecked()) {

                Intent intent = new Intent(RegisterUser.this, JunkDriverRegister.class);
                user = new User(email,fname,lname);
                intent.putExtra("User", user);
                startActivity(intent);
                RegisterUser.this.finish();
            } else {
                Intent intent = new Intent(RegisterUser.this, LoginActivity.class);
                startActivity(intent);
                RegisterUser.this.finish();
            }
        }

    }
}
}

