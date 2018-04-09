package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.data.GtfsContract;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.BusDateTime;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.models.StopTime;
import com.gustavohidalgo.quaiscalingudum.models.Trip;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_FREQUENCIES_LOADER = 11;
    private static final int ID_STOP_TIMES_LOADER = 12;
    private static final int ID_STOPS_LOADER = 13;

    private BusNotification mBusNotification;
    private Trip mTrip;
    private Cursor mStopTimesFiltered, mFrequenciesFiltered, mStopsFiltered;
    private DateTime mArriveBusTime;
    private String mStopId;
    private int mStopTimePosition = 0;
    ArrayAdapter<String> mStopNamesArrayAdapter;
    ArrayList<String> mStopsNames;

    @BindView(R.id.line_code_tv)
    TextView mLineCodeTV;
    @BindView(R.id.line_name_tv)
    TextView mLineNameTV;
    @BindView(R.id.departure_stop_sp)
    Spinner mDepartureStopSP;
    @BindView(R.id.departure_time_rg)
    RadioGroup mDepartureTimeRG;
    @BindView(R.id.departure_time_before_rb)
    RadioButton mDepartureTimeBeforeRB;
    @BindView(R.id.departure_time_after_rb)
    RadioButton mDepartureTimeAfterRB;

    private OnEditNotificationListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param busNotification Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(BusNotification busNotification) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, busNotification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStopsNames = new ArrayList<>();
        if (getArguments() != null) {
            mBusNotification = getArguments().getParcelable(NOTIFICATION);
            mTrip = mBusNotification.getTrip();
            getLoaderManager().initLoader(ID_STOP_TIMES_LOADER, null, this);
            getLoaderManager().initLoader(ID_FREQUENCIES_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        mLineCodeTV.setText(mTrip.getRouteId().replaceAll("\"", ""));
        mLineNameTV.setText(mTrip.getTripHeadsign().replaceAll("\"", ""));
        mStopNamesArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                mStopsNames
        );
        mDepartureStopSP.setOnItemSelectedListener(this);
        mDepartureStopSP.setAdapter(mStopNamesArrayAdapter);
        mDepartureTimeRG.setOnCheckedChangeListener(this);
        return view;
    }

    @OnClick(R.id.next_notifications_bt)
    public void onNextPressed() {
        if (mListener != null) {
            mBusNotification.setArriveDateTime(NotificationUtils.dateTimeToBus(mArriveBusTime));
            mListener.toSetNotifications(mBusNotification);
        }
    }

    @OnClick(R.id.back_pickline_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toPickLine(mBusNotification);
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        mBusNotification.setDepartureStop(mStopsNames.get(0));
        mBusNotification.setArriveStop(mStopsNames.get(position));

        mStopTimesFiltered.moveToFirst();

        mBusNotification.setDepartureStopTime(new StopTime(
                mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)
        ));

        mStopTimesFiltered.moveToPosition(position);

