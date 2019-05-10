package com.example.ykm.findfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.ykm.findfriends.Adapters.MyAdapter;
import com.example.ykm.find.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    long back_pressed = 0;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
/*
    @Override
    public void onBackPressed() {
        mAdapter = new MyAdapter(null,FriendsListActivity.this);
        mRecyclerView.setAdapter(null);
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.friends);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {
                } else if (tabId == R.id.locate) {
                    Intent intent = new Intent(FriendsListActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } else if (tabId == R.id.request) {
                    Intent intent = new Intent(FriendsListActivity.this, RequestAcitivtyTabs.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (tabId == R.id.profile) {
                    Intent intent = new Intent(FriendsListActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {

                } else if (tabId == R.id.locate) {
/*
                    Intent intent = new Intent(FriendsListActivity.this,MapsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                    Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
*/

                } else if (tabId == R.id.profile) {

                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final MyAdapter adapter = new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        final List<String> dataset = new ArrayList<>();
        final List<String> dataset2 = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();



        /*for(int i=0;i<10;i++){
            ListItem listItem = new ListItem(
                    "heading"+(i+1),
                    "Lorem ipsum dummy text"
            );
            mListItems.add(listItem);
        }

        mAdapter = new MyAdapter(mListItems,this);

        mRecyclerView.setAdapter(mAdapter);*/

        //-----//
        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();

        dataset.clear();
        dataset2.clear();
        myRef.child("users").child(UserId).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                String headingName;
                String descriptionEmail = "NO DESCRIPTION";
                for (DataSnapshot child : children) {
                    headingName = child.getValue().toString();

                    dataset.add(headingName);
                    dataset2.add(descriptionEmail);
                }
                adapter.setDataset(dataset);
                adapter.setDataset2(dataset2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //----------------//
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 1000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
