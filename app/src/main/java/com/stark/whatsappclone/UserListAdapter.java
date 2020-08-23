package com.stark.whatsappclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListRecyclerViewHolder> {
    ArrayList<UserObject> userList;
    public UserListAdapter(ArrayList<UserObject> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListRecyclerViewHolder rcv = new UserListRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListRecyclerViewHolder holder, int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class UserListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mPhone;

        public UserListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPhone = itemView.findViewById(R.id.phone);

        }
    }
}
