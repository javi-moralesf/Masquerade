package com.moralesf.masquerade.android.Chat;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
    private RecyclerView.Adapter chatAdapter;
    private LinearLayoutManager chatLayoutManager;
    private String keygen;
    private long mask_id;
    private int mask_api_id;

    private static final int CHAT_LOADER = 0;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        if(intent != null && intent.hasExtra(Intent.EXTRA_TITLE)){
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            getActivity().setTitle(title);
        }


        chatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        chatLayoutManager = new LinearLayoutManager(getActivity());
        chatLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(chatLayoutManager);

        final EditText edit_txt = (EditText) rootView.findViewById(R.id.new_chat_chat_title);
        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(edit_txt.getText().toString());
                    edit_txt.setText("");
                    return true;
                }
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
                        sendMessage(edit_txt.getText().toString());
                        edit_txt.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        if(intent != null && intent.hasExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID)){
            mask_id = intent.getLongExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, 0L);
        }
        if(intent != null && intent.hasExtra(MasqueradeContract.MaskEntry.COLUMN_API_ID)){
            mask_api_id = intent.getIntExtra(MasqueradeContract.MaskEntry.COLUMN_API_ID, 0);
        }

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void sendMessage(String text){
        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, mask_id);
        values.put(MasqueradeContract.ChatEntry.COLUMN_MINE, 1);
        values.put(MasqueradeContract.ChatEntry.COLUMN_TEXT, text);
        values.put(MasqueradeContract.ChatEntry.COLUMN_USER_ID, 1);

        ContentResolver contentResolver = getActivity().getContentResolver();
        contentResolver.insert(MasqueradeContract.ChatEntry.CONTENT_URI, values);

        ApiHelper apiHelper = new ApiHelper(getActivity());
        String token = apiHelper.getToken();

        apiHelper.getApi().chatSend(token, new ChatSendRequest(mask_api_id, text))
                .subscribe(new Action1<ChatSendResponse>() {
                    @Override
                    public void call(ChatSendResponse response) {
                        //TODO:Add icon to see difference between sended or pending to send
                    }
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CHAT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri chatUri = MasqueradeContract.ChatEntry.buildChatUri(
                mask_id);

        return new CursorLoader(getActivity(), chatUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        chatAdapter = new ChatAdapter(getActivity(), data);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        chatAdapter = null;
        chatRecyclerView.setAdapter(chatAdapter);
    }
}
