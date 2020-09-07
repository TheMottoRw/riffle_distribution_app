package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {
    private EditText edtPhone,edtPassword;
    private Button btnLogin;
    private Helper helper;
    private ProgressDialog pgdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        helper = new Helper(getApplicationContext());
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage(getString(R.string.trylogin));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(edtPhone.getText().toString().trim(),edtPassword.getText().toString());
            }
        });
    }
    public void login(final String phone,final String password) {
        final String url = Helper.host+"/users.php";
        pgdialog.show();
//        tvLoggingIn.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pgdialog.dismiss();
                        Log.d("Logresp",response);
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getString("status").equals("ok")) {
                                JSONObject obj = object.getJSONObject("user");
                                if(obj.has("sess_category")) {
                                    if (obj.getString("sess_category").equals("Deployer")) {
                                        helper.setSession(obj.getString("sess_id"), obj.getString("sess_category"), obj.getString("sess_district"));
//                                tvLoggingIn.setVisibility(View.GONE);
                                        helper.showToast("Login success");
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else helper.showToast("Wrong username or password");
                                } else helper.showToast("Access denied");
                            } else  helper.showToast("Wrong username or password");
                        }catch (JSONException ex){
                            helper.showToast("Json error "+ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgdialog.dismiss();
                        helper.showToast("Something went wrong");
                        Log.e("jsonerr","JSON Error "+(error!=null?error.getMessage():""));
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cate", "login");
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", helper.getDataValue("appid"));//put your token here
                return headers;
            }
        };;

// add it to the RequestQueue
        queue.add(getRequest);
    }
}