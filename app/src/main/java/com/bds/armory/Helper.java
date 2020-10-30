package com.bds.armory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Helper {
    public String host = "";
    public Context ctx;
    public Helper(Context context){
        ctx = context;
        host = getHost();
    }
//    public static String host = "https://www.mbwira.rw/Methode/armory/api/requests";
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public void toggleNetworkConnectivityTextView(TextView tv){
        if(isNetworkConnected()) tv.setVisibility(View.GONE);
        else tv.setVisibility(View.VISIBLE);
        tv.refreshDrawableState();
    }
    public void setHost(String host){
        SharedPreferences.Editor sh = this.getEditor().edit();
        sh.putString("host",host);
        sh.apply();
    }
    public String getHost(){
        String local = "http://192.168.43.161/RUT/Methode/api/requests",
                remote = "http://192.168.1.8/RUT/Methode/api/requests";
        return getEditor().getString("host","http://192.168.1.14/RUT/Methode/armory/api/requests");
    }

    public void showToast(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();
    }
    public void setSession(String sessid,String category,String district){
        SharedPreferences.Editor sh = this.getEditor().edit();
        sh.putString("sess_id",sessid);
        sh.putString("sess_category",category);
        sh.putString("sess_district",district);
        sh.apply();
    }
     public void  logout(){
        SharedPreferences.Editor sh = getEditor().edit();
        sh.remove("sess_id");
        sh.remove("sess_category");
        sh.remove("sess_district");
        sh.commit();
     }
    public boolean hasSession(){
        return this.getEditor().contains("sess_id");
    }
    public String getDataValue(String parameter){
        SharedPreferences sh = ctx.getSharedPreferences("armory",Context.MODE_PRIVATE);
        return sh.getString(parameter,"");
    }
    public SharedPreferences getEditor(){
        SharedPreferences sh = ctx.getSharedPreferences("armory",Context.MODE_PRIVATE);
        return sh;
    }
}
