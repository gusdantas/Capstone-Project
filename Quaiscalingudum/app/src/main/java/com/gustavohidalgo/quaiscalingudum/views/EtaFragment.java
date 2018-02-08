package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.enums.DaysOfWeek;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gustavohidalgo.quaiscalingudum.enums.DaysOfWeek.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link EtaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EtaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NOTIFICATION = "notification";

    // TODO: Rename and change types of parameters
    private Notification mNotification;
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
    CheckBox mSaturdaydayCB;
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
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eta, container, false);
        ButterKnife.bind(this, view);
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
    }

    @OnClick({R.id.sun_cb, R.id.mon_cb, R.id.tue_cb, R.id.wed_cb, R.id.thu_cb, R.id.fri_cb,
            R.id.sat_cb})
    public void onDayChecked(CheckBox dayCheckBox) {
        switch(dayCheckBox.getId()){
            case R.id.sun_cb:
                mNotification.setDaysOfWeek(0b1000000);
                break;
            case R.id.mon_cb:
                mNotification.setDaysOfWeek(0b0100000);
                break;
            case R.id.tue_cb:
                mNotification.setDaysOfWeek(0b0010000);
                break;
            case R.id.wed_cb:
                mNotification.setDaysOfWeek(0b0001000);
                break;
            case R.id.thu_cb:
                mNotification.setDaysOfWeek(0b0000100);
                break;
            case R.id.fri_cb:
                mNotification.setDaysOfWeek(0b0000010);
                break;
            case R.id.sat_cb:
                mNotification.setDaysOfWeek(0b0000001);
                break;
        }
//        if(dayCheckBox.isChecked()){
//
//            Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
//        } else {
//            switch(dayCheckBox.getId()){
//                case R.id.sun_cb:
//                    break;
//                case R.id.mon_cb:
//                    break;
//                case R.id.tue_cb:
//                    break;
//                case R.id.wed_cb:
//                    break;
//                case R.id.thu_cb:
//                    break;
//                case R.id.fri_cb:
//                    break;
//                case R.id.sat_cb:
//                    break;
//            }
//            Toast.makeText(getContext(), "uncheck", Toast.LENGTH_SHORT).show();
//        }
    }

    @OnClick(R.id.next_pickline_bt)
    public void onNextPressed() {
        if (mListener != null) {
            mListener.toPickLine(mNotification);
        }
    }

    @OnClick(R.id.back_cancel_bt)
    public void onBackPressed() {
        getActivity().finish();
    }

}
