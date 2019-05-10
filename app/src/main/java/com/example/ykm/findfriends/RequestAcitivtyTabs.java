package com.example.ykm.findfriends;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.ykm.findfriends.Adapters.SectionsPageAdapter;
import com.example.ykm.findfriends.Fragments.FirstFragment;
import com.example.ykm.findfriends.Fragments.SecondFragment;
import com.example.ykm.find.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class RequestAcitivtyTabs extends AppCompatActivity {

    public static final String TAG = "MAinactivity--------!!!";

    private  ViewPager mViewPager ;
    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acitivty_tabs);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.request);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {
                    Intent intent = new Intent(RequestAcitivtyTabs.this, FriendsListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    // Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else if(tabId == R.id.locate){
                    Intent intent = new Intent(RequestAcitivtyTabs.this,MapsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else if (tabId == R.id.profile) {
                    Intent intent = new Intent(RequestAcitivtyTabs.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(tabId == R.id.request){}
            }
        });



    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragemnt(new FirstFragment(),"Send");
        adapter.addFragemnt(new SecondFragment(),"Receive");
        // adapter.addFragemnt(new ThirdFragment(),"TAB3");
        viewPager.setAdapter(adapter);
    }
}
