package com.bds.armory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        helper = new Helper(this);
        if(!helper.hasSession()){
            finish();
            startActivity(new Intent(getApplicationContext(),Signin.class));
        } else {
            if(helper.getDataValue("sess_category").equals("Riffle_distributor")){
                startActivity(new Intent(this,MainActivity.class));
            }else if(helper.getDataValue("sess_category").equals("Police")){
                startActivity(new Intent(this,PoliceAllocation.class));
            }
        }
    }
}