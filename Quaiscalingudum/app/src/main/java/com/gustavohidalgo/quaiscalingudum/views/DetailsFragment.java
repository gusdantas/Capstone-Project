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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.data.GtfsContract;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    private Notification mNotification;
    private String[] mLine;
    private Cursor mStopTimesFiltered, mFrequenciesFiltered;
    private DateTime mDateTime;
    private String mTripId = "";
    SimpleCursorAdapter mSelectStopAdapter;
    //private int mPosition = RecyclerView.NO_POSITION;

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
     * @param notification Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(Notification notification) {
        DetailsFragment fragment = new DetailsFragment();
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
            mLine = mNotification.getLine();
            mTripId = mLine[2];
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
        mLineCodeTV.setText(mLine[TRIPS_ROUTE_ID].replaceAll("\"", ""));
        mLineNameTV.setText(mLine[TRIPS_TRIP_HEADSIGN].replaceAll("\"", ""));
        String[] strings = {GtfsContract.StopTimesEntry.STOP_ID,
                GtfsContract.StopTimesEntry.STOP_SEQUENCE};
        int[] ints = {android.R.id.text1, android.R.id.text1};
        mSelectStopAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                null,
                strings,
                ints,
                0);
        mDepartureStopSP.setOnItemSelectedListener(this);
        mDepartureStopSP.setAdapter(mSelectStopAdapter);
        mDepartureTimeRG.setOnCheckedChangeListener(this);
        return view;
    }

    @OnClick(R.id.next_notifications_bt)
    public void onNextPressed() {
        if (mListener != null) {
            mNotification.setDateTime(mDateTime);
            mListener.toSetNotifications(mNotification);
        }
    }

    @OnClick(R.id.back_pickline_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toPickLine(mNotification);
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
        mStopTimesFiltered.moveToFirst();
        String[] terminalDepartureStop = {
                mStopTimesFiltered.getString(STOP_TIMES__ID),
                mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)};

        mStopTimesFiltered.moveToPosition(position);
        String[] desiredStop = {
                mStopTimesFiltered.getString(STOP_TIMES__ID),
                mStopTimesFiltered.getString(STOP_TIMES_TRIP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_ARRIVAL_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_DEPARTURE_TIME),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_ID),
                mStopTimesFiltered.getString(STOP_TIMES_STOP_SEQUENCE)};
        mNotification.setStop(desiredStop);

        // time get by the bus from start to the choosen stop
        int[] refSaidaTerminalTabela = stringToTime(terminalDepartureStop[STOP_TIMES_DEPARTURE_TIME]);
        DateTime refSaidaTerminal = new DateTime(0, 1, 1, 0,
                refSaidaTerminalTabela[MINUTE], 0);

        int[] refChegadaPontoTabela = stringToTime(mNotification.getStop()[STOP_TIMES_ARRIVAL_TIME]);
        DateTime refChegadaPonto = new DateTime(0, 1, 1,
                (refChegadaPontoTabela[HOUR] - refSaidaTerminalTabela[HOUR]), refChegadaPontoTabela[MINUTE],
                0);

        DateTime tempoViagem = new DateTime(refChegadaPonto.getMillis() - refSaidaTerminal.getMillis());

        // get the start time considering the bus arriving at the choosen point in the choosen time
        DateTime horaChegadaDesejada = new DateTime(0,1,1,
                mNotification.getDateTime().getHourOfDay(),
                mNotification.getDateTime().getMinuteOfHour(), 0);
        DateTime horaSaidaDesejada = new DateTime(horaChegadaDesejada.getMillis() - tempoViagem.getMillis());

        // get the headway_sec atr the time of the bus departuring
        int intervalo = 0;
        mFrequenciesFiltered.moveToFirst();
        for (int i = 0; i < mFrequenciesFiltered.getCount(); i++) {
            int hour = Integer.parseInt(mFrequenciesFiltered.getString(2)
                    .replaceAll("\"", "").split(":")[0]);
            if (hour == horaSaidaDesejada.getHourOfDay()){
                intervalo = 1000 * Integer.parseInt(mFrequenciesFiltered.getString(4)
                        .replaceAll("\"", ""));
                break;
            }
            mFrequenciesFiltered.moveToNext();
        }

        while((horaSaidaDesejada.getMillis() < horaChegadaDesejada.getMillis())){
            horaSaidaDesejada = new DateTime(horaSaidaDesejada.getMillis() + intervalo);
        }

        DateTime horaSaidaAnterior = new DateTime(horaSaidaDesejada.getMillis() - intervalo);
        DateTime horaSaidaPosterior = new DateTime(horaSaidaDesejada.getMillis());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        mDepartureTimeBeforeRB.setText(horaSaidaAnterior.toString(fmt));
        mDepartureTimeAfterRB.setText(horaSaidaPosterior.toString(fmt));
        mDepartureTimeAfterRB.setChecked(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int[] stringToTime(String time){
        int hour = Integer.parseInt(time.replaceAll("\"", "").split(":")[HOUR]);
        int minute = Integer.parseInt(time.replaceAll("\"", "").split(":")[MINUTE]);
        return new int[]{hour, minute};
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = group.findViewById(checkedId);
        String[] choosenTime = button.getText().toString().split(":");
        mDateTime = new DateTime(mNotification.getDateTime().getYear(),
                mNotification.getDateTime().getMonthOfYear(),
                mNotification.getDateTime().getDayOfMonth(),
                Integer.parseInt(choosenTime[HOUR]),
                Integer.parseInt(choosenTime[MINUTE]),0);
    }

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
                selectionArgs = new String[]{"%" + mTripId + "%"};

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
                selectionArgs = new String[]{"%" + mTripId + "%"};

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
        if (loader.getId() == ID_STOP_TIMES_LOADER) {
            mStopTimesFiltered = data;
            mSelectStopAdapter.swapCursor(data);
            mSelectStopAdapter.notifyDataSetChanged();
        } else {
            mFrequenciesFiltered = data;
        }
//        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//        mLinesRV.smoothScrollToPosition(mPosition);
        //if (data.getCount() != 0) showWeatherDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSelectStopAdapter.swapCursor(null);
    }
}
