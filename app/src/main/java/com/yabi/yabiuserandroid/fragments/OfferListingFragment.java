package com.yabi.yabiuserandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.adapters.OfferAdapter;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.MerchantDetail;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomDialog;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.palette.CustomFontTextView;
import com.yabi.yabiuserandroid.utils.BranchDeepLinkHelper;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import io.branch.referral.Branch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferListingFragment extends Fragment implements LoadingInterface {
    public static final String KEY_OFFER = "offer";
    private MerchantDetail merchantDetail;
    private ImageView headerImageView;
    private de.hdodenhof.circleimageview.CircleImageView profileImg;
    private CustomFontTextView customFontTextView;
    private int merchantId;
    private TextView atRateTv;
    private CircleImageView androidImag;
    private CircleImageView iosImag;
    private ListView listView;
    private RelativeLayout offerContainer;
    private ImageView errorImage;
    private TextView errorMsgTitleTv;
    private TextView errorMsgDescTv;
    private Button retryBtn;
    private LinearLayout errorLayout;
    private View header;
    View noOfferView;
    Branch branch;
    Boolean isOfferLink = false;
    int position;
    CustomUncancelableLoader customUncancelableLoader;
    static OfferListingFragment fragment;
    SharedPrefUtils sharedPrefUtils;
    private OfferAdapter offerAdapter;
    private List<Offers> offersList;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private EventBus eventBus = null;


    public static OfferListingFragment newInstance(int merchantId) {
        fragment = new OfferListingFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_OFFER, merchantId);
        fragment.setArguments(args);
        return fragment;
    }

    public static OfferListingFragment oldInstance() {
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPrefUtils = new SharedPrefUtils(getContext());
        if (getArguments() != null) {
            merchantId = (int) getArguments().getSerializable(KEY_OFFER);
        }
        branch = Branch.getInstance(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_listing, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        header = inflater.inflate(R.layout.offer_list_header, null, false);
        noOfferView = inflater.inflate(R.layout.no_offer_view, null, false);
        headerImageView = (ImageView) header.findViewById(R.id.img_backdrop);
        profileImg = (CircleImageView) header.findViewById(R.id.img_profile);
        customFontTextView = (CustomFontTextView) header.findViewById(R.id.text_restaurant_name);
        androidImag = (CircleImageView) header.findViewById(R.id.android_icn);
        atRateTv = (TextView) header.findViewById(R.id.atRateTv);
        iosImag = (CircleImageView) header.findViewById(R.id.ios_icon);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        errorImage = (ImageView) rootView.findViewById(R.id.errorImg);
        errorMsgTitleTv = (TextView) rootView.findViewById(R.id.errorMsgTitle);
        errorMsgDescTv = (TextView) rootView.findViewById(R.id.errorMsgDesc);
        retryBtn = (Button) rootView.findViewById(R.id.tryAgainBtn);
        offerContainer = (RelativeLayout) rootView.findViewById(R.id.offerLayoutContainer);
        avLoadingIndicatorView = (AVLoadingIndicatorView) header.findViewById(R.id.custom_loader);
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(),"Please wait ....");
        customUncancelableLoader.setCancelable(false);
        showLoading(customUncancelableLoader);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
        sharedPrefUtils = new SharedPrefUtils(getActivity());
        offersList = new ArrayList<>();
        getOfferListing(merchantId);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCustomDialogBox(position - 1);
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(customUncancelableLoader);
                getOfferListing(merchantId);
            }
        });
        return rootView;
    }

    private void getOfferListing(int merchantId) {

        if (new Utils().isConnectedToInternet(getContext())){
            NetworkManager.getInstance(getActivity()).getMerchantDetails("merchant/" + merchantId);
        }else{
            offerContainer.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorMsgTitleTv.setText(getResources().getString(R.string.no_internet_title));
            errorMsgDescTv.setText(getResources().getString(R.string.no_internet_desc));
            hideLoading(customUncancelableLoader);
        }

    }


    public void initView() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(merchantDetail.getName());
        hideLoading(customUncancelableLoader);
        offerAdapter = new OfferAdapter(getActivity(), offersList);
        listView.setAdapter(offerAdapter);
        listView.addHeaderView(header, null, false);
        if (offersList.size() == 0) {
            listView.addFooterView(noOfferView, null, false);
        }
        customFontTextView.setText(merchantDetail.getName());

        if (!merchantDetail.getAndroid_app().equals("")) {
            androidImag.setVisibility(View.VISIBLE);
            atRateTv.setVisibility(View.VISIBLE);
        } else {
            androidImag.setVisibility(View.GONE);
        }

        if (!merchantDetail.getIos_app().equals("")) {
            iosImag.setVisibility(View.VISIBLE);
            atRateTv.setVisibility(View.VISIBLE);
        } else {
            iosImag.setVisibility(View.GONE);
        }
        Picasso.with(getContext()).load(merchantDetail.getCover()).placeholder(R.drawable.offer_placeholder).into(headerImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onError() {
                avLoadingIndicatorView.hide();
            }
        });

        Picasso.with(getContext()).load(merchantDetail.getLogo()).placeholder(R.drawable.offer_placeholder).into(profileImg, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
            if(isOfferLink){
                openCustomDialogBox(position);
            }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        eventBus = EventBus.getDefault();
//        eventBus.register(this);
//    }

    @Override
    public void onStop() {
        super.onStop();
        sharedPrefUtils.removeParticularPreference("IsCameFromDeepLinking");
        sharedPrefUtils.removeParticularPreference("merchantId");
        sharedPrefUtils.removeParticularPreference("ISOFFERLINK");
        sharedPrefUtils.removeParticularPreference("OFFERPOSITION");
        hideLoading(customUncancelableLoader);
        eventBus.unregister(this);
    }

    public void onEvent(JsonObject jsonObject) {
        if (jsonObject == null) {
            Toast.makeText(getActivity(), "Unable to fetch details", Toast.LENGTH_LONG).show();
            //show proper handle here with retry button
            showIfErrorOccurred();
            hideLoading(customUncancelableLoader);

            return;
        } else {
            JsonObject myObj = jsonObject.getAsJsonObject("data");
            merchantDetail = new Gson().fromJson(myObj, new TypeToken<MerchantDetail>() {
            }.getType());
            offersList = merchantDetail.getOffersList();
            initView();
            offerContainer.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }

    }

    public void onEvent(ErrorTypes errorTypes) {
        if (errorTypes.getType() == ErrorTypes.FETCH_MERCHANT_ERROR) {
            Toast.makeText(getActivity(), "Unable to fetch merchant detail.", Toast.LENGTH_SHORT).show();
            showIfErrorOccurred();
            hideLoading(customUncancelableLoader);
            //show proper handle here with retry button
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_merchant, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:
                if (merchantDetail != null){
                    BranchDeepLinkHelper.getInstance().createMerchantLinkToShare(getActivity(), merchantId, merchantDetail);
                }
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setOfferDeepLinkDetail(Boolean isOfferLink,int position){

        this.isOfferLink = isOfferLink;
        this.position = position;

    }

    public void openCustomDialogBox(int position){
        CustomDialog customDialog = new CustomDialog(getActivity(), merchantDetail,position,offersList.get(position));
        customDialog.show();
    }



//    public void showOfferDetails(final Offers offer)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(offer.getTitle());
//        SpannableString spannableString = new SpannableString("Use Coupon \n" + offer.getCode() +"\n to avail the offer.\n\n*"+offer.getT_n_c());
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 11, 11+offer.getCode().length()+1, 0);
//        spannableString.setSpan(new RelativeSizeSpan(2.5f), 11, 11+offer.getCode().length()+1, 0);
//        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 11, 11+offer.getCode().length()+1, 0);
//        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), spannableString.length() - offer.getT_n_c().length(), spannableString.length(), 0);
//
//        builder.setMessage(spannableString);
//        builder.setPositiveButton(getActivity().getString(android.R.string.ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        String buttonText = Utils.isFavourite(getActivity(),offer)?getResources().getString(R.string.string_remove_coupon):getResources().getString(R.string.string_save_coupon);
//        builder.setNeutralButton(buttonText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        if(offer.getMerchantId()==null)
////                            offer.setMerchant_id(merchant.getId());
////                            offer.setMerchantId(String.valueOf(merchant.getId()));
//                        Utils.toggleFavourite(getActivity(),offer);
//                    }
//                });
//        try{
//            AlertDialog dialog = builder.show();
//            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
//            messageView.setGravity(Gravity.CENTER);
//        }catch (Exception e)
//        {
//            Log.e("alert exception",""+e);
//        }
//
//    }

    private void showIfErrorOccurred(){
        offerContainer.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMsgTitleTv.setText(getResources().getString(R.string.error_title));
        errorMsgTitleTv.setVisibility(View.GONE);
        errorMsgDescTv.setText(getResources().getString(R.string.error_desc));
        hideLoading(customUncancelableLoader);
    }

    @Override
    public void showLoading(CustomUncancelableLoader customUncancelableLoader) {
        if (customUncancelableLoader != null && !customUncancelableLoader.isShowing()) {
            customUncancelableLoader.show();
        }
    }

    @Override
    public void hideLoading(CustomUncancelableLoader customUncancelableLoader) {
        if (customUncancelableLoader != null && customUncancelableLoader.isShowing()) {
            customUncancelableLoader.hide();
        }
    }

}
