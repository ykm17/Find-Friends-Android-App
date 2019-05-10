package com.example.ykm.findfriends.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ykm.findfriends.MapsActivity;
import com.example.ykm.find.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.ykm.findfriends.MapsActivity.FIND;

/**
 * Created by ykm on 01-Sep-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mDataset;
    private List<String> mDataset2;
    public MyAdapter() {
        mDataset = new ArrayList<>();
        mDataset2 = new ArrayList<>();
    }

    public void setDataset(List<String> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
        notifyDataSetChanged();
    }


    public void setDataset2(List<String> dataset2) {
        mDataset2.clear();
        mDataset2.addAll(dataset2);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (parent.getContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mDataset.get(position);
       String desc = mDataset2.get(position);
        holder.mName.setText(name);
        holder.mDesc.setText(desc);
    }

    @Override
    public int getItemCount() {
      return   mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName,mDesc;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.textViewHead);
            mDesc = itemView.findViewById(R.id.textViewDescription);
            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FIND =1;
                    Intent intent = new Intent(view.getContext(),MapsActivity.class);
                    intent.putExtra("NameFromAdapterClass",mName.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }

    }


}