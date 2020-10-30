package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SearchAllocation extends AppCompatActivity {
    private Button btnSearch;
    private EditText edtSearch;
    private Helper helper;
    private ProgressDialog pgdialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_allocation);
        helper  = new Helper(getApplicationContext());
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage(getString(R.string.loading));
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recycerlview);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtSearch.getText().toString().trim().equals("")) search();
                else Toast.makeText(getApplicationContext(),"You must search by Police ID or phone number",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void search() {
        pgdialog.show();
        final String url = helper.host + "/assignment.php?cate=search&deployer="+helper.getDataValue("sess_id")+"&keyword="+edtSearch.getText().toString().trim();
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
                                Toast.makeText(getApplicationContext(), "No users found", Toast.LENGTH_LONG).show();
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