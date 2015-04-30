package com.moralesf.masquerade;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moralesf.masquerade.android.data.MasqueradeApi;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class ApiHelper {
    Context context;
    private MasqueradeApi api;
    private String token;
    private int user_id;

    private static final String PROPERTY_TOKEN = "api_token";
    private static final String PROPERTY_USER_ID = "api_user_id";

    public ApiHelper(Context context){
        this.context = context;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.api_host))
                .build();

        api = restAdapter.create(MasqueradeApi.class);

        final SharedPreferences prefs = getPreferences(context);
        this.token = prefs.getString(PROPERTY_TOKEN, "");
        this.user_id = prefs.getInt(PROPERTY_USER_ID, 0);
    }

    public MasqueradeApi getApi(){
        return api;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        final SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_TOKEN, token);
        editor.apply();
    }

    public int getUserId() {
        return this.user_id;
    }
    public void setUserId(int user_id) {
        final SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PROPERTY_USER_ID, user_id);
        editor.apply();
    }

    private SharedPreferences getPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return context.getSharedPreferences(ApiHelper.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}
