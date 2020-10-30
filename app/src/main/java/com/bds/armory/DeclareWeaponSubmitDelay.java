package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeclareWeaponSubmitDelay extends AppCompatActivity {

    private Helper helper;
    private ProgressDialog pgdialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText edtReason,edtName;
    private Button btnSave;
    String id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_weapon_submit_delay);
        helper = new Helper(this);
        pgdialog = new ProgressDialog(this);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("police");
        edtName = findViewById(R.id.edtUsername);
        edtReason = findViewById(R.id.edtReason);
        btnSave = findViewById(R.id.btnSave);
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage(getString(R.string.senddata));

        edtName.setText(name);
        //Toast.makeText(getApplicationContext(),id+"-"+name,Toast.LENGTH_LONG).show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declareWeapon();
            }
        });

    }
    public void declareWeapon() {
        pgdialog.show();
        final String url = helper.host + "/assignment.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.d("Tag",response);
                        pgdialog.dismiss();
                        try {
                            JSONObject array = new JSONObject(response);
                            if (array.getString("status").equals("ok")) {
                                helper.showToast("Declaration done success");
                                edtReason.setText("");
                            } else {
                                helper.showToast("Failed to register declaration "+response);
                            }
                        } catch (JSONException ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgdialog.dismiss();
                        Log.e("jsonerr", "JSON Error " + (error != null ? error.getMessage() : ""));
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cate", "declare");
                params.put("id", id);
                params.put("reason", edtReason.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", helper.getDataValue("appid"));//put your token here
                return headers;
            }
        };
        ;

// add it to the RequestQueue
        queue.add(getRequest);
    }

}