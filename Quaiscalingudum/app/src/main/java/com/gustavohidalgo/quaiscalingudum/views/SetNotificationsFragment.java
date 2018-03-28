package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION_LIST;
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
    @BindView(R.id.notification_active_sw)
    Switch mNotificationActiveSW;
    @BindView(R.id.notification_name_et)
    EditText mNotificationName;
    @BindView(R.id.notifications_added_rv)
    RecyclerView mNotificationsRV;

    private ArrayList<String> mNotificationList;
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
    public static SetNotificationsFragment newInstance(Notification notification,
                                                       ArrayList<String> notificationList) {
        SetNotificationsFragment fragment = new SetNotificationsFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        args.putStringArrayList(NOTIFICATION_LIST, notificationList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mNotificationList = getArguments().getStringArrayList(NOTIFICATION_LIST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_notifications, container, false);
        ButterKnife.bind(this, view);
        DateTime dateTime = NotificationUtils.getDateTime(mNotification);
        mLineNameTV.setText(mNotification.getTrip().getTripHeadsign());
        mLineCodeTV.setText(mNotification.getTrip().getRouteId());
        String time = dateTime.getHourOfDay() + ":" +
                dateTime.getMinuteOfHour();
        mArrivalTime.setText(time);
        return view;
    }

    @OnClick(R.id.next_finish_bt)
    public void onNextPressed() {
        if (mListener != null) {
            if (mNotificationList.contains(mNotificationName.getText().toString())){
                Toast.makeText(getContext(), "this name already exists", Toast.LENGTH_SHORT).show();
            } else {
                mNotification.setName(mNotificationName.getText().toString());
                mNotification.setIsActive(mNotificationActiveSW.isChecked());
                mListener.toFinishCreatingNotification(mNotification);
            }
        }
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
