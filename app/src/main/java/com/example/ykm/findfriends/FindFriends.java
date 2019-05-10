package com.example.ykm.findfriends;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ykm.findfriends.Adapters.Adapter;
import com.example.ykm.find.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class FindFriends extends AppCompatActivity {
    RecyclerView mReyclerview;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mReyclerview = findViewById(R.id.recyclerView2);
        mReyclerview.setLayoutManager(new LinearLayoutManager(this));
        final Adapter adapter = new Adapter();
        mReyclerview.setAdapter(adapter);
        final List<String> dataset = new ArrayList<>();
        final List<String>  dataset2 = new ArrayList<>();

        final List<String>  key = new ArrayList<>();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.request);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                 if (tabId == R.id.friends) {
                    Intent intent = new Intent(FindFriends.this, FriendsListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                   // Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else if(tabId == R.id.locate){
                    Intent intent = new Intent(FindFriends.this,MapsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else if (tabId == R.id.profile) {
                    Intent intent = new Intent(FindFriends.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(tabId == R.id.request){}
            }
        });


        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataset.clear();
                dataset2.clear();
                key.clear();

                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                String headingName;
                String descriptionEmail = "NO DESCRIPTION";
                String mkey="";
                for (DataSnapshot child : children) {
                    headingName = child.child("name").getValue().toString();
                    mkey = child.getKey();
                    dataset.add(headingName);
                    dataset2.add(descriptionEmail);
                    key.add(mkey);
                }

                adapter.setNameset(dataset);
                adapter.setDescset(dataset2);
                adapter.setkey(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*
        String headingName = "Yash", descriptionEmail="mehta";
        for(int i=0;i<120;i++){
            dataset.add(headingName+" "+i);
            dataset2.add(descriptionEmail+" "+i);
        }

        adapter.setNameset(dataset);
        adapter.setDescset(dataset2);*/


    }

}
