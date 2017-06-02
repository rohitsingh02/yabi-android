package com.yabi.yabiuserandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.R;
import java.util.ArrayList;
import java.util.List;


public class OfferAdapter extends ArrayAdapter {

    private Context context;
    private List<Offers> offersList = new ArrayList<>();
    int layoutResourceId;

    public OfferAdapter(Context context, List<Offers> offersList) {
        super(context, R.layout.list_item_offer);
        this.context = context;
        this.layoutResourceId = R.layout.list_item_offer;
        this.offersList = offersList;
    }

    @Override
    public int getCount() {
        return offersList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_offer, null, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.offerTitle = (TextView) convertView.findViewById(R.id.txt_offer_title);
                childViewHolder.offerDescription = (TextView) convertView.findViewById(R.id.txt_offer_description);
                childViewHolder.offerExpiry = (TextView) convertView.findViewById(R.id.txt_offer_expiry);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

        childViewHolder.offerTitle.setText(offersList.get(position).getTitle());
        childViewHolder.offerDescription.setText(offersList.get(position).getDescription());
        String offerExpiryString = offersList.get(position).getT_n_c();
        childViewHolder.offerExpiry.setText(offerExpiryString);
        return convertView;
    }

    static class ChildViewHolder {
        TextView offerTitle, offerDescription,offerExpiry;
    }
}
