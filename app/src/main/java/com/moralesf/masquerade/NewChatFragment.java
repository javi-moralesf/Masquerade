package com.moralesf.masquerade;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewChatFragment extends Fragment {

    public NewChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String key = new KeyGen().getKey();

        View rootView = inflater.inflate(R.layout.fragment_new_chat, container, false);

        TextView chat_key_tv = (TextView) rootView.findViewById(R.id.chat_key);
        chat_key_tv.setText(key);

        EditText chat_name_et = (EditText) rootView.findViewById(R.id.new_chat_chat_title);
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        chat_name_et.setHint(date);

        return rootView;
    }
}
