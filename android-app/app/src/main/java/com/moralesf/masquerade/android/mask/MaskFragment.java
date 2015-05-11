package com.moralesf.masquerade.android.mask;

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

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.android.chat.ChatActivity;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.android.list.ListActivity;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateRequest;
import com.moralesf.masquerade.java.Api.Mask.MaskCreateResponse;
import com.moralesf.masquerade.java.Key.KeyGen;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract.MaskEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;


/**
 * A placeholder fragment containing a simple view.
 */
public class MaskFragment extends Fragment {

    public MaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ApiHelper apiHelper = new ApiHelper(getActivity());
        final String token = apiHelper.getToken();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor c = contentResolver.query(MasqueradeContract.MaskEntry.CONTENT_URI, null, null, null, null);
        ArrayList<String> existing_codes = new ArrayList<String>();
        while(c.moveToNext()){
            String code = c.getString(MaskEntry.INDEX_COLUMN_KEYGEN);
            code = code.substring(code.lastIndexOf("@") + 1);
            existing_codes.add(code);
        }

        KeyGen gen = new KeyGen(2);
        gen.setFalsePositives(existing_codes);

        final String key = new StringBuilder()
                    .append(apiHelper.getKey())
                    .append("@")
                    .append(gen.getKey())
                    .toString();





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
                Map<String, String> flurryParams = new HashMap<String, String>();
                if (title.length() == 0) {
                    title = date;
                    flurryParams.put("empty_title", "yes");
                }else{
                    flurryParams.put("empty_title", "no");
                }
                final String final_title = title;


                FlurryAgent.logEvent("mask_created", flurryParams);


                ContentValues values = new ContentValues();
                values.put(MaskEntry.COLUMN_KEYGEN, key);
                values.put(MaskEntry.COLUMN_TITLE, final_title);
                //values.put(MaskEntry.COLUMN_API_ID, response.mask_id);

                ContentResolver contentResolver = getActivity().getContentResolver();
                contentResolver.insert(MasqueradeContract.MaskEntry.CONTENT_URI, values);

                Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/key/" + key),
                        null, null, null, null);
                c.moveToFirst();

                final long _id = c.getLong(0);
                if(ListActivity.mTwoPane){
                    getActivity().finish();
                }else{
                    ChatActivity.startActivity(getActivity(), final_title, key, _id, 0);
                }

                apiHelper.getApi().maskCreate(token, new MaskCreateRequest(key))
                        .subscribe(new Action1<MaskCreateResponse>() {
                            @Override
                            public void call(MaskCreateResponse response) {
                                ContentValues values = new ContentValues();
                                values.put(MaskEntry.COLUMN_API_ID, response.mask_id);
                                values.put(MaskEntry.COLUMN_SYNC, 1);
                                String where = "_id = "+_id;

                                ContentResolver contentResolver = getActivity().getContentResolver();
                                contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);
                            }
                        });
            }
        });


        return rootView;
    }
}
