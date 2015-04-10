package com.moralesf.masquerade;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private String[] mDataset;
    private Context context;

    public ChatAdapter(Context context, String[] myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_chat, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        // ...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.chat_text_tv.setText(mDataset[i]);
        viewHolder.position = i;

        if ( (i & 1) == 0 ) {
            Drawable background = this.context.getResources().getDrawable(R.drawable.chat_me);
            viewHolder.chat_text_tv.setBackground(background);
            //viewHolder.chat_text_tv.setGravity(Gravity.END);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.chat_text_tv.getLayoutParams();
            params.gravity = Gravity.END;
            viewHolder.chat_text_tv.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        //public View chat_title_tv;
        public TextView chat_text_tv;
        public LinearLayout chat_ll;
        public int position;
        public ViewHolder(View v) {
            super(v);
            chat_text_tv = (TextView) v.findViewById(R.id.recycle_chat_text);
            chat_ll = (LinearLayout) v.findViewById(R.id.chat_ll);
            chat_ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}