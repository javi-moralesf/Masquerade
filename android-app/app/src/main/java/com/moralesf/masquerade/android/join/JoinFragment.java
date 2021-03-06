package com.moralesf.masquerade.android.join;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.chat.ChatActivity;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.android.list.ListActivity;
import com.moralesf.masquerade.java.Api.Mask.MaskJoinRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskJoinResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * A placeholder fragment containing a simple view.
 */
public class JoinFragment extends Fragment {

    public JoinFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.join_fragment, container, false);

        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        final EditText chat_name_et = (EditText) rootView.findViewById(R.id.join_chat_title);
        chat_name_et.setHint(date);

        final EditText chat_key_et = (EditText) rootView.findViewById(R.id.chat_key);

        final TextView join_submit = (TextView) rootView.findViewById(R.id.join_submit);
        join_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = chat_key_et.getText().toString();
                if(key.length() >= 3){
                    String chat_title = chat_name_et.getText().toString();
                    Map<String, String> flurryParams = new HashMap<String, String>();
                    if(chat_title.length() == 0){
                        chat_title = chat_name_et.getHint().toString();
                        flurryParams.put("empty_title", "yes");
                    }else{
                        flurryParams.put("empty_title", "no");
                    }

                    FlurryAgent.logEvent("mask_joined", flurryParams);

                    final String title = chat_title;

                    ApiHelper apiHelper = new ApiHelper(getActivity());
                    String token = apiHelper.getToken();

                    ContentValues values = new ContentValues();
                    values.put(MasqueradeContract.MaskEntry.COLUMN_KEYGEN, key);
                    values.put(MasqueradeContract.MaskEntry.COLUMN_TITLE, title);

                    ContentResolver contentResolver = getActivity().getContentResolver();
                    contentResolver.insert(MasqueradeContract.MaskEntry.CONTENT_URI, values);

                    Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/key/" + key),
                            null, null, null, null);
                    c.moveToFirst();

                    final long _id = c.getLong(0);
                    if(ListActivity.mTwoPane){
                        getActivity().finish();
                    }else{
                        ChatActivity.startActivity(getActivity(), title, key, _id, 0);
                    }



                    apiHelper.getApi().maskJoin(token, new MaskJoinRequest(key))
                            .subscribe(new Action1<MaskJoinResponse>() {
                                @Override
                                public void call(MaskJoinResponse response) {
                                    ContentValues values = new ContentValues();
                                    values.put(MasqueradeContract.MaskEntry.COLUMN_API_ID, response.mask_id);
                                    values.put(MasqueradeContract.MaskEntry.COLUMN_SYNC, 1);

                                    String where = "_id = "+_id;

                                    ContentResolver contentResolver = getActivity().getContentResolver();
                                    contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), getString(R.string.invalid_key)
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }
}
