package com.bds.armory;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Assignment extends Fragment {

    private Context ctx;
    private Helper helper;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnAssignWeapon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_assignment, container, false);
        ctx = view.getContext();
        helper = new Helper(ctx);
        recyclerView = view.findViewById(R.id.recycerlview);
        btnAssignWeapon = view.findViewById(R.id.btnAssignWeapon);
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);

        btnAssignWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx,Assign.class));
            }
        });

        loadData();
        return view;
    }
    public void loadData() {
        final String url = helper.host + "/assignment.php?cate=bydeployer&deployer="+helper.getDataValue("sess_id");
        RequestQueue queue = Volley.newRequestQueue(ctx);
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.d("Notices response", response.toString());
                        //sync on local storage
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0)
                                Toast.makeText(ctx, "Nta amakuru abonetse", Toast.LENGTH_LONG).show();
                            else {
                                AssignmentAdapter adapter = new AssignmentAdapter(ctx, array);
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
//                        Log.d("Load notices err", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }
}