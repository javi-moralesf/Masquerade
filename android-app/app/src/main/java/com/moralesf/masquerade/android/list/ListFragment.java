package com.moralesf.masquerade.android.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.moralesf.masquerade.R;
import com.moralesf.masquerade.android.mask.MaskActivity;
import com.moralesf.masquerade.android.data.MasqueradeContract.MaskEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LIST_LOADER = 0;
    private static final String TAG = "ListFragment";
    private RecyclerView listRecyclerView;
    private ListAdapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;

    private static final String SELECTED_KEY = "selected_position";

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
        if (listAdapter.sPosition != -1) {
            listRecyclerView.smoothScrollToPosition(listAdapter.sPosition);
        }

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

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            listAdapter.sPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = listAdapter.getPosition();
            listAdapter.sPosition = position;
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        Cursor c = listAdapter.getItemAt(position);

        long id = c.getLong(ListAdapter.COL_MASK_ID);
        int api_id = c.getInt(ListAdapter.COL_MASK_DB_ID);
        switch (item.getItemId()) {
            case R.id.list_contextual_menu_delete:
                //Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                MaskActivity.deleteMask(getActivity(), id, api_id);
                FlurryAgent.logEvent("list_contextual_delete");
                return true;
            case R.id.list_contextual_menu_rename:
                editMasqueradeName(c);
                return true;
            case R.id.list_contextual_menu_view_code:
                viewMasqueradeCode(c);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editMasqueradeName(final Cursor item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.mask_edit_title));

        final EditText input = new EditText(getActivity());
        String title = item.getString(MaskEntry.INDEX_COLUMN_TITLE);
        input.setText(title);
        input.setSingleLine(true);
        alert.setView(input);

        Map<String, String> flurryParams = new HashMap<String, String>();
        flurryParams.put("title", title);

        FlurryAgent.logEvent("list_contextual_mask_delete", flurryParams);

        alert.setPositiveButton(getString(R.string.mask_edit_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MaskActivity.editMaskTitle(getActivity(), item.getLong(MaskEntry.INDEX_COLUMN_ID), input.getText().toString());
            }
        });

        alert.setNegativeButton(getString(R.string.mask_edit_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        input.setSelection(input.getText().length());
    }

    private void viewMasqueradeCode(final Cursor item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.mask_view_code));
        alert.setMessage(item.getString(MaskEntry.INDEX_COLUMN_KEYGEN));

        FlurryAgent.logEvent("list_contextual_mask_view_code");

        alert.setPositiveButton(getString(R.string.mask_view_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (listAdapter.sPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, listAdapter.sPosition);
        }
        super.onSaveInstanceState(outState);
    }

}
