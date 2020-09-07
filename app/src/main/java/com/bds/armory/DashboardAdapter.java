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


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    public LinearLayout v;
    public Context ctx;
    public JSONObject readStatusSymbol = new JSONObject();
    private JSONArray mDataset;
    public ImageView imgx;
    public MyViewHolder vh;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DashboardAdapter(Context context, JSONArray myDataset) {
        mDataset = myDataset;
        ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DashboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_dashboard, parent, false);
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
            JSONObject stats = currentObj.getJSONObject("stats");
            String date = currentObj.getString("date"),
            returned = stats.getString("returned"),
            notreturned = stats.getString("not_returned");
            holder.tvDate.setText(date);
            holder.tvReturnedNo.setText(returned);
            holder.tvNotReturnedNo.setText(notreturned);

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
        public TextView tvDate, tvReturnedNo,tvNotReturnedNo;
        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvReturnedNo = view.findViewById(R.id.tvReturnedNo);
            tvNotReturnedNo = view.findViewById(R.id.tvNotReturnedNo);


            //tvMsg = lny.findViewById(R.id.tvRecyclerDate);
        }
    }


}

