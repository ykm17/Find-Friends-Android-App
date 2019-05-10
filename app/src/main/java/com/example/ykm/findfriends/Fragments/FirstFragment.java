package com.example.ykm.findfriends.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ykm.findfriends.Adapters.Adapter;
import com.example.ykm.find.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ykm on 16-Sep-17.
 */

public class FirstFragment extends Fragment {
    View mView;
    RecyclerView mReyclerview;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_find_friends, container, false);




        mReyclerview = mView.findViewById(R.id.recyclerView2);
        mReyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
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


       /* BottomBar bottomBar = (BottomBar) mView.findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.request);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {
                    Intent intent = new Intent(getContext(), FriendsListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    // Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else if(tabId == R.id.locate){
                    Intent intent = new Intent(getContext(),MapsActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else if (tabId == R.id.profile) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(tabId == R.id.request){}
            }
        });
*/
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
                    //Toast.makeText(FindFriends.this,mkey,Toast.LENGTH_SHORT).show();

                    /*

                    Log.d(TAG, "---------------------Name---------->: " + child.child("name").getValue());
                    Log.d(TAG, "---------------------Latitude---------->: " + child.child("latitude").getValue());
                    Log.d(TAG, "---------------------Longitude---------->: " + child.child("longitude").getValue());
*/

                    //   ListItem listItem = new ListItem(headingName, descriptionEmail);
                    if(!(mkey.equals(UserId))) {
                        dataset.add(headingName);
                        dataset2.add(descriptionEmail);
                        key.add(mkey);
                    }
                }

                adapter.setNameset(dataset);
                adapter.setDescset(dataset2);
                adapter.setkey(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return mView;
    }


}
