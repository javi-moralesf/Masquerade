package com.moralesf.masquerade.java.Api.User;

public class UserRegisterRequest {
    final String regid;
    final String os;
    final String device;
    final String old_token;

    public UserRegisterRequest(String regid, String os, String device, String old_token) {
        this.regid = regid;
        this.os = os;
        this.device = device;
        this.old_token = old_token;
    }
}
