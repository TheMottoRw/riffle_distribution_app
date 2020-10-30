package com.bds.armory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Assign extends AppCompatActivity {
    public Button btnScanSerial, btnScanSubmit, btnRegister;
    public EditText edtSerial, edtPolice;
    public Helper helper;
    public ProgressDialog pgdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        //set arrow back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        edtPolice = findViewById(R.id.edtPoliceId);
        edtSerial = findViewById(R.id.edtSerial);
        btnScanSerial = findViewById(R.id.btnScanSerial);
        btnScanSubmit = findViewById(R.id.btnScanSubmit);
        btnRegister = findViewById(R.id.btnRegister);
        helper = new Helper(Assign.this);
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage(getString(R.string.savingdata));

        btnScanSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Assign.this, BarcodeCaptureActivity.class), 1);
            }
        });
        btnScanSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Assign.this, BarcodeCaptureActivity.class), 2);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignWeapon();
            }
        });

        edtPolice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Assign.this,SearchPolice.class);
                startActivityForResult(intent,3);
                return true;
            }
        });
    }

    public void declareWeaponReturn() {
        pgdialog.show();
        final String url = helper.host + "/assignment.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pgdialog.dismiss();
                        try {
                            JSONObject array = new JSONObject(response);
                            if (array.getString("status").equals("ok")) {
                                helper.showToast("Deassigned succesfull");
                            } else {
                                helper.showToast("Weapon assignment failed");
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
                params.put("cate", "submit");
                params.put("weapon", edtSerial.getText().toString());
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

    public void assignWeapon() {
        pgdialog.show();
        final String url = helper.host + "/assignment.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pgdialog.dismiss();
                        try {
                            JSONObject array = new JSONObject(response);
                            if (array.getString("status").equals("ok")) {
                                helper.showToast("Assigned succesfull");
                            } else {
                                helper.showToast("Weapon assignment failed");
                            }
                        } catch (JSONException ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgdialog.hide();
                        Log.e("jsonerr", "JSON Error " + (error != null ? error.getMessage() : ""));
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cate", "assign");
                params.put("police", edtPolice.getText().toString().trim());
                params.put("weapon", edtSerial.getText().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent obj) {
        super.onActivityResult(requestCode, resultCode, obj);
        if (obj != null) {
            if (requestCode == 1) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    String card = obj.getStringExtra("Barcode");
                    edtSerial.setText(card);
//                    assignWeapon();
                }
            } else if (requestCode == 2) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    Intent intent = obj;
                    String text = intent.getStringExtra("Barcode");
                    edtSerial.setText(text);
                    declareWeaponReturn();
                    //submit caode
                }
            } else if (requestCode == 3) {
                if (resultCode == RESULT_OK) {
                    Intent intent = obj;
                    String text = intent.getStringExtra("police");
                    edtPolice.setText(text);
                    //submit caode
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == 16908332) finish();//back clicked

        return super.onOptionsItemSelected(item);
    }
}