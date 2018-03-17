package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
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
 * Use the {@link SetNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetNotificationsFragment extends Fragment {

    @BindView(R.id.line_code_details_tv)
    TextView mLineCodeTV;
    @BindView(R.id.line_name_details_tv)
    TextView mLineNameTV;
    @BindView(R.id.line_time_details_tv)
    TextClock mArrivalTime;
    @BindView(R.id.notification_name_et)
    EditText mNotificationName;
    @BindView(R.id.notifications_added_rv)
    RecyclerView mNotificationsRV;

    private Notification mNotification;

    private OnEditNotificationListener mListener;

    public SetNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notification Parameter 1.
     * @return A new instance of fragment SetNotificationsFragment.
     */
    public static SetNotificationsFragment newInstance(Notification notification) {
        SetNotificationsFragment fragment = new SetNotificationsFragment();
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_notifications, container, false);
        ButterKnife.bind(this, view);
        mLineNameTV.setText(mNotification.getLine()[TRIPS_TRIP_HEADSIGN]);
        mLineCodeTV.setText(mNotification.getLine()[TRIPS_ROUTE_ID]);
        String time = mNotification.getDateTime().getHourOfDay() + ":" +
                mNotification.getDateTime().getMinuteOfHour();
        mArrivalTime.setText(time);
        return view;
    }

    @OnClick(R.id.next_finish_bt)
    public void onNextPressed() {
        getActivity().finish();

    }

    @OnClick(R.id.back_details_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toDetails(mNotification);
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
}
