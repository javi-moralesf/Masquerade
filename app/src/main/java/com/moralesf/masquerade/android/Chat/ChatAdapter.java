package com.moralesf.masquerade.android.Chat;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moralesf.masquerade.R;
import com.moralesf.masquerade.java.Date.DateParser;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_CHAT_ID = 0;
    public static final int COL_CHAT_MASK_ID = 1;
    public static final int COL_CHAT_DATE = 2;
    public static final int COL_CHAT_TEXT = 3;
    public static final int COL_CHAT_MINE = 4;

    public ChatAdapter(Context context, Cursor c) {
        this.context = context;
        this.cursor = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_row, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        // ...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.cursor.moveToPosition(i);
        viewHolder.chat_row_text.setText(this.cursor.getString(COL_CHAT_TEXT));
        String date = DateParser.dateToChatTime(this.cursor.getString(COL_CHAT_DATE));
        viewHolder.chat_row_date.setText(date);
        viewHolder.position = i;

        if ( Integer.valueOf(this.cursor.getString(COL_CHAT_MINE)) == 1 ) {
            // Check if we're running on Android 5.0 or higher
            Drawable background;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                background = this.context.getResources().getDrawable(R.drawable.chat_me, null);
            } else {
                background = this.context.getResources().getDrawable(R.drawable.chat_me);
            }

            viewHolder.chat_row_group.setBackground(background);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_row_group.getLayoutParams();
            params.gravity = Gravity.END;
            viewHolder.chat_row_group.setLayoutParams(params);
        }
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
        public LinearLayout chat_row_group;
        public LinearLayout chat_row_ll;
        public int position;
        public ViewHolder(View v) {
            super(v);
            chat_row_text = (TextView) v.findViewById(R.id.chat_row_text);
            chat_row_date = (TextView) v.findViewById(R.id.chat_row_date);
            chat_row_group = (LinearLayout) v.findViewById(R.id.chat_row_group);
            chat_row_ll = (LinearLayout) v.findViewById(R.id.chat_row_ll);
            chat_row_ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}