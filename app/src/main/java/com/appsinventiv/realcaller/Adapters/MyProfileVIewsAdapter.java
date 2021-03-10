package com.appsinventiv.realcaller.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Models.SmsModel;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.NetworkResponses.ListModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;

import java.util.Date;
import java.util.List;

public class MyProfileVIewsAdapter extends RecyclerView.Adapter<MyProfileVIewsAdapter.ViewHolder> {
    Context context;
    List<ListModel> itemList;

    public MyProfileVIewsAdapter(Context context, List<ListModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item_layout, parent, false);
        MyProfileVIewsAdapter.ViewHolder viewHolder = new MyProfileVIewsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListModel model = itemList.get(position);

        if (model.getName().equals("")) {
            holder.name.setText(model.getPhone());
            holder.nameInitial.setText("\uD83E\uDDD4");

        } else {
            holder.name.setText(model.getName());
            holder.nameInitial.setText(model.getName().substring(0, 1));

        }

        holder.phone.setText(model.getPhone());
        if (model.getDateTime() == null) {
            holder.day.setText("");
        } else {
            holder.day.setText(model.getDateTime().substring(0, 10));

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, nameInitial, day, phone;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameInitial = itemView.findViewById(R.id.nameInitial);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            day = itemView.findViewById(R.id.day);

        }
    }

}
