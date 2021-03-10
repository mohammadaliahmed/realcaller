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

import com.appsinventiv.realcaller.Models.ContactModel;
import com.appsinventiv.realcaller.NetworkResponses.ListModel;
import com.appsinventiv.realcaller.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.ViewHolder> {
    Context context;
    List<ListModel> itemList;

    public BlockedAdapter(Context context, List<ListModel> itemList) {
        this.context = context;
        this.itemList = itemList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blocked_item_layout, parent, false);
        BlockedAdapter.ViewHolder viewHolder = new BlockedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListModel model = itemList.get(position);
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());


        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;

        ImageView dial;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            dial = itemView.findViewById(R.id.dial);

        }
    }
}
