package com.bds.armory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoliceRequests extends Fragment {

    private Context ctx;
    private Helper helper;
    private ProgressDialog pgdialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnNewRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_police_requests, container, false);
        ctx = view.getContext();
        helper = new Helper(ctx);
        recyclerView = view.findViewById(R.id.recycerlview);
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        pgdialog = new ProgressDialog(ctx);
        pgdialog.setMessage(ctx.getString(R.string.loading));
        btnNewRequest = view.findViewById(R.id.btnNewRequest);
        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx,SendRequests.class));
            }
        });
        loadData();
        return view;
    }
    public void loadData() {
        pgdialog.show();
        final String url = helper.host + "/requests.php?cate=bypolice&police="+helper.getDataValue("sess_id");
       // Toast.makeText(ctx,url,Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(ctx);
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pgdialog.dismiss();
                        Log.d("Notices response", response.toString());
                        //sync on local storage
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0)
                                Toast.makeText(ctx, "No request found", Toast.LENGTH_LONG).show();
                            else {
                                extractData(array);
                                RequestsAdapter adapter = new RequestsAdapter(ctx,array);
                                recyclerView.setAdapter(adapter);
                            }
//                            progressDialog.dismiss();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgdialog.dismiss();
//                        Log.d("Load notices err", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }
    public void extractData(JSONArray arr){
        String[] dataArr = new String[arr.length()-1];
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        try{
            for (int i=0; i < arr.length();i++){
                JSONObject obj = arr.getJSONObject(i);
                list.add(obj.getString(obj.getString("reason")));
            }
        }catch (JSONException ex){

        }
        adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,list);
//        listView.setAdapter(adapter);
    }
}