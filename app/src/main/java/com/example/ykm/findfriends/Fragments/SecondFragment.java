package com.example.ykm.findfriends.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ykm.find.R;
import com.example.ykm.findfriends.Adapters.ReceivedRequestAdpater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ykm on 16-Sep-17.
 */

public class SecondFragment extends Fragment {
    View mView;
    RecyclerView mReyclerview;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.second_layout, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myRef2 = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();

        mReyclerview = mView.findViewById(R.id.recyclerView3);
        mReyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final ReceivedRequestAdpater adapter = new ReceivedRequestAdpater();
        mReyclerview.setAdapter(adapter);
        final List<String> dataset = new ArrayList<>();
        final List<String>  dataset2 = new ArrayList<>();

        myRef.child("users").child(UserId).child("requestreceived").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataset.clear();
                dataset2.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                long c = dataSnapshot.getChildrenCount();

                for (DataSnapshot child : children) {
                    String key=child.getKey();
                    Log.d(TAG, "--------"+c+"-------------HEREEEEEEE--------->: ");
                    dataset.add(key);
                    dataset2.add("No Desc");

                }
                adapter.setNameset(dataset);
                adapter.setDescset(dataset2);

                //Toast.makeText(getContext(),dataSnapshot.getValue()+"",Toast.LENGTH_SHORT).show();  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

/*
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
*/
/*
                dataset.clear();
                dataset2.clear();
                key.clear();

*//*



                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                Log.d(TAG, "----********---------here-----------------: ");
                String headingName;
                String descriptionEmail = "NO DESCRIPTION";
                String mkey = "";
                for (DataSnapshot child : children) {
                    mkey = child.getKey();
                    if(mkey == UserId) {
                        Log.d(TAG, "----------------------------------------------------Requestreceived: " + child.child("requestreceived").child("key").getValue().toString());
                    }//headingName = child.child("requestreceived").getValue().toString();

                    //Toast.makeText(FindFriends.this,mkey,Toast.LENGTH_SHORT).show();

                    */
/*

                    Log.d(TAG, "---------------------Name---------->: " + child.child("name").getValue());
                    Log.d(TAG, "---------------------Latitude---------->: " + child.child("latitude").getValue());
                    Log.d(TAG, "---------------------Longitude---------->: " + child.child("longitude").getValue());
*//*


                    //   ListItem listItem = new ListItem(headingName, descriptionEmail);
*/
/*
                    dataset.add(headingName);
                    dataset2.add(descriptionEmail);
                    key.add(mkey);
*//*

                }

*/
/*
                adapter.setNameset(dataset);
                adapter.setDescset(dataset2);
                adapter.setkey(key);
*//*

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

        return mView;
    }
}
