package com.example.ykm.findfriends.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.ContentValues.TAG;

/**
 * Created by ykm on 20-Sep-17.
 */

/**
 * Created by ykm on 19-Sep-17.
 */

public class ReceivedRequestAdpater extends RecyclerView.Adapter<ReceivedRequestAdpater.ViewHolder> {

    int sw; // switch friendrequest send  and cancel
    private List<String> mNameset;
    private List<String> mDescset;
  //  private List<String> mkey;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;


    public ReceivedRequestAdpater() {
        mNameset = new ArrayList<>();
        mDescset = new ArrayList<>();
     //   mkey = new ArrayList<>();
        setHasStableIds(true);
    }

    public void setNameset(List<String> nameset) {
        mNameset.clear();
        mNameset.addAll(nameset);
        notifyDataSetChanged();
    }


    public void setDescset(List<String> descset) {
        mDescset.clear();
        mDescset.addAll(descset);
        notifyDataSetChanged();
    }
/*
    public void setkey(List<String> key) {
        mkey.clear();
        mkey.addAll(key);
        notifyDataSetChanged();

    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (parent.getContext());
        View view = inflater.inflate(R.layout.receivepeople,parent,false);
        return new ViewHolder(view);
    }
    String nameTemp="";
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // final FindFriends findFriends = new FindFriends();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
         myRef = mFirebaseDatabase.getReference();




        final String name = mNameset.get(position);
        String desc = mDescset.get(position);
        Log.d(TAG, "=-----------------------------------------onBindViewHolder: "+name);
        myRef.child("users").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameTemp=  dataSnapshot.child("name").getValue().toString();
                holder.mName.setText(nameTemp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        holder.mDesc.setText(desc);
        holder.mAcceptBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                acceptResponse(name);
              Toast.makeText(view.getContext(),"Accept",Toast.LENGTH_SHORT).show();
            }
        });

        holder.mDeclinebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Decline",Toast.LENGTH_SHORT).show();
                declineResponse(name);
            }
        });

    }


    public void declineResponse(String key){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();

        myRef = mFirebaseDatabase.getReference("users").child(UserId).child("requestreceived");
        myRef.child(key).removeValue();


        myRef = mFirebaseDatabase.getReference("users").child(key).child("requestsent");
        myRef.child(UserId).setValue("declined");


    }
    String Sname="",Sname2="";
    public void acceptResponse(final String key){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        DatabaseReference myRef2 = mFirebaseDatabase.getReference();


        final String UserId = User.getUid();
        myRef = mFirebaseDatabase.getReference("users").child(UserId).child("requestreceived");
        myRef.child(key).removeValue();
        //Log.d(TAG, "--------------------acceptResponse: "+string);
        myRef2.child("users").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sname=  dataSnapshot.child("name").getValue().toString();
                myRef = mFirebaseDatabase.getReference("users").child(UserId).child("friends");
                myRef.child(key).setValue(Sname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef = mFirebaseDatabase.getReference("users").child(key).child("requestsent");
        myRef.child(UserId).setValue("accepted");


        myRef2.child("users").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sname2=  dataSnapshot.child("name").getValue().toString();
                myRef = mFirebaseDatabase.getReference("users").child(key).child("friends");
                myRef.child(UserId).setValue(Sname2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
    public void cancel(String key){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();
        myRef = mFirebaseDatabase.getReference("users").child(UserId).child("requestsent");
        myRef.child(key).removeValue();
        myRef = mFirebaseDatabase.getReference("users").child(key).child("requestreceived");
        myRef.child(UserId).removeValue();

    }



    @Override
    public int getItemCount() {
        return   mNameset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName,mDesc;
        Button mAcceptBtn, mDeclinebtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameReceivedText);
            mDesc = itemView.findViewById(R.id.emailReceivedText);
            mAcceptBtn = itemView.findViewById(R.id.acceptbutton);
            mDeclinebtn = itemView.findViewById(R.id.declinebutton);
        }

    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
