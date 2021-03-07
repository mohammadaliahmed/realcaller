package com.appsinventiv.realcaller.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.ViewHolder> {
    Context context;
    List<CallLogsModel> itemList;
    CallLogsAdapterCallbacks callbacks;

    public CallLogsAdapter(Context context, List<CallLogsModel> itemList, CallLogsAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_log_item_layout, parent, false);
        CallLogsAdapter.ViewHolder viewHolder = new CallLogsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLogsModel model = itemList.get(position);
        try {
            if (model.getName() == null) {
                holder.name.setText(model.getPhone());
                holder.nameInitial.setText("\uD83E\uDDD4");

            } else {
                holder.name.setText(model.getName());
                holder.nameInitial.setText(model.getName().substring(0, 1));

            }

            holder.duration.setText(CommonUtils.getDurationFormatted(Long.parseLong(model.getDuration())));


        } catch (Exception e) {

        }

        if (model.getType().equalsIgnoreCase("incoming")) {
            Glide.with(context).load(R.drawable.incoming_call).into(holder.callType);
        } else if (model.getType().equalsIgnoreCase("outgoing")) {
            Glide.with(context).load(R.drawable.outgoing_call).into(holder.callType);
        } else if (model.getType().equalsIgnoreCase("missed")) {
            Glide.with(context).load(R.drawable.missed_call).into(holder.callType);
        } else if (model.getType().equalsIgnoreCase("rejected")) {
            Glide.with(context).load(R.drawable.rejected_call).into(holder.callType);
        } else {
            Glide.with(context).load(R.drawable.ic_black_phone).into(holder.callType);
        }

        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                context.startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(model.getPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, nameInitial, duration;

        ImageView dial, callType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            nameInitial = itemView.findViewById(R.id.nameInitial);
            dial = itemView.findViewById(R.id.dial);
            duration = itemView.findViewById(R.id.duration);
            callType = itemView.findViewById(R.id.callType);
        }
    }

    public interface CallLogsAdapterCallbacks {
        public void onClick(String number);
    }
}
