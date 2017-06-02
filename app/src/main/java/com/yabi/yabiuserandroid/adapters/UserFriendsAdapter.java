package com.yabi.yabiuserandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.models.data.UserFriends;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rohitsingh on 18/03/17.
 */

public class UserFriendsAdapter extends RecyclerView.Adapter<UserFriendsAdapter.ItemRowHolder> {

    private List<UserFriends> friendsList;
    private Context mContext;

    public UserFriendsAdapter(Context context, List<UserFriends> friendsList) {
        this.friendsList = friendsList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_list, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int position) {

       //load image here
       // itemRowHolder.itemDesc.setVisibility(View.GONE);
        Picasso.with(mContext).load("https://graph.facebook.com/" + friendsList.get(position).getId() + "/picture").into(itemRowHolder.itemImage);
        Log.d("LOGD","https://graph.facebook.com/" + friendsList.get(position).getId() + "/picture?type=large");

        itemRowHolder.itemTitle.setText(friendsList.get(position).getName());


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != friendsList ? friendsList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected TextView itemDesc;
        protected CircleImageView itemImage;


        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (CircleImageView) view.findViewById(R.id.itemImage);
//            this.itemDesc = (TextView) view.findViewById(R.id.txt_restaurant_offer_count);

        }

    }

}
