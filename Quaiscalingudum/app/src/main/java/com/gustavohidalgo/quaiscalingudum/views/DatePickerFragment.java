package com.gustavohidalgo.quaiscalingudum.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnSetDateListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import java.util.Calendar;

/**
 * Created by gustavo.hidalgo on 18/02/16.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static OnSetDateListener mOnSetDateListener;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(OnSetDateListener onSetDateListener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setOnSetDateListener(onSetDateListener);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mOnSetDateListener.setDate(year, month, dayOfMonth);
    }

    public void setOnSetDateListener(OnSetDateListener onSetDateListener){
        mOnSetDateListener = onSetDateListener;
    }
}