//        while(mStopTimesFiltered.getPosition() != position){
//
//            String[] stopBefore = {
//                mStopTimesFiltered.getString(STOP_TIMES__ID),
//                mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
//                mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
//                mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
//                mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
//                mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)};
//            int[] timeBefore = stringToTime(stopBefore[STOP_TIMES_DEPARTURE_TIME]);
//            DateTime dateTimeBefore = new DateTime(1970, 1, 1, timeBefore[HOUR],
//                    timeBefore[MINUTE], 0);
//
//            mStopTimesFiltered.moveToNext();
//
//            String[] stopAfter = {
//                    mStopTimesFiltered.getString(STOP_TIMES__ID),
//                    mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
//                    mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
//                    mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
//                    mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
//                    mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)};
//            int[] timeAfter = stringToTime(stopAfter[STOP_TIMES_DEPARTURE_TIME]);
//            DateTime dateTimeAfter = new DateTime(1970, 1, 1, timeAfter[HOUR],
//                    timeAfter[MINUTE], 0);
//
//            long millis = dateTimeAfter.getMillis() - dateTimeBefore.getMillis();
//            int seconds = (int) millis/1000;
//        }

        mBusNotification.setArriveStopTime(new StopTime(
                mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)
        ));

        // time get by the bus from start to the choosen stop
        int[] departureRefTime = stringToTime(mBusNotification.getDepartureStopTime().getArrivalTime());
        DateTime departureRefDt = new DateTime(1970, 1, 1, 0,
                departureRefTime[MINUTE], 0);

        int[] arrivalRefTime = stringToTime(mBusNotification.getArriveStopTime().getArrivalTime());
        DateTime arrivalRefDt = new DateTime(1970, 1, 1,
                (arrivalRefTime[HOUR] - departureRefTime[HOUR]), arrivalRefTime[MINUTE],
                0);

        DateTime travelDt = new DateTime(arrivalRefDt.getMillis() - departureRefDt.getMillis());

        // get the start time considering the bus arriving at the choosen point in the choosen time
        DateTime desiredArrivalDt = new DateTime(1970,1,1,
                mBusNotification.getArriveDateTime().getHourOfDay(),
                mBusNotification.getArriveDateTime().getMinuteOfHour(), 0);
        DateTime desiredDepartureDt = new DateTime(desiredArrivalDt.getMillis() - travelDt.getMillis());
        mBusNotification.setDepartureDateTime(new BusDateTime(
                mBusNotification.getArriveDateTime().getYear(),
                mBusNotification.getArriveDateTime().getMonthOfYear(),
                mBusNotification.getArriveDateTime().getDayOfMonth(),
                desiredDepartureDt.getHourOfDay(),
                desiredDepartureDt.getMinuteOfHour()));

        // get the headway_sec atr the time of the bus departuring
        int headway = 0;
        mFrequenciesFiltered.moveToFirst();
        for (int i = 0; i < mFrequenciesFiltered.getCount(); i++) {
            int hour = Integer.parseInt(mFrequenciesFiltered.getString(2)
                    .replaceAll("\"", "").split(":")[0]);
            if (hour == desiredDepartureDt.getHourOfDay()){
                headway = 1000 * Integer.parseInt(mFrequenciesFiltered.getString(4)
                        .replaceAll("\"", ""));
                break;
            }
            mFrequenciesFiltered.moveToNext();
        }

        while((desiredDepartureDt.getMillis() < desiredArrivalDt.getMillis())){
            desiredDepartureDt = new DateTime(desiredDepartureDt.getMillis() + headway);
        }

        DateTime arrivalDtBefore = new DateTime(desiredDepartureDt.getMillis() - headway);
        DateTime arrivalDtAfter = new DateTime(desiredDepartureDt.getMillis());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        mDepartureTimeBeforeRB.setText(arrivalDtBefore.toString(fmt));
        mDepartureTimeAfterRB.setText(arrivalDtAfter.toString(fmt));
        mDepartureTimeAfterRB.setChecked(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = group.findViewById(checkedId);
        String[] choosenTime = button.getText().toString().split(":");
        mArriveBusTime = new DateTime(mBusNotification.getArriveDateTime().getYear(),
                mBusNotification.getArriveDateTime().getMonthOfYear(),
                mBusNotification.getArriveDateTime().getDayOfMonth(),
                Integer.parseInt(choosenTime[HOUR]),
                Integer.parseInt(choosenTime[MINUTE]),0);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri queryUri;
        String sortOrder;
        String selection;
        String[] selectionArgs;

        switch (id) {
            case ID_STOP_TIMES_LOADER:
                queryUri = GtfsContract.StopTimesEntry.STOP_TIMES_CONTENT_URI;
                sortOrder = GtfsContract.StopTimesEntry._ID + " ASC";
                selection = "(" + GtfsContract.StopTimesEntry.TRIP_ID + " LIKE ?)";
                selectionArgs = new String[]{"%" + mTrip.getTripId() + "%"};

                return new CursorLoader(getActivity(),
                        queryUri,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder);

            case ID_FREQUENCIES_LOADER:
                queryUri = GtfsContract.FrequenciesEntry.FREQUENCIES_CONTENT_URI;
                sortOrder = GtfsContract.FrequenciesEntry._ID + " ASC";
                selection = "(" + GtfsContract.FrequenciesEntry.TRIP_ID + " LIKE ?)";
                selectionArgs = new String[]{"%" + mTrip.getTripId() + "%"};

                return new CursorLoader(getActivity(),
                        queryUri,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder);

            case ID_STOPS_LOADER:
                queryUri = GtfsContract.StopsEntry.STOPS_CONTENT_URI;
                sortOrder = GtfsContract.StopsEntry._ID + " ASC";
                selection = "(" + GtfsContract.StopsEntry.STOP_ID + " LIKE ?)";
                selectionArgs = new String[]{mStopId};

                return new CursorLoader(getActivity(),
                        queryUri,
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
        data.moveToFirst();
        switch (loader.getId()) {
            case ID_FREQUENCIES_LOADER:
                mFrequenciesFiltered = data;
                break;
            case ID_STOP_TIMES_LOADER:
                mStopTimesFiltered = data;
                loadStopNames();
                break;
            case ID_STOPS_LOADER:
                mStopsFiltered = data;
                mStopsFiltered.moveToFirst();
                mStopsNames.add(mStopsFiltered.getString(STOPS_STOP_NAME));
                mStopTimePosition++;
                loadStopNames();
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    void loadStopNames(){
        if (mStopTimePosition < mStopTimesFiltered.getCount()) {
            mStopTimesFiltered.moveToPosition(mStopTimePosition);
            mStopId = mStopTimesFiltered.getString(STOP_TIMES_STOP_ID);
            getLoaderManager().restartLoader(ID_STOPS_LOADER, null, this);
        } else {
            mStopNamesArrayAdapter.notifyDataSetChanged();
        }
    }

    private int[] stringToTime(String time){
        int hour = Integer.parseInt(time.replaceAll("\"", "").split(":")[HOUR]);
        int minute = Integer.parseInt(time.replaceAll("\"", "").split(":")[MINUTE]);
        return new int[]{hour, minute};
    }
}
