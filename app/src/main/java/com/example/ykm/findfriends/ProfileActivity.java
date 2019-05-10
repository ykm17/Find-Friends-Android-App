package com.example.ykm.findfriends;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ykm.find.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.profile);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {

                    Intent intent = new Intent(ProfileActivity.this,FriendsListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if (tabId == R.id.locate) {

                    Intent intent = new Intent(ProfileActivity.this,MapsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    //Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else if(tabId == R.id.request){
                    Intent intent = new Intent(ProfileActivity.this,RequestAcitivtyTabs.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else if (tabId == R.id.profile) {

                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {

                }
                else if (tabId == R.id.locate) {
                }
                else if (tabId == R.id.profile) {

                }
            }
        });

    }
}
