package com.yabi.yabiuserandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yabi.yabiuserandroid.models.data.Merchant;
import com.yabi.yabiuserandroid.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.OfferViewHolder>{

    Context context = null;
    List<Merchant> merchantList = new ArrayList<>();
    private OfferClickInterface offerClickInterface;

    public OutletAdapter(Context context, List<Merchant> merchantList, OfferClickInterface offerClickInterface)
    {
        this.context = context;
        this.merchantList = merchantList;
        System.out.print(merchantList);
        this.offerClickInterface = offerClickInterface;
    }
    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_outlet, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new OfferViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final OfferViewHolder holder, final int position) {

        Picasso.with(context).load(merchantList.get(position).getLogo()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerClickInterface.onOfferClick(holder.itemView,merchantList.get(position).getId(),false);
            }
        });
        holder.offerTitleTv.setText(merchantList.get(position).getName());
        holder.offerCountTv.setText("Offers: " + String.valueOf(merchantList.get(position).getOffer_count()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerClickInterface.onOfferClick(holder.itemView,merchantList.get(position).getId(),false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return merchantList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView offerCountTv;
        public TextView offerTitleTv;

        public OfferViewHolder(View itemView)
        {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.img_logo);
            offerTitleTv = (TextView) itemView.findViewById(R.id.txt_title);
            offerCountTv = (TextView) itemView.findViewById(R.id.txt_restaurant_offer_count);
        }

    }

    public interface OfferClickInterface
    {
        void onOfferClick(View itemView, int merchantId, boolean isDefaultSelection);
    }


}

