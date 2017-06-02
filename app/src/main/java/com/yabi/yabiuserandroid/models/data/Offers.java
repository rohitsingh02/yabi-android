package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 01/10/16.
 */
public class Offers {
    private int id;
    private String title;
    private String code;
    private String description;
    private String t_n_c;
    private int type;
    private String expiry_mode;
    private String expiry_date_and_time;
    private String timestamp;
    private int is_active;
    private int merchant_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getT_n_c() {
        return t_n_c;
    }

    public void setT_n_c(String t_n_c) {
        this.t_n_c = t_n_c;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExpiry_mode() {
        return expiry_mode;
    }

    public void setExpiry_mode(String expiry_mode) {
        this.expiry_mode = expiry_mode;
    }

    public String getExpiry_date_and_time() {
        return expiry_date_and_time;
    }

    public void setExpiry_date_and_time(String expiry_date_and_time) {
        this.expiry_date_and_time = expiry_date_and_time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }
}
