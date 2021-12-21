package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomersRegistration  implements Serializable {

    String message,name,email,phone_number,gender,id;
    ErrorRegisatration errors;
    int status;

    public static class ErrorRegisatration  implements Serializable {
        ArrayList<String> email,name,phone_number,gender,password;

        public ArrayList<String> getEmail() {
            return email;
        }

        public void setEmail(ArrayList<String> email) {
            this.email = email;
        }

        public ArrayList<String> getName() {
            return name;
        }

        public void setName(ArrayList<String> name) {
            this.name = name;
        }

        public ArrayList<String> getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(ArrayList<String> phone_number) {
            this.phone_number = phone_number;
        }

        public ArrayList<String> getGender() {
            return gender;
        }

        public void setGender(ArrayList<String> gender) {
            this.gender = gender;
        }

        public ArrayList<String> getPassword() {
            return password;
        }

        public void setPassword(ArrayList<String> password) {
            this.password = password;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ErrorRegisatration getErrors() {
        return errors;
    }

    public void setErrors(ErrorRegisatration errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
