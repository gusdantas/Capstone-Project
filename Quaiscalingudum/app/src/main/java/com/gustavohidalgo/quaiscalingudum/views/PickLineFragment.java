package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.adapters.SearchLineAdapter;
import com.gustavohidalgo.quaiscalingudum.data.TripsContract;
import com.gustavohidalgo.quaiscalingudum.data.TripsDbHelper;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnChooseLineListener;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.DIRECTION_ID;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.ROUTE_ID;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.SERVICE_ID;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.SHAPE_ID;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.TRIP_HEADSIGN;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.TRIP_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link PickLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickLineFragment extends Fragment implements SearchView.OnQueryTextListener,
        OnChooseLineListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String NOTIFICATION = "notification";
    private static final String LINES = "lines";

    public static final String[] TRIPS_PROJECTION = {
            ROUTE_ID, SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, DIRECTION_ID, SHAPE_ID
    };

    public static final int INDEX_TRIP_ROUTE_ID = 0;
    public static final int INDEX_TRIP_SERVICE_ID = 1;
    public static final int INDEX_TRIP_TRIP_ID = 2;
    public static final int INDEX_TRIP_TRIP_HEADSIGN_ID = 3;
    public static final int INDEX_TRIP_DIRECTION_ID = 4;
    public static final int INDEX_TRIP_SHAPE_ID = 5;

    private static final int ID_TRIPS_LOADER = 353;

    SearchView mSearchView;
    @BindView(R.id.lines_rv)
    RecyclerView mLinesRV;
    @BindView(R.id.line_code_selected_tv)
    TextView mLineCodeSelectedTV;
    @BindView(R.id.line_name_selected_tv)
    TextView mLineNameSelectedTV;

    private Notification mNotification;
    private ArrayList<String> mLines;
    private SearchLineAdapter mSearchLineAdapter;
    private SQLiteDatabase mDb;
    private Uri mUri;
    private int mPosition = RecyclerView.NO_POSITION;

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
    public static PickLineFragment newInstance(Notification notification, ArrayList<String> lines) {
        PickLineFragment fragment = new PickLineFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        args.putStringArrayList(LINES, lines);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mLines = getArguments().getStringArrayList(LINES);
        }
        //mUri = getActivity().getIntent().getData();
//      COMPLETED (17) Throw a NullPointerException if that URI is null
        //if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");
        getLoaderManager().initLoader(ID_TRIPS_LOADER, null, this);
        daysFilter(mNotification.getServiceIds());
        Toast.makeText(getActivity(), "stop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_line, container, false);
        ButterKnife.bind(this, view);
        mSearchView = view.findViewById(R.id.searchView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        TripsDbHelper tripsDbHelper = new TripsDbHelper(getActivity());
        mDb = tripsDbHelper.getWritableDatabase();
        mLinesRV.setLayoutManager(layoutManager);
        mSearchLineAdapter = new SearchLineAdapter(getActivity(), this);
        //mSearchLineAdapter.setChooseLineListener(this);
        //mSearchLineAdapter.setLines(mLines);
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
        mSearchLineAdapter.linesFilter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchLineAdapter.linesFilter(newText);
        return true;
    }

    @Override
    public void lineChosen(String[] line) {
        mLineCodeSelectedTV.setText(line[0]);
        mLineNameSelectedTV.setText(line[3]);
        mNotification.setLine(line);
    }

    public void daysFilter(ArrayList<String> filters) {
        ArrayList<String> itemsCopy = new ArrayList<>();
        ArrayList<String> itemsToRemove = new ArrayList<>();
        itemsCopy.addAll(mLines);
        mLines.clear();
        if(filters.isEmpty()){
            mLines.addAll(itemsCopy);
        } else {
            for (String filter : filters) {
                filter = filter.toUpperCase();
                itemsToRemove.clear();
                for(String item : itemsCopy){
                    String[] line = item.split(",");
                    if(line[1].toUpperCase().replaceAll("\"", "").equals(filter)){
                        mLines.add(item);
                        itemsToRemove.add(item);
                    }
                }
                itemsCopy.removeAll(itemsToRemove);
            }
        }
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
                Uri tripsQueryUri = TripsContract.TripsEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = TripsContract.TripsEntry.TRIP_ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                //String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(getActivity(),
                        tripsQueryUri,
                        TRIPS_PROJECTION,
                        null,
                        null,
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
