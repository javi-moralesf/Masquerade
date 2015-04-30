package com.moralesf.masquerade.android.Mask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.android.Chat.ChatActivity;
import com.moralesf.masquerade.android.data.MasqueradeApi;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateResponse;
import com.moralesf.masquerade.java.Key.KeyGen;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract.MaskEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A placeholder fragment containing a simple view.
 */
public class MaskFragment extends Fragment {

    public MaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String key = new KeyGen().getKey();

        View rootView = inflater.inflate(R.layout.mask_fragment, container, false);

        TextView chat_key_tv = (TextView) rootView.findViewById(R.id.chat_key);
        chat_key_tv.setText(key);

        final EditText chat_name_et = (EditText) rootView.findViewById(R.id.new_chat_chat_title);

        final String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        chat_name_et.setHint(date);


        final TextView mask_submit = (TextView) rootView.findViewById(R.id.mask_submit);
        mask_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = chat_name_et.getText().toString();
                if (title.length() == 0) {
                    title = date;
                }
                final String final_title = title;


                ApiHelper apiHelper = new ApiHelper(getActivity());
                String token = apiHelper.getToken();

                apiHelper.getApi().maskCreate(token, new MaskCreateRequest(key))
                        .subscribe(new Action1<MaskCreateResponse>() {
                            @Override
                            public void call(MaskCreateResponse response) {
                                ContentValues values = new ContentValues();
                                values.put(MaskEntry.COLUMN_KEYGEN, key);
                                values.put(MaskEntry.COLUMN_TITLE, final_title);
                                values.put(MaskEntry.COLUMN_API_ID, response.mask_id);

                                ContentResolver contentResolver = getActivity().getContentResolver();
                                contentResolver.insert(MasqueradeContract.MaskEntry.CONTENT_URI, values);

                                Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/" + response.mask_id),
                                        null, null, null, null);
                                c.moveToFirst();

                                long _id = c.getLong(0);
                                ChatActivity.startActivity(getActivity(), final_title, key, _id, response.mask_id);
                            }
                        });
            }
        });


        return rootView;
    }
}
