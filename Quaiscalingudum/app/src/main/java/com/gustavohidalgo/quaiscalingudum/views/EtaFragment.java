package com.gustavohidalgo.quaiscalingudum.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnSetDateListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

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
 * Use the {@link EtaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EtaFragment extends Fragment implements OnSetDateListener {

    private Notification mNotification;
    private DateTime mDateTime;
    private ArrayList<String> mWeeklyDays = new ArrayList<>();

    @BindView(R.id.time_tv)
    TextView mTimeTV;
    @BindView(R.id.pick_date_iv)
    ImageView mPickDateIV;
    @BindView(R.id.sun_cb)
    CheckBox mSundayCB;
    @BindView(R.id.mon_cb)
    CheckBox mMondayCB;
    @BindView(R.id.tue_cb)
    CheckBox mTuesdayCB;
    @BindView(R.id.wed_cb)
    CheckBox mWednesdayCB;
    @BindView(R.id.thu_cb)
    CheckBox mThursdayCB;
    @BindView(R.id.fri_cb)
    CheckBox mFridayCB;
    @BindView(R.id.sat_cb)
    CheckBox mSaturdayCB;
    @BindView(R.id.timePicker)
    TimePicker mTimePicker;
    @BindView(R.id.back_cancel_bt)
    Button mCancelBt;
    @BindView(R.id.next_pickline_bt)
    Button mPickLineBt;

    private OnEditNotificationListener mListener;

    public EtaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notification Parameter 1.
     * @return A new instance of fragment EtaFragment.
     */
    public static EtaFragment newInstance(Notification notification) {
        EtaFragment fragment = new EtaFragment();
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
        for(int i = 0; i < 7; i++){
            mWeeklyDays.add("");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eta, container, false);
        ButterKnife.bind(this, view);
        mDateTime = new DateTime();
        String today = "Today, " + mDateTime.toString(DateTimeFormat.fullDate()) + " only.";
        mTimeTV.setText(today);
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

    @OnClick(R.id.pick_date_iv)
    public void onPickDatePressed() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getChildFragmentManager(), "timePicker");
    }

    @OnClick({R.id.sun_cb, R.id.mon_cb, R.id.tue_cb, R.id.wed_cb, R.id.thu_cb, R.id.fri_cb,
            R.id.sat_cb})
    public void onDayChecked(CheckBox dayCheckBox) {
        if(dayCheckBox.isChecked()) {
            switch (dayCheckBox.getId()) {
                case R.id.sun_cb:
                    mNotification.setDayOfWeek(SUNDAY);
                    mWeeklyDays.set(0, "Sundays");
                    break;
                case R.id.mon_cb:
                    mNotification.setDayOfWeek(MONDAY);
                    mWeeklyDays.set(1, "Mondays");
                    break;
                case R.id.tue_cb:
                    mNotification.setDayOfWeek(TUESDAY);
                    mWeeklyDays.set(2, "Tuesdays");
                    break;
                case R.id.wed_cb:
                    mNotification.setDayOfWeek(WEDNESDAY);
                    mWeeklyDays.set(3, "Wednesdays");
                    break;
                case R.id.thu_cb:
                    mNotification.setDayOfWeek(THURSDAY);
                    mWeeklyDays.set(4, "Thursdays");
                    break;
                case R.id.fri_cb:
                    mNotification.setDayOfWeek(FRIDAY);
                    mWeeklyDays.set(5, "Fridays");
                    break;
                case R.id.sat_cb:
                    mNotification.setDayOfWeek(SATURDAY);
                    mWeeklyDays.set(6, "Saturdays");
                    break;
            }
        } else {
            switch (dayCheckBox.getId()) {
                case R.id.sun_cb:
                    mNotification.resetDayOfWeek(SUNDAY);
                    mWeeklyDays.set(0, "");
                    break;
                case R.id.mon_cb:
                    mNotification.resetDayOfWeek(MONDAY);
                    mWeeklyDays.set(1, "");
                    break;
                case R.id.tue_cb:
                    mNotification.resetDayOfWeek(TUESDAY);
                    mWeeklyDays.set(2, "");
                    break;
                case R.id.wed_cb:
                    mNotification.resetDayOfWeek(WEDNESDAY);
                    mWeeklyDays.set(3, "");
                    break;
                case R.id.thu_cb:
                    mNotification.resetDayOfWeek(THURSDAY);
                    mWeeklyDays.set(4, "");
                    break;
                case R.id.fri_cb:
                    mNotification.resetDayOfWeek(FRIDAY);
                    mWeeklyDays.set(5, "");
                    break;
                case R.id.sat_cb:
                    mNotification.resetDayOfWeek(SATURDAY);
                    mWeeklyDays.set(6, "");
                    break;
            }
        }

        if(mNotification.getDaysOfWeek() > 0){
            mNotification.setIsWeekly(true);
            StringBuilder stringBuilder = new StringBuilder();
            for (String day : mWeeklyDays) {
                if (!day.equals("")){
                    stringBuilder.append(day).append(", ");
                }
            }
            stringBuilder.append("weekly.");
            mTimeTV.setText(stringBuilder.toString());
        } else {
            mNotification.setIsWeekly(false);
            String time = mDateTime.toString(DateTimeFormat.fullDate()) + " only.";
            mTimeTV.setText(time);
        }
    }

    @Override
    public void setDate(int year, int month, int dayOfMonth) {
        mSundayCB.setChecked(false);
        mMondayCB.setChecked(false);
        mTuesdayCB.setChecked(false);
        mWednesdayCB.setChecked(false);
        mThursdayCB.setChecked(false);
        mFridayCB.setChecked(false);
        mSaturdayCB.setChecked(false);
        mNotification.setIsWeekly(false);
        mDateTime = new DateTime(year, month, dayOfMonth, mDateTime.getHourOfDay(),
                mDateTime.getMinuteOfHour());
        String time = mDateTime.toString(DateTimeFormat.fullDate()) + " only.";
        mTimeTV.setText(time);
    }

    @OnClick(R.id.next_pickline_bt)
    public void onNextPressed() {
        mDateTime = new DateTime(mDateTime.getYear(), mDateTime.getMonthOfYear(),
                mDateTime.getDayOfMonth(), mDateTime.getHourOfDay(), mDateTime.getMinuteOfHour());
        mNotification.setDateTime(mDateTime);
        if (mListener != null) {
            mListener.toPickLine(mNotification);
        }
    }

    @OnClick(R.id.back_cancel_bt)
    public void onBackPressed() {
        getActivity().finish();
    }
}
