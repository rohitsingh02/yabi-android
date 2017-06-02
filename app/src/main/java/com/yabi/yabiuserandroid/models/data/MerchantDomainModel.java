package com.yabi.yabiuserandroid.models.data;

import java.util.List;

/**
 * Created by rohitsingh on 29/10/16.
 */

public class MerchantDomainModel {

    private int id;
    private String name;
    private String logo;
    private int is_active;
    private int category;
    private String timestamp;
    private String latitude;
    private String longitude;
    private List<Offers> offers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Offers> getOffersList() {
        return offers;
    }

    public void setOffersList(List<Offers> offers) {
        this.offers = offers;
    }


}
