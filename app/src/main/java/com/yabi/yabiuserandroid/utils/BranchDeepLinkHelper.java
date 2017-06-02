package com.yabi.yabiuserandroid.utils;

import android.app.Activity;
import android.util.Log;

import com.yabi.yabiuserandroid.models.data.MerchantDetail;
import com.yabi.yabiuserandroid.models.data.Offers;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by rohitsingh on 14/12/16.
 */

public class BranchDeepLinkHelper {

    private static BranchDeepLinkHelper branchDeepLinkHelper;

    public static BranchDeepLinkHelper getInstance() {
        if (branchDeepLinkHelper == null) {
            branchDeepLinkHelper = new BranchDeepLinkHelper();
        }
        return branchDeepLinkHelper;
    }

    public void createMerchantLinkToShare(Activity activity, int merchantId, MerchantDetail merchantDetail) {

        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(activity);
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(sharedPrefUtils.getUserPhone())
                // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                .setTitle(merchantDetail.getName())//imp
                .setContentDescription(merchantDetail.getName())//imp
                .setContentImageUrl(merchantDetail.getCover())//imp

                // You use this to specify whether this content can be discovered publicly - default is public
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);//optional
        // Here is where you can add custom keys/values to the deep link data

//                .addContentMetadata("$og_image_url", imgUrl)
//                .addContentMetadata("$og_title", title)
//                .addContentMetadata("$og_description", description);


        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing")
                .addControlParameter("MERCHANTID", String.valueOf(merchantId))
                .addControlParameter("ISMERCHANTLINK", "true")
                .addControlParameter("$desktop_url", merchantDetail.getWebsite())
                .addControlParameter("$ios_url", merchantDetail.getIos_app());

        branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                } else {
                    Log.e("error", error.toString());
                }
            }
        });


        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(activity, "Check this out!", merchantDetail.getName())
                .setMoreOptionStyle(activity.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER);

        branchUniversalObject.showShareSheet(activity,
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {

                    }

                    @Override
                    public void onShareLinkDialogDismissed() {

                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                        Log.e("LinkShared", "success");
                    }

                    @Override
                    public void onChannelSelected(String channelName) {

                    }
                });

    }

    public void createOfferLinkToShare(Activity activity,MerchantDetail merchantDetail,int location, Offers offers) {

        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(activity);
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(sharedPrefUtils.getUserPhone())
                // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                .setTitle(merchantDetail.getName())//imp
                .setContentDescription(offers.getDescription())//imp
                .setContentImageUrl(merchantDetail.getCover())//imp

                // You use this to specify whether this content can be discovered publicly - default is public
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);//optional

        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing")
                .addControlParameter("MERCHANTID", String.valueOf(offers.getMerchant_id()))
                .addControlParameter("ISMERCHANTLINK", "false")
                .addControlParameter("OFFERPOSITION", String.valueOf(location))
                .addControlParameter("OFFERID", String.valueOf(offers.getId()));


        branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                } else {
                    Log.e("error", error.toString());
                }
            }
        });


        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(activity, "Check this out!", offers.getTitle())
                .setMoreOptionStyle(activity.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER);

        branchUniversalObject.showShareSheet(activity,
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {

                    }

                    @Override
                    public void onShareLinkDialogDismissed() {

                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                        Log.e("LinkShared", "success");
                    }

                    @Override
                    public void onChannelSelected(String channelName) {

                    }
                });

    }


}
