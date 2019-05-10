package com.example.ykm.findfriends.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ykm.find.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ykm on 19-Sep-17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    int sw; // switch friendrequest send  and cancel
    private List<String> mNameset;
    private List<String> mDescset;
    private List<String> mkey;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;


    public Adapter() {
        mNameset = new ArrayList<>();
        mDescset = new ArrayList<>();
        mkey = new ArrayList<>();
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

    public void setkey(List<String> key) {
        mkey.clear();
        mkey.addAll(key);
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (parent.getContext());
        View view = inflater.inflate(R.layout.findpeople,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       // final FindFriends findFriends = new FindFriends();
        String name = mNameset.get(position);
        String desc = mDescset.get(position);
        final String key = mkey.get(position);
        holder.mName.setText(name);
        holder.mDesc.setText(desc);
        holder.mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(sw==0){
                Toast.makeText(view.getContext(),"Friend Request Sent !", Toast.LENGTH_SHORT).show();
                sent(key);
                holder.mButton.setText("-");
                sw=1;
                }
                else {
                    Toast.makeText(view.getContext(),"Friend Request Cancelled !", Toast.LENGTH_SHORT).show();
                    cancel(key);
                    holder.mButton.setText("+");
                    sw=0;
                }
            }
        });

    }
    public void sent(String key){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        final String UserId = User.getUid();
        myRef = mFirebaseDatabase.getReference("users").child(UserId).child("requestsent");
        myRef.child(key).setValue("sent");
        myRef = mFirebaseDatabase.getReference("users").child(key).child("requestreceived");
        myRef.child(UserId).setValue("Unknown");

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
        Button mButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameText);
            mDesc = itemView.findViewById(R.id.emailText);
           mButton = itemView.findViewById(R.id.requestbutton);
            /* mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //FIND =1;
                    Toast.makeText(null,"Clicked",Toast.LENGTH_SHORT).show();*//*
                    Intent intent = new Intent(view.getContext(),MapsActivity.class);
                    intent.putExtra("NameFromAdapterClass",mName.getText().toString());
                    view.getContext().startActivity(intent);*//*
                }
            });*/
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
