package com.JunkAway;

/**
 * Created by panth_000 on 4/14/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.view.ViewParent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.WIFI_SERVICE;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class User implements Serializable {
    public String e_mail="";
    public String f_name =  "";
    public String l_name = "";
    public boolean isJunkDriver = false;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context mContext;

    public User() {
        e_mail="";
        f_name =  "";
        l_name = "";
        isJunkDriver = false;
    }
    public User(String userEmail)
    {
        e_mail = userEmail;
        //f_name = call php script to get first name
        //l_name = call php script to get last name
    }
    public String phpFname()
    {
        //pull the fname
        return f_name;
    }
    public String phpLname()
    {
        //pull the lname
        return l_name;
    }
    public User (String userEmail, String fname, String lname)
    {
        e_mail = userEmail;
        f_name = fname;
        l_name = lname;
    }
    public void set_fname(String name)
    {
        f_name=name;
    }
    public String get_fname()
    {
        return f_name;
    }
    public void set_lname(String name)
    {
        l_name=name;
    }
    public String get_lname()
    {
        return l_name;
    }
    public void set_email(String email)
    {
        e_mail=email;
    }
    public String get_email()
    {
        return e_mail;
    }
    public boolean getJJ()
    {
        return isJunkDriver;
    }
    public void setJJ(boolean tf)
    {
        isJunkDriver=tf;
    }
    /*public void getAllUserData()
    {
        new User.AsyncGetUser().execute(e_mail);

    }*/

}
