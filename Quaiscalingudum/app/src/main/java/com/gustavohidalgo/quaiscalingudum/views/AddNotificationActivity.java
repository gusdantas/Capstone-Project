package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;

import java.util.ArrayList;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.*;

public class AddNotificationActivity extends AppCompatActivity
        implements OnEditNotificationListener {
    private BusNotification mBusNotification;
    private ArrayList<String> mNotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mBusNotification = intent.getParcelableExtra(OLD_NOTIFICATION);
            mNotificationList = intent.getStringArrayListExtra(NOTIFICATION_LIST);

            if (mBusNotification == null) {
                getSupportActionBar().setTitle("New notification");
                mBusNotification = new BusNotification();
                mBusNotification.setWeekly(NOT_WEEKLY);
            } else {
                getSupportActionBar().setTitle("Edit notification");
            }

            toEta(mBusNotification);
        }
    }

    @Override
    public void toEta(BusNotification busNotification) {
        EtaFragment etaFragment = EtaFragment.newInstance(busNotification);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, etaFragment, ETA).commit();
    }

    @Override
    public void toPickLine(BusNotification busNotification) {
        PickLineFragment pickLineFragment = PickLineFragment.newInstance(busNotification);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, pickLineFragment, PICK_LINE).commit();
    }

    @Override
    public void toDetails(BusNotification busNotification) {
        DetailsFragment detailsFragment = DetailsFragment.newInstance(busNotification);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, detailsFragment, DETAILS).commit();
    }

    @Override
    public void toSetNotifications(BusNotification busNotification) {
        SetNotificationsFragment setNotificationsFragment = SetNotificationsFragment
                .newInstance(busNotification, mNotificationList);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_add_notification, setNotificationsFragment, SET_NOTIFICATIONS)
                .commit();
    }

    @Override
    public void toFinishCreatingNotification(BusNotification busNotification) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTIFICATION, busNotification);
        intent.putExtra(NOTIFICATION, bundle);
        startActivity(intent);
    }
}
