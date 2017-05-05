//written by Collin Duddy
//tested by Collin Duddy
//Debugged by all members


package com.JunkAway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by panth_000 on 4/22/2017.
 */

public class RequestAdapter extends ArrayAdapter<Request> {
    private Context context;
    private ArrayList<Request> reqs;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.context=context;
        this.reqs=requests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Request request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_list, parent, false);
        }
        // Lookup view for data population
        TextView req = (TextView) convertView.findViewById(R.id.ReqID);
        TextView date = (TextView) convertView.findViewById(R.id.Date);
        TextView desc = (TextView) convertView.findViewById(R.id.Description);
        TextView pickup = (TextView) convertView.findViewById(R.id.PickupAdd);
        TextView dropoff = (TextView) convertView.findViewById(R.id.DropOffAdd);
        TextView count = (TextView) convertView.findViewById(R.id.ItemCount);
        TextView cost = (TextView) convertView.findViewById(R.id.Cost);
        TextView driveracc = (TextView) convertView.findViewById(R.id.DriverAccepted);
        TextView status = (TextView) convertView.findViewById(R.id.Status);
        TextView user = (TextView) convertView.findViewById(R.id.User);
        TextView completed = (TextView) convertView.findViewById(R.id.IsCompleted);


        // Populate the data into the template view using the data object
        req.setText("Request Number: "+request.getRequestID());
        date.setText("Date: "+request.getDate());
        desc.setText("Description: "+request.getDescription());
        pickup.setText(request.getPickupAddress());
        dropoff.setText(request.getDropOffAddress());
        count.setText(Integer.toString(request.getNumItems()));
        cost.setText(request.getPrice().toString());
        driveracc.setText(request.getDriverName());
        status.setText(request.getCurrStatus());
        user.setText(request.getUser());
        completed.setText(request.getCompleted().toString());
        // Return the completed view to render on screen
        return convertView;
    }
    @Override
    public int getCount() {
        return reqs.size();
    }
}
