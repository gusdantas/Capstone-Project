package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;
import com.gustavohidalgo.quaiscalingudum.utils.FileHelper;

import java.util.ArrayList;

public class AddNotificationActivity extends AppCompatActivity
        implements OnEditNotificationListener {
    private Notification mNotification;
    private static ArrayList<String> sTrips, sStopTimes, sFrequencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mNotification = intent.getParcelableExtra("old_notification");
            if (sTrips == null){
                sTrips = FileHelper.getLines(R.raw.trips, this);
            }
            if (sStopTimes == null){
                sStopTimes = FileHelper.getLines(R.raw.stop_times, this);
            }
            if (sFrequencies == null){
                sFrequencies = FileHelper.getLines(R.raw.frequencies, this);
            }

            if (mNotification == null) {
                getSupportActionBar().setTitle("New notification");
                mNotification = new Notification();
                mNotification.setIsWeekly(false);
            } else {
                getSupportActionBar().setTitle("Edit notification");
            }

            toEta(mNotification);
        }
    }

    @Override
    public void toEta(Notification notification) {
        EtaFragment etaFragment = EtaFragment.newInstance(notification);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, etaFragment, "eta").commit();
    }

    @Override
    public void toPickLine(Notification notification) {
        PickLineFragment pickLineFragment = PickLineFragment.newInstance(notification, sTrips);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, pickLineFragment, "pickline").commit();
    }

    @Override
    public void toDetails(Notification notification) {
        DetailsFragment detailsFragment = DetailsFragment
                .newInstance(notification, sStopTimes, sFrequencies);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, detailsFragment, "details").commit();
    }

    @Override
    public void toSetNotifications(Notification notification) {
        SetNotificationsFragment setNotificationsFragment = SetNotificationsFragment
                .newInstance(notification);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, setNotificationsFragment, "notifications")
                .commit();
    }
}
