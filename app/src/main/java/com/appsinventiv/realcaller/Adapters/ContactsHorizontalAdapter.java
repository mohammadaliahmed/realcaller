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
import com.appsinventiv.realcaller.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsHorizontalAdapter extends RecyclerView.Adapter<ContactsHorizontalAdapter.ViewHolder> {
    Context context;
    List<ContactModel> itemList;
    List<ContactModel> arrayList;
    ContactsHoriCallbacks callbacks;

    public ContactsHorizontalAdapter(Context context, List<ContactModel> itemList, ContactsHoriCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
        this.arrayList = new ArrayList<>(itemList);

    }

    public void updateList(List<ContactModel> itemList) {
        this.itemList = itemList;
        arrayList.clear();
        arrayList.addAll(itemList);
        notifyDataSetChanged();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arrayList);
        } else {
            for (ContactModel item : arrayList) {
                if (item.getName().toLowerCase().contains(charText.toLowerCase()) || item.getPhone().contains(charText)) {

                    itemList.add(item);
                }

            }


        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_horizontal_item_layout, parent, false);
        ContactsHorizontalAdapter.ViewHolder viewHolder = new ContactsHorizontalAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel model = itemList.get(position);
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.nameInitial.setText(model.getName().substring(0, 1));
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
        TextView name, nameInitial, phone;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            nameInitial = itemView.findViewById(R.id.nameInitial);

        }
    }

    public interface ContactsHoriCallbacks {
        public void onClick(String number);
    }
}
