package com.yabi.yabiuserandroid.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.WidgetPermissionPopupActivity;
import com.yabi.yabiuserandroid.adapters.OutletAdapter;
import com.yabi.yabiuserandroid.adapters.UserFriendsAdapter;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.Merchant;
import com.yabi.yabiuserandroid.models.data.UserFriends;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.AutofitRecyclerView;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.MarginDecoration;
import com.yabi.yabiuserandroid.utils.AppConstants;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class OutletListingFragment extends Fragment implements OutletAdapter.OfferClickInterface,LoadingInterface{
    ViewGroup rootView;
    private AutofitRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Merchant> merchantsList = new ArrayList<>();
    private OutletAdapter outletAdapter;
    private UserFriendsAdapter userFriendsAdapter;
    private ImageView errorImage;
    LinearLayout userFriendsContainer;
    private RecyclerView userFriendsRv;
    private TextView errorMsgTitleTv;
    SharedPrefUtils sharedPrefUtils;
    private TextView errorMsgDescTv;
    private Button retryBtn;
    private LinearLayout errorLayout;
    CustomUncancelableLoader customUncancelableLoader;
    private EventBus eventBus ;

    public OutletListingFragment() {
        // Required empty public constructor
    }


    public static OutletListingFragment newInstance() {
        OutletListingFragment fragment = new OutletListingFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_outlet_listing, container, false);
        userFriendsRv = (RecyclerView) rootView.findViewById(R.id.friendsRv);
        sharedPrefUtils = new SharedPrefUtils(getActivity());
        userFriendsContainer = (LinearLayout) rootView.findViewById(R.id.userFriendsContainer);
        userFriendsRv.setHasFixedSize(true);
        userFriendsRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView = (AutofitRecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(false);
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(),"Please wait ....");
        customUncancelableLoader.setCancelable(false);
        showLoading(customUncancelableLoader);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);
        errorImage = (ImageView) rootView.findViewById(R.id.errorImg);
        errorMsgTitleTv = (TextView) rootView.findViewById(R.id.errorMsgTitle);
        errorMsgDescTv = (TextView) rootView.findViewById(R.id.errorMsgDesc);
        retryBtn = (Button) rootView.findViewById(R.id.tryAgainBtn);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMerchantListing();
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          customUncancelableLoader.show();
          getMerchantListing();
            }
        });
        getMerchantListing();

        return rootView;
    }
public void updateUi()
{
    swipeRefreshLayout.setRefreshing(false);
    String userFriendsListString =  sharedPrefUtils.readString(AppConstants.USER_FRIENDS, null);
    List<UserFriends> userFriendsList = new Gson().fromJson(userFriendsListString,  new TypeToken<List<UserFriends>>(){}.getType());
    userFriendsAdapter = new UserFriendsAdapter(getActivity(),userFriendsList);
    outletAdapter = new OutletAdapter(getActivity(), merchantsList, this);
    hideLoading(customUncancelableLoader);
    if(userFriendsList != null && !userFriendsList.isEmpty()){
        userFriendsContainer.setVisibility(View.VISIBLE);
        userFriendsRv.setAdapter(userFriendsAdapter);
    }else{
       userFriendsContainer.setVisibility(View.GONE);
    }
    recyclerView.setAdapter(outletAdapter);

    if(!sharedPrefUtils.readBoolean(AppConstants.IS_ACCESSIBILITY_PERMISSION_GIVEN,false) && !sharedPrefUtils.readBoolean(AppConstants.IS_CAN_DRAW_OVERLAY_PERMISSION_GIVEN,false))
    startActivity(new Intent(getContext(), WidgetPermissionPopupActivity.class));


}

    private void getMerchantListing(){

      if (new Utils().isConnectedToInternet(getContext())){
          errorLayout.setVisibility(View.GONE);
          NetworkManager.getInstance(getActivity()).getMerchants();
      }
      else{
          errorLayout.setVisibility(View.VISIBLE);
          errorMsgTitleTv.setText(getResources().getString(R.string.no_internet_title));
          errorMsgDescTv.setText(getResources().getString(R.string.no_internet_desc));
          hideLoading(customUncancelableLoader);
      }
    }

    @Override
    public void onOfferClick(View itemView, int merchantId, boolean isDefaultSelection) {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_body, OfferListingFragment.newInstance(merchantId)).addToBackStack(null).commit();

        eventBus.unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //hideLoading(customUncancelableLoader);
        eventBus.unregister(this);
    }

    public void onEvent(JsonObject jsonObject) {
        if(jsonObject == null){
            showIfErrorOccurred();
            hideLoading(customUncancelableLoader);
            return;
        } else{
            JsonArray jsonArray =  jsonObject.getAsJsonArray("data");
            System.out.print(jsonArray);
            merchantsList = new Gson().fromJson(jsonArray,new TypeToken<List<Merchant>>(){}.getType());
            System.out.print(merchantsList);
            errorLayout.setVisibility(View.GONE);
            updateUi();
        }

    }

    public void onEvent(ErrorTypes errorTypes)
    {
        hideLoading(customUncancelableLoader);
        if(errorTypes.getType() == ErrorTypes.FETCH_MERCHANT_ERROR);
        showIfErrorOccurred();
    }

   private void showIfErrorOccurred(){

       Log.d("Error","Called");
       errorMsgTitleTv.setText(getResources().getString(R.string.error_title));
       errorMsgTitleTv.setVisibility(View.GONE);
       errorMsgDescTv.setText(getResources().getString(R.string.error_desc));
       hideLoading(customUncancelableLoader);
       errorLayout.setVisibility(View.VISIBLE);
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
            customUncancelableLoader.dismiss();
        }
    }

}
