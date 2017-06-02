package com.yabi.yabiuserandroid.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.adapters.OfferAdapter;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitsingh on 23/12/16.
 */

public class FavouritesActivityFragment extends Fragment {
    private ListView listView;
    OfferAdapter offerAdapter;
    List<Offers> offers;
    private ImageView errorImage;
    private TextView errorMsgTitleTv;
    private TextView errorMsgDescTv;
    private Button retryBtn;
    private LinearLayout errorLayout;

    public FavouritesActivityFragment() {
    }

    public static FavouritesActivityFragment newInstance() {
        FavouritesActivityFragment fragment = new FavouritesActivityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favourites, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        errorImage = (ImageView) rootView.findViewById(R.id.errorImg);
        errorMsgTitleTv = (TextView) rootView.findViewById(R.id.errorMsgTitle);
        errorMsgDescTv = (TextView) rootView.findViewById(R.id.errorMsgDesc);
        retryBtn = (Button) rootView.findViewById(R.id.tryAgainBtn);
        initViews();
        return rootView;
    }
    public void initViews()
    {

        offers = new ArrayList<>();
        offers.addAll(Utils.getFavouriteOffers(getActivity()));
        if(offers.size()==0){
            retryBtn.setVisibility(View.GONE);
            errorMsgDescTv.setText(getResources().getString(R.string.text_no_favourites));
            errorLayout.setVisibility(View.VISIBLE);

        }else
        errorLayout.setVisibility(View.GONE);

        offerAdapter = new OfferAdapter(getActivity(),offers);
        listView.setAdapter(offerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOfferDetails(offers.get(position));
            }
        });
    }
    public void showOfferDetails(final Offers offer)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(offer.getTitle());

        SpannableString spannableString = new SpannableString("Use Coupon \n" + offer.getCode() +"\n to avail the offer.\n\n*"+offer.getT_n_c());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 11, 11+offer.getCode().length()+1, 0);
        spannableString.setSpan(new RelativeSizeSpan(2.5f), 11, 11+offer.getCode().length()+1, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 11, 11+offer.getCode().length()+1, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), spannableString.length() - offer.getT_n_c().length(), spannableString.length(), 0);

        builder.setMessage(spannableString);
        builder.setPositiveButton(getActivity().getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNeutralButton(getActivity().getString(R.string.string_remove_coupon),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.toggleFavourite(getActivity(),offer);
                        refreshData();
                    }
                });
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public void refreshData()
    {
        offers.clear();
        offers.addAll(Utils.getFavouriteOffers(getActivity()));
        offerAdapter.notifyDataSetChanged();
    }
}
