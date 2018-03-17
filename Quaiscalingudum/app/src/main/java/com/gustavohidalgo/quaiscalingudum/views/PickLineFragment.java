package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.adapters.SearchLineAdapter;
import com.gustavohidalgo.quaiscalingudum.data.GtfsContract;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnTripSelectListener;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.TRIPS_ROUTE_ID;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.TRIPS_TRIP_HEADSIGN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link PickLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickLineFragment extends Fragment implements SearchView.OnQueryTextListener,
        OnTripSelectListener, LoaderManager.LoaderCallbacks<Cursor> {

//    public static final String[] PICK_LINE_PROJECTION = {
//            ROUTE_ID, SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, DIRECTION_ID, SHAPE_ID
//    };

    private static final int ID_TRIPS_LOADER = 353;

    SearchView mSearchView;
    @BindView(R.id.lines_rv)
    RecyclerView mLinesRV;
    @BindView(R.id.line_code_selected_tv)
    TextView mLineCodeSelectedTV;
    @BindView(R.id.line_name_selected_tv)
    TextView mLineNameSelectedTV;

    private Notification mNotification;
    private SearchLineAdapter mSearchLineAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private String mLineQuery = "";
    private String mDaysQuery;

    private OnEditNotificationListener mListener;

    public PickLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notification Parameter 1.
     * @return A new instance of fragment PickLineFragment.
     */
    public static PickLineFragment newInstance(Notification notification) {
        PickLineFragment fragment = new PickLineFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mDaysQuery = mNotification.getServiceIds();
        }
        getLoaderManager().initLoader(ID_TRIPS_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_line, container, false);
        ButterKnife.bind(this, view);
        mSearchView = view.findViewById(R.id.searchView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLinesRV.setLayoutManager(layoutManager);
        mSearchLineAdapter = new SearchLineAdapter(getActivity(), this);
        mLinesRV.setAdapter(mSearchLineAdapter);
        mSearchView.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditNotificationListener) {
            mListener = (OnEditNotificationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mLineQuery = newText;
        getLoaderManager().restartLoader(ID_TRIPS_LOADER, null, this);
        return true;
    }

    @Override
    public void tripSelected(String[] trip) {
        mLineCodeSelectedTV.setText(trip[TRIPS_ROUTE_ID]);
        mLineNameSelectedTV.setText(trip[TRIPS_TRIP_HEADSIGN]);
        mNotification.setLine(trip);
    }

    @OnClick(R.id.next_detail_bt)
    public void onNextPressed() {
        if (mListener != null && mNotification.getLine() != null) {
            mListener.toDetails(mNotification);
        }
    }

    @OnClick(R.id.back_eta_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toEta(mNotification);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_TRIPS_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri tripsQueryUri = GtfsContract.TripsEntry.TRIPS_CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = GtfsContract.TripsEntry._ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String selection = "(((" + GtfsContract.TripsEntry.ROUTE_ID + " LIKE ?) OR ("
                        + GtfsContract.TripsEntry.TRIP_HEADSIGN + " LIKE ?)) AND ("
                        + GtfsContract.TripsEntry.SERVICE_ID + " LIKE ?))";
                String[] selectionArgs = new String[]{"%" + mLineQuery + "%",
                        "%" + mLineQuery + "%", "%" + mDaysQuery + "%"};

                return new CursorLoader(getActivity(),
                        tripsQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSearchLineAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mLinesRV.smoothScrollToPosition(mPosition);
        //if (data.getCount() != 0) showWeatherDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSearchLineAdapter.swapCursor(null);

    }

}
