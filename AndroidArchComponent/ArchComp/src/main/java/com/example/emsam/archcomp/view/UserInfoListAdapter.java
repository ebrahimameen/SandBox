package com.example.emsam.archcomp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emsam.archcomp.R;
import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

public class UserInfoListAdapter extends RecyclerView.Adapter<UserInfoListAdapter.UserViewHolder>
{

    private final LayoutInflater mInflater;
    private List<UserInfo> users;

    class UserViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tvName;

        private UserViewHolder(View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.textView);
        }

    }

    UserInfoListAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mInflater.inflate(R.layout.userlist_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position)
    {
        UserInfo current = users.get(position);
        holder.tvName.setText(String.format(" %s\t (%d)", current.getName(), current.getAge()));
    }
    public void setUsers(List<UserInfo> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount()
    {
        if (users != null)
        {
            return users.size();
        }
        else
        {
            return 0;
        }
    }
}
