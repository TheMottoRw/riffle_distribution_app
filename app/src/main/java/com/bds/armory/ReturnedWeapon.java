package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class ReturnedWeapon extends AppCompatActivity {

    private Helper helper;
    private ProgressDialog pgdialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String workdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returned_weapon);
        helper = new Helper(ReturnedWeapon.this);
        pgdialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recycerlview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        workdate = getIntent().getStringExtra("workdate");
        loadData();
    }
    public void loadData() {
        pgdialog.show();
        final String url = helper.host + "/assignment.php?cate=returnedbydate&workdate="+workdate+"&deployer="+helper.getDataValue("sess_id");
        RequestQueue queue = Volley.newRequestQueue(this);
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
                                Toast.makeText(getApplicationContext(), "No assignment found", Toast.LENGTH_LONG).show();
                            else {
                                extractData(array);
                                AssignmentAdapter adapter = new AssignmentAdapter(getApplicationContext(),array);
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
                list.add(obj.getString("police_name")+" - "+obj.getString("police_id"));
            }
        }catch (JSONException ex){

        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//        listView.setAdapter(adapter);
    }
}