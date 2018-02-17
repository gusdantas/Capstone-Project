package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String NOTIFICATION = "notification";
    private static final String STOPS = "stops";

    private Notification mNotification;
    private String[] mLine;
    private ArrayList<String> mStops, mStopsFiltered;

    @BindView(R.id.line_code_tv)
    TextView mLineCodeTV;
    @BindView(R.id.line_name_tv)
    TextView mLineNameTV;
    @BindView(R.id.destination_rg)
    RadioGroup mDestinationRG;
    @BindView(R.id.destination_before_rb)
    RadioButton mDestinationBeforeRB;
    @BindView(R.id.destination_after_rb)
    RadioButton mDestinationAfterRB;
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
    public static DetailsFragment newInstance(Notification notification, ArrayList<String> stops) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        args.putStringArrayList(STOPS, stops);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mStops = getArguments().getStringArrayList(STOPS);
            mLine = mNotification.getLine();
            stopsFilter(mLine[2]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        mLineCodeTV.setText(mLine[0].replaceAll("\"", ""));
        mLineNameTV.setText(mLine[3].replaceAll("\"", ""));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, mStopsFiltered);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mDepartureStopSP.setOnItemSelectedListener(this);
        mDepartureStopSP.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.next_notifications_bt)
    public void onNextPressed() {
        if (mListener != null) {
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

    public void stopsFilter(String filter) {
        mStopsFiltered.clear();
        if(filter.isEmpty()){
            mStopsFiltered.addAll(mStops);
        } else {
            filter = filter.toLowerCase();
            for(String item : mStops){
                String[] stop = item.split(",");
                if(stop[0].toLowerCase().replaceAll("\"", "").equals(filter)){
                    mStopsFiltered.add(item);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mNotification.setStop(mStopsFiltered.get(position).split(","));
        int[] initialTime = stringToTime(mStopsFiltered.get(0).split(","));
        DateTime initialStop = new DateTime(0, 0, 0, initialTime[0],
                initialTime[1], 0);
        int[] actualTime = stringToTime(mNotification.getStop());
        DateTime actualStop = new DateTime(0, 0, 0, actualTime[0],
                actualTime[1], 0);
        DateTime diffStop = new DateTime(actualStop.getMillis() - initialStop.getMillis());
        DateTime stored = new DateTime(0,0,0, mNotification.getDateTime().getHourOfDay(),
                mNotification.getDateTime().getMinuteOfHour(), 0);
        DateTime diffReal = new DateTime(stored.getMillis() - diffStop.getMillis());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int[] stringToTime(String[] strings){
        int hour = Integer
                .getInteger(strings[1].replaceAll("\"", "").split(":")[0]);
        int minute = Integer
                .getInteger(strings[1].replaceAll("\"", "").split(":")[1]);
        return new int[]{hour, minute};
    }
}
