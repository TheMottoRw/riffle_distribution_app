package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchPolice extends AppCompatActivity {
    private Helper helper;
    private ListView listView;
    private EditText edtSearch;
    private JSONArray userDatas;
    private ArrayList<String> searchDatas;
    private ArrayAdapter<String> adapter;
    private ProgressDialog pgdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_police);
        helper = new Helper(this);
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage("Loading data...");
        pgdialog.setCancelable(false);

        listView = findViewById(R.id.listView);
        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(edtSearch.getText().toString().trim().equals("")) extractData(userDatas);
                else searchFromLocalData();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject obj = userDatas.getJSONObject(i);
                    String dataArr = obj.getString("police_id");
                    //Toast.makeText(getApplicationContext(),dataArr,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("police", dataArr);
                    setResult(RESULT_OK, intent);
                    finish();
                }catch (JSONException ex){

                }
            }
        });
        loadData();
    }
    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
    public void loadData() {
        pgdialog.show();
        final String url = helper.host + "/police.php?cate=load&deployer="+helper.getDataValue("sess_id");
        RequestQueue queue = Volley.newRequestQueue(this);
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.d("Notices response", response.toString());
                        pgdialog.dismiss();
                        //sync on local storage
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0)
                                Toast.makeText(getApplicationContext(), "No users found", Toast.LENGTH_LONG).show();
                            else {
                                userDatas = array;
                                extractData(array);
                            }
//                            progressDialog.dismiss();
                        } catch (JSONException ex) {
                            pgdialog.dismiss();
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
    public void searchFromLocalData(){
        searchDatas = new ArrayList<String>();
        try{
            for (int i=0; i < userDatas.length();i++){
                JSONObject obj = userDatas.getJSONObject(i);
                if(obj.getString("name").contains(edtSearch.getText().toString()) || obj.getString("police_id").contains(edtSearch.getText().toString()) || obj.getString("phone").contains(edtSearch.getText().toString()))
                    searchDatas.add(obj.getString("name")+" - "+obj.getString("police_id"));
            }
        }catch (JSONException ex){

        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,searchDatas);
        listView.setAdapter(adapter);
    }
    public void extractData(JSONArray arr){
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        try{
            for (int i=0; i < arr.length();i++){
                JSONObject obj = arr.getJSONObject(i);
                list.add(obj.getString("name")+" - "+obj.getString("police_id"));
            }
        }catch (JSONException ex){

        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }
}