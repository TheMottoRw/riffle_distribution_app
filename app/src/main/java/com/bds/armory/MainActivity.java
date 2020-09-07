package com.bds.armory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new Helper(getApplicationContext());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        setUpDefaultFragment();

        if(!helper.hasSession()){
            finish();
            startActivity(new Intent(getApplicationContext(),Signin.class));
        }

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.dashboard:
                    selectedFragment = new Dashboard();break;
                case R.id.assignment:
                    selectedFragment = new Assignment();break;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            ft.replace(R.id.frame_container,selectedFragment).commit();
            return true;
        }
    };
    void setUpDefaultFragment(){
        Fragment selectedFragment = new Dashboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.frame_container,selectedFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.logout){
            finish();
            helper.logout();
            startActivity(new Intent(getApplicationContext(),Signin.class));
        }
        return super.onOptionsItemSelected(item);
    }
}