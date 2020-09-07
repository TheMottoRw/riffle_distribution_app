package com.bds.armory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {
    public LinearLayout v;
    public Context ctx;
    public JSONObject readStatusSymbol = new JSONObject();
    private JSONArray mDataset;
    public ImageView imgx;
    public MyViewHolder vh;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AssignmentAdapter(Context context, JSONArray myDataset) {
        mDataset = myDataset;
        ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_assignment, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            final JSONObject currentObj = mDataset.getJSONObject(position);
            String returnedOn = currentObj.getString("returned_on"),
                    assignedOn = currentObj.getString("assigned_on").substring(0,10),
                    workdate = currentObj.getString("work_date").substring(0,10),
                    serial = currentObj.getString("weapon_serial_number");
            returnedOn = returnedOn.equals("null")?"-":returnedOn;
            holder.tvPolice.setText(currentObj.getString("police_name"));
            holder.tvPost.setText(currentObj.getString("post_name"));
            holder.tvWeapon.setText(serial.equals("null")?"None":serial);
            holder.tvWorkdate.setText(workdate);
            holder.tvAssigned.setText(assignedOn);
            holder.tvReturned.setText(returnedOn);
            //loading image attached to text
           holder.btnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag","Scan QR Code");
                }
            });

        } catch (JSONException ex) {

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvPolice, tvPost,tvWeapon, tvWorkdate,tvAssigned,tvReturned;
        public Button btnAssign;
        public MyViewHolder(View view) {
            super(view);
            tvPolice = view.findViewById(R.id.tvPolice);
            tvPost = view.findViewById(R.id.tvPost);
            tvWeapon = view.findViewById(R.id.tvWeapon);
            tvWorkdate = view.findViewById(R.id.tvWorkdateOn);
            tvAssigned = view.findViewById(R.id.tvAssignedOn);
            tvReturned = view.findViewById(R.id.tvReturnedOn);
            btnAssign = view.findViewById(R.id.btnAssign);


            //tvMsg = lny.findViewById(R.id.tvRecyclerDate);
        }
    }


}

