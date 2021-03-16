package com.appsinventiv.realcaller.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.Models.SmsModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    Context context;
    List<SmsModel> itemList;
    SmsAdapterCallbacks callbacks;

    public SmsAdapter(Context context, List<SmsModel> itemList, SmsAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sms_item_layout, parent, false);
        SmsAdapter.ViewHolder viewHolder = new SmsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SmsModel model = itemList.get(position);

        if (SharedPrefs.getContactsMap() != null && SharedPrefs.getContactsMap().size() > 0) {
            if (SharedPrefs.getContactsMap().containsKey(model.getPhone())) {
                holder.name.setText(SharedPrefs.getContactsMap().get(model.getPhone()));

            } else {
                holder.name.setText(model.getPhone());
            }
        } else {
            holder.name.setText(model.getPhone());
        }

        holder.sms.setText(model.getBody());


//        holder.day.setText(DateFormat.format("EEE", Date.parse(model.getDate())));
        holder.day.setText(CommonUtils.getOnlyDay(Long.parseLong(model.getDate())));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(model,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, day, sms;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            sms = itemView.findViewById(R.id.sms);
            day = itemView.findViewById(R.id.day);

        }
    }

    public interface SmsAdapterCallbacks {
        public void onClick(SmsModel model,int position);
    }
}
