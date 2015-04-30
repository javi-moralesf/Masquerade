package com.moralesf.masquerade.java.Api.User;

public class UserRegisterRequest {
    final String regid;
    final String os;
    final String device;

    public UserRegisterRequest(String regid, String os, String device) {
        this.regid = regid;
        this.os = os;
        this.device = device;
    }
}
