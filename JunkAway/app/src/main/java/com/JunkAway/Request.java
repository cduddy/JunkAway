//written by Collin Duddy
//tested by Collin Duddy
//Debugged by all members


package com.JunkAway;

import android.content.ClipData;
import android.content.Context;
import android.location.Location;

import java.io.Serializable;

/**
 * Created by panth_000 on 4/20/2017.
 */

public class Request implements Serializable {
    private String RequestID;
    private String email;
    private String Pickup;//maybe switch to location for gmaps api
    private String DropOff;
    private int numItems;
    private String Description;
    private double price;
    private String DriverName;
    private boolean isCompleted;
    private String currStatus;
    private String Date;
    public Request(User account) {
        RequestID = "0";
        email = account.get_email();
        Pickup = null;
        DropOff = null;
        Date = "";
        numItems = 0;
        Description = "";
        price = 0.00;
        DriverName = "";
        isCompleted = false;
        currStatus = "Open";

    }
    //Constructor for ActiveRequest Class
    public Request(String Reqid,String user,String PickupAdd,String DropOffAdd,String Datef,String ItemCount,String Desc, String Cost,String Driver,String IsComplete, String status)
    {
        RequestID= Reqid;
        Date = Datef;
        Description=Desc;
        Pickup=PickupAdd;
        DropOff=DropOffAdd;
        email=user;
        numItems=Integer.parseInt(ItemCount);
        price=Double.parseDouble(Cost);
        DriverName=Driver;
        currStatus=status;
        if(IsComplete.equals("false"))
        {
            isCompleted=false;
        }
        else
        {
            isCompleted=true;
        }
    }
    public String getRequestID()
    {
        return RequestID;
    }
    public String getDate()
    {
        return Date;
    }
    public String getUser()
    {
        return email;
    }
    public String getDescription()
    {
        return Description;
    }
    public String getPickupAddress()
    {
        return Pickup;
    }
    public String getDropOffAddress()
    {
        return DropOff;
    }
    public int getNumItems()
    {
        return numItems;
    }
    public Double getPrice()
    {
        return price;
    }
    public String getDriverName()
    {
        return DriverName;
    }
    public String getCurrStatus()
    {
        return currStatus;
    }
    public Boolean getCompleted()
    {
        return isCompleted;
    }
}
