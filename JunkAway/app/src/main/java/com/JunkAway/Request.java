package com.JunkAway;

import android.content.Context;
import android.location.Location;

import java.io.Serializable;

/**
 * Created by panth_000 on 4/20/2017.
 */

public class Request {
    private int RequestID;
    private String email;
    private Location Pickup;//maybe switch to string
    private Location DropOff;
    private int numItems;
    private String Description;
    private double price;
    private String DriverName;
    private boolean isCompleted;
    private String Status;
    public Request(User account) {
        RequestID = 0;
        email = account.get_email();
        Pickup = null;
        DropOff = null;
        numItems = 0;
        Description = "";
        price = 0.00;
        DriverName = "";
        isCompleted = false;
        Status = "Open";
    }
}
