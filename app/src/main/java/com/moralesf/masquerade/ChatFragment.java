package com.moralesf.masquerade;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager chatLayoutManager;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        if(intent != null && intent.hasExtra(Intent.EXTRA_TITLE)){
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            getActivity().setTitle(title);
        }


        chatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        chatLayoutManager = new LinearLayoutManager(getActivity());
        chatRecyclerView.setLayoutManager(chatLayoutManager);

        // specify an adapter (see also next example)

        String[] myDataset = {
                "Holaa!",
                "Hola guapo, que tal?",
                "Uyy aquí, programando después de una noche seguida, y hablando solo...",
                "Como que solo, yo no existo?...",
                "No T_T"
        };

        chatAdapter = new ChatAdapter(getActivity(), myDataset);
        chatRecyclerView.setAdapter(chatAdapter);



        return rootView;
    }
}
