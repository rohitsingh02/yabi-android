package com.yabi.yabiuserandroid.models.data;

import java.util.List;

/**
 * Created by rohitsingh on 12/10/16.
 */

public class MerchantListingObject {

    List<Merchant> data;

    public List<Merchant> getMerchantList() {
        return data;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.data = merchantList;
    }
}
