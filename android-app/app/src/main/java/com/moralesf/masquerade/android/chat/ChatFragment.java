package com.moralesf.masquerade.android.chat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.java.Api.Chat.ChatSendRequest;
import com.moralesf.masquerade.java.Api.Chat.ChatSendResponse;

import rx.functions.Action1;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ChatFragment";
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private LinearLayoutManager chatLayoutManager;
    private String keygen;
    private long mask_id;
    private int mask_api_id;
    private EditText edit_txt;
    private ApiHelper apiHelper;

    private static final int CHAT_LOADER = 0;
    private static final int MASK_LOADER = 1;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle bundle = this.getArguments();

        apiHelper = new ApiHelper(getActivity());

        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        if(intent != null && intent.hasExtra(Intent.EXTRA_TITLE)){
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            getActivity().setTitle(title);
        }
        else if(bundle.containsKey(Intent.EXTRA_TITLE)){
            String title = bundle.getString(Intent.EXTRA_TITLE);
            getActivity().setTitle(title);
        }

        if(intent != null && intent.hasExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID)){
            mask_id = intent.getLongExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, 0L);
        }else if(bundle.containsKey(MasqueradeContract.ChatEntry.COLUMN_MASK_ID)){
            mask_id = bundle.getLong(MasqueradeContract.ChatEntry.COLUMN_MASK_ID);
        }


        chatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        chatLayoutManager = new LinearLayoutManager(getActivity());
        chatLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(chatLayoutManager);

        edit_txt = (EditText) rootView.findViewById(R.id.new_chat_chat_title);
        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!edit_txt.isFocusable()) {
                    return false;
                }
                if(event != null){
                    edit_txt.append("\n</br>");
                    return true;
                }

                sendMessage(edit_txt.getText().toString());
                return true;
            }
        });

        edit_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edit_txt.getRight() - edit_txt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        FlurryAgent.logEvent("chat_send_button_image");
                        edit_txt.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        return false;
                    }
                }
                return false;
            }
        });



        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void sendMessage(String text){
        if(text.length() == 0){
            return;
        }
        edit_txt.setText("");
        text = text.replace("\n", "<br />");

        FlurryAgent.logEvent("chat_sent");

        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, mask_id);
        values.put(MasqueradeContract.ChatEntry.COLUMN_TYPE, 1);
        values.put(MasqueradeContract.ChatEntry.COLUMN_TEXT, text);
        values.put(MasqueradeContract.ChatEntry.COLUMN_USER_ID, apiHelper.getUserId());
        values.put(MasqueradeContract.ChatEntry.COLUMN_READED, 1);

        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri insert = contentResolver.insert(MasqueradeContract.ChatEntry.CONTENT_URI, values);

        Cursor inserted_row = contentResolver.query(insert, null, null, null, null);
        inserted_row.moveToFirst();
        final long internal_id = inserted_row.getLong(MasqueradeContract.ChatEntry.INDEX_COLUMN_ID);

        String token = apiHelper.getToken();

        apiHelper.getApi().chatSend(token, new ChatSendRequest(mask_api_id, text))
                .subscribe(new Action1<ChatSendResponse>() {
                    @Override
                    public void call(ChatSendResponse response) {
                        ChatActivity.markAsSync(getActivity(), internal_id);
                    }
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CHAT_LOADER, null, this);
        getLoaderManager().initLoader(MASK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id){
            case CHAT_LOADER:
                uri = MasqueradeContract.ChatEntry.buildChatUri(mask_id);
                break;
            case MASK_LOADER:
                uri = MasqueradeContract.MaskEntry.buildChatByIdUri(mask_id);
                break;
        }

        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case CHAT_LOADER:
                chatAdapter = new ChatAdapter(getActivity(), data, apiHelper, edit_txt);
                chatRecyclerView.setAdapter(chatAdapter);
                break;
            case MASK_LOADER:
                if(data.getCount() > 0){
                    data.moveToFirst();
                    mask_api_id = data.getInt(MasqueradeContract.MaskEntry.INDEX_COLUMN_API_ID);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        chatAdapter = null;
        chatRecyclerView.setAdapter(chatAdapter);
    }
}
