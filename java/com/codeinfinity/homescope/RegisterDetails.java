package com.codeinfinity.homescope;

public class RegisterDetails {
    String name, email, password, uid;



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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public RegisterDetails() {

    }

    public RegisterDetails(String name, String email, String password, String uid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
    }
}
