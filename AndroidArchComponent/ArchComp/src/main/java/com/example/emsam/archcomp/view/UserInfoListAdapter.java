package com.example.emsam.archcomp.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emsam.archcomp.R;
import com.example.emsam.archcomp.model.UserInfo;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class UserInfoListAdapter extends PagedListAdapter<UserInfo, UserInfoListAdapter.UserViewHolder>
{

    private static final DiffUtil.ItemCallback<UserInfo> ITEM_CALLBACK = new DiffUtil.ItemCallback<UserInfo>()
    {
        @Override
        public boolean areItemsTheSame(final UserInfo oldItem, final UserInfo newItem)
        {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(final UserInfo oldItem, final UserInfo newItem)
        {
            return oldItem.equals(newItem);
        }
    };

    public UserInfoListAdapter()
    {
        super(ITEM_CALLBACK);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_cardview_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position)
    {
        UserInfo current = getItem(position);
        if (current != null)
        {
            holder.tvName.setText(String.format("%d- %s\t (%d)", current.id, current.getName(), current.getAge()));
            holder.tvEmail.setText(current.getEmail());
        }
        else
        {
            Log.i("UserInfo", "onBindViewHolder: user is null: " + position);
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tvName;
        private final TextView tvEmail;

        private UserViewHolder(View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
