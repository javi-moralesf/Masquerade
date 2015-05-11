package com.moralesf.masquerade.android.chat;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moralesf.masquerade.ApiHelper;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.java.Date.DateParser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private ArrayList<String> masquerades;
    private ArrayList<String> masquerades_colors;
    private ArrayList<Integer> users;
    private ApiHelper api;
    private EditText chat;

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_CHAT_ID = 0;
    public static final int COL_CHAT_MASK_ID = 1;
    public static final int COL_CHAT_DATE = 2;
    public static final int COL_CHAT_TEXT = 3;
    public static final int COL_CHAT_TYPE = 4;
    public static final int COL_CHAT_USER_ID = 5;

    public ChatAdapter(Context context, Cursor cursor, ApiHelper api, EditText edit_text) {
        this.context = context;
        this.cursor = cursor;
        this.api = api;
        this.chat = edit_text;

       users = new ArrayList<Integer>();

       masquerades = new ArrayList<String>();
       masquerades.add("Bauta");
       masquerades.add("Columbina");
       masquerades.add("Medico Della Peste");
       masquerades.add("Moretta");
       masquerades.add("Volto");
       masquerades.add("Arlecchino");
       masquerades.add("Brighella");
       masquerades.add("Pulcinella");
       masquerades.add("La Ruffiana");
       masquerades.add("Scaramuccia");

       masquerades_colors = new ArrayList<String>();
       masquerades_colors.add("#a76c52");
       masquerades_colors.add("#504365");
       masquerades_colors.add("#853e4f");
       masquerades_colors.add("#654856");
       masquerades_colors.add("#000000");
       masquerades_colors.add("#ff7845");
       masquerades_colors.add("#ce6453");
       masquerades_colors.add("#5d9ea2");
       masquerades_colors.add("#31789c");
       masquerades_colors.add("#c83448");

        while(cursor.moveToNext()){
            int user_id = this.cursor.getInt(COL_CHAT_USER_ID);
            if(user_id > 0 && !users.contains(user_id)){
                users.add(user_id);
            }
        }
        cursor.moveToFirst();
        updateChatSate();
    }

    public int getUserCount(){

        return this.users.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_row, viewGroup, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.cursor.moveToPosition(i);
        int user_id = this.cursor.getInt(COL_CHAT_USER_ID);
        if(cursor.getInt(MasqueradeContract.ChatEntry.INDEX_COLUMN_READED) == 0){
            ChatActivity.markAsRead(context, cursor.getLong(MasqueradeContract.ChatEntry.INDEX_COLUMN_ID));
        }
        String name = "";
        if(user_id > 0 && getUserCount() > 2){
            int index = users.indexOf(user_id);
            name = "<font color='" + masquerades_colors.get(index) + "'>" + masquerades.get(index) + ": </font><br />";
        }
        String text = this.cursor.getString(COL_CHAT_TEXT);
        viewHolder.chat_row_text.setText(Html.fromHtml(name + text));
        if(user_id > 0){
            String date = DateParser.dateToChatTime(this.cursor.getString(COL_CHAT_DATE));
            viewHolder.chat_row_date.setText(date);
        }

        viewHolder.position = i;

        if(this.cursor.getInt(COL_CHAT_TYPE) == MasqueradeContract.ChatEntry.CHAT_TYPE_SYSTEM){
            customizeChat(viewHolder, R.drawable.chat_system, Gravity.CENTER);
        }
        else if (api.getUserId() == this.cursor.getInt(COL_CHAT_USER_ID) ) {
            customizeChat(viewHolder, R.drawable.chat_me, Gravity.END);
        }

        if(cursor.getInt(MasqueradeContract.ChatEntry.INDEX_COLUMN_SYNC) == 0){
            viewHolder.chat_row_sync.setVisibility(View.VISIBLE);
        }else{
            viewHolder.chat_row_sync.setVisibility(View.GONE);
        }

        //updateChatSate();
    }

    private void updateChatSate() {
        if(this.getUserCount() <= 1){
            chat.setFocusable(false);
            chat.setText(context.getString(R.string.chat_waiting_others));
        }else if(chat.getText().toString().equals(context.getString(R.string.chat_waiting_others))){
            chat.setFocusable(true);
            chat.setFocusableInTouchMode(true);
            chat.setText("");
        }
    }

    private void customizeChat(ViewHolder viewHolder, int resource, int gravity){
        // Check if we're running on Android 5.0 or higher
        Drawable background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = this.context.getResources().getDrawable(resource, null);
        } else {
            background = this.context.getResources().getDrawable(resource);
        }

        viewHolder.chat_row_group.setBackground(background);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_row_group.getLayoutParams();
        params.gravity = gravity;
        viewHolder.chat_row_group.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.cursor != null){
            count = this.cursor.getCount();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        //public View chat_title_tv;
        public TextView chat_row_text;
        public TextView chat_row_date;
        public ImageView chat_row_sync;
        public LinearLayout chat_row_group;
        public LinearLayout chat_row_ll;
        public int position;
        public ViewHolder(View v) {
            super(v);
            chat_row_text = (TextView) v.findViewById(R.id.chat_row_text);
            chat_row_date = (TextView) v.findViewById(R.id.chat_row_date);
            chat_row_sync = (ImageView) v.findViewById(R.id.chat_row_sync);
            chat_row_group = (LinearLayout) v.findViewById(R.id.chat_row_group);
            chat_row_ll = (LinearLayout) v.findViewById(R.id.chat_row_ll);
            chat_row_ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}