package com.moralesf.masquerade.java.Api.Chat;

public class ChatSendRequest {
    final long mask_id;
    final String message;

    public ChatSendRequest(long mask_id, String message) {
        this.mask_id = mask_id;
        this.message = message;
    }
}
