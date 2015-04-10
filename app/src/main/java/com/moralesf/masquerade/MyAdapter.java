package com.moralesf.masquerade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private String[] mDataset;

    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_text_view, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        // ...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.chat_title_tv.setText(mDataset[i]);
        viewHolder.chat_title_icon_tv.setText(mDataset[i].substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public View chat_title_tv;
        public TextView chat_title_tv;
        public TextView chat_title_icon_tv;
        public ViewHolder(View v) {
            super(v);
            chat_title_tv = (TextView) v.findViewById(R.id.chat_title);
            chat_title_icon_tv = (TextView) v.findViewById(R.id.chat_title_icon);
        }
    }
}