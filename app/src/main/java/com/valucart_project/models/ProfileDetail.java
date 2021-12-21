package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDetail  implements Serializable {

    int status;
    public Data data = new Data();

    public static class Data implements Serializable {

        Boolean email_verified,phone_number_verified;
        String id,name,gender,email,oauth_provider,phone_number;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOauth_provider() {
            return oauth_provider;
        }

        public void setOauth_provider(String oauth_provider) {
            this.oauth_provider = oauth_provider;
        }

        public String getPhone_number() {
            return phone_number==null?"":phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public Boolean getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(Boolean email_verified) {
            this.email_verified = email_verified;
        }

        public Boolean getPhone_number_verified() {
            return phone_number_verified;
        }

        public void setPhone_number_verified(Boolean phone_number_verified) {
            this.phone_number_verified = phone_number_verified;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
