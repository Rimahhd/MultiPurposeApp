package com.example.multipurposeapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Activity activity;
    ArrayList<ContactsModel> arrayList;

    public ContactsAdapter(Activity activity, ArrayList<ContactsModel> arrayList){

        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ContactsModel model = arrayList.get(position);

        holder.nameTV.setText(model.getName());
        holder.numberTV.setText(model.getNumber());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, numberTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.idTVContactName);
            numberTV = itemView.findViewById(R.id.idTVContactNumber);
        }
    }
}
