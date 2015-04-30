package com.moralesf.masquerade.android.data;


import com.moralesf.masquerade.java.Api.Chat.ChatSendRequest;
import com.moralesf.masquerade.java.Api.Chat.ChatSendResponse;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateResponse;
import com.moralesf.masquerade.java.Api.Mask.MaskJoinRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskJoinResponse;
import com.moralesf.masquerade.java.Api.User.UserRegisterRequest;
import com.moralesf.masquerade.java.Api.User.UserRegisterResponse;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

public interface MasqueradeApi {

    @POST("/user/register")
    Observable<UserRegisterResponse> userRegister(@Body UserRegisterRequest body);

    @POST("/mask/create")
    Observable<MaskCreateResponse> maskCreate(@Header("x-auth-token") String authorization, @Body MaskCreateRequest body);

    @POST("/mask/join")
    Observable<MaskJoinResponse> maskJoin(@Header("x-auth-token") String authorization, @Body MaskJoinRequest body);

    @POST("/chat/send")
    Observable<ChatSendResponse> chatSend(@Header("x-auth-token") String authorization, @Body ChatSendRequest body);

}