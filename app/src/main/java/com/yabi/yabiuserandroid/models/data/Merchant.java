package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 01/10/16.
 */

public class Merchant {
        private int id;
        private String name;
        private String logo;
        private String cover;
        private String website;
        private String android_app;
        private String ios_app;
        private String custom_domain;
        private int is_active;
        private int category;
        private String timestamp;
        private String latitude;
        private String longitude;
        private int offer_count;

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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getAndroid_app() {
            return android_app;
        }

        public void setAndroid_app(String android_app) {
            this.android_app = android_app;
        }

        public String getIos_app() {
            return ios_app;
        }

        public void setIos_app(String ios_app) {
            this.ios_app = ios_app;
        }

        public String getCustom_domain() {
            return custom_domain;
        }

        public void setCustom_domain(String custom_domain) {
            this.custom_domain = custom_domain;
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

    public int getOffer_count() {
        return offer_count;
    }

    public void setOffer_count(int offer_count) {
        this.offer_count = offer_count;
    }
}

