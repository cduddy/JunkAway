package com.JunkAway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ViewRequest extends AppCompatActivity {
    private Request request;
    private TextView mReqID;
    private TextView mUser;
    private TextView mPickupAdd;
    private TextView mDropOffAdd;
    private TextView mNumItems;
    private TextView mPrice;
    private TextView mDriver;
    private TextView mStatus;
    private TextView mDescription;
    private String callingClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        Intent i = getIntent();
        request = (Request) i.getSerializableExtra("Request");
        callingClass = (String) i.getSerializableExtra("Calling Activity");
        mReqID = (TextView) findViewById(R.id.requestNum);
        mUser = (TextView) findViewById(R.id.UserName);
        mPickupAdd = (TextView) findViewById(R.id.PickupAdd);
        mDropOffAdd = (TextView) findViewById(R.id.dropoffAdd);
        mNumItems = (TextView) findViewById(R.id.numItems);
        mPrice = (TextView) findViewById(R.id.priceCost);
        mDriver = (TextView) findViewById(R.id.driverAccepted);
        mStatus = (TextView) findViewById(R.id.Status);
        mDescription = (TextView) findViewById(R.id.description);
        if(callingClass.equals("Active Requests")||callingClass.equals("Complete Requests"))
        {
            mUser.setVisibility(View.GONE);
        }
        else
        {
            mDriver.setVisibility(View.GONE);
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


    }
}
