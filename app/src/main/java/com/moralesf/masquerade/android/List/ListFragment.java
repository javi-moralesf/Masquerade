package com.moralesf.masquerade.android.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.data.MasqueradeContract;
import com.moralesf.masquerade.android.data.MasqueradeContract.MaskEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LIST_LOADER = 0;
    private RecyclerView listRecyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri maskUri = MaskEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), maskUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter = new ListAdapter(getActivity(), data);
        listRecyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter = null;
        listRecyclerView.setAdapter(listAdapter);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        listRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_list_recycler_view);
        listRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        listLayoutManager = new LinearLayoutManager(getActivity());
        listRecyclerView.setLayoutManager(listLayoutManager);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
