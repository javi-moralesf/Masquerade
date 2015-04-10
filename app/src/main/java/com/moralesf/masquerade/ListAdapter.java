package com.moralesf.masquerade;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private String[] mDataset;

    public ListAdapter(Context context, Cursor c, int flags) {
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_chat_list, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        // ...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.chat_title_tv.setText(mDataset[i]);
        viewHolder.chat_title_icon_tv.setText(mDataset[i].substring(0, 1));
        viewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        //public View chat_title_tv;
        public TextView chat_title_tv;
        public TextView chat_title_icon_tv;
        public FrameLayout chat_list_fl;
        public int position;
        public ViewHolder(View v) {
            super(v);
            chat_title_tv = (TextView) v.findViewById(R.id.chat_title);
            chat_title_icon_tv = (TextView) v.findViewById(R.id.chat_title_icon);
            chat_list_fl = (FrameLayout) v.findViewById(R.id.chat_list_fl);
            chat_list_fl.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("aaa", "POSITION: "+String.valueOf(this.position));
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra(Intent.EXTRA_TITLE, chat_title_tv.getText());
            v.getContext().startActivity(intent);
        }
    }
}