package com.moralesf.masquerade.android.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moralesf.masquerade.android.Chat.ChatActivity;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_MASK_ID = 0;
    public static final int COL_MASK_DB_ID = 1;
    public static final int COL_MASK_DATE = 2;
    public static final int COL_MASK_KEYGEN = 3;
    public static final int COL_MASK_TITLE = 4;

    public ListAdapter(Context context, Cursor c) {
        this.context = context;
        this.cursor = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        // ...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.cursor.moveToPosition(i);
        String title = this.cursor.getString(COL_MASK_TITLE);

        if(title.length() == 0){
            return;
        }
        viewHolder.chat_title_tv.setText(title);
        viewHolder.chat_title_icon_tv.setText(title.substring(0, 1));
        viewHolder.position = i;
        viewHolder._id = this.cursor.getLong(COL_MASK_ID);
        viewHolder.mask_api_id = this.cursor.getInt(COL_MASK_DB_ID);
        viewHolder.keygen = this.cursor.getString(COL_MASK_KEYGEN);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.cursor != null){
            count = this.cursor.getCount();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case
        //public View chat_title_tv;
        public TextView chat_title_tv;
        public TextView chat_title_icon_tv;
        public FrameLayout chat_list_fl;
        public long _id;
        public int mask_api_id;
        public int position;
        public String keygen;
        public ViewHolder(View v) {
            super(v);
            chat_title_tv = (TextView) v.findViewById(R.id.chat_title);
            chat_title_icon_tv = (TextView) v.findViewById(R.id.chat_title_icon);
            chat_list_fl = (FrameLayout) v.findViewById(R.id.chat_list_fl);
            chat_list_fl.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra(Intent.EXTRA_TITLE, chat_title_tv.getText());
            intent.putExtra(Intent.EXTRA_UID, keygen);
            intent.putExtra(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, _id);
            intent.putExtra(MasqueradeContract.MaskEntry.COLUMN_API_ID, mask_api_id);
            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}