package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 28/09/16.
 */
public class ProfileRequest {

    public ProfileRequest(String first_name, String last_name, String email_id, String date_of_birth, String gender, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phone_number = phone_number;
    }

    private String first_name;
    private String last_name;
    private String email_id;
    private String date_of_birth;
    private String gender;
    private String phone_number;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
