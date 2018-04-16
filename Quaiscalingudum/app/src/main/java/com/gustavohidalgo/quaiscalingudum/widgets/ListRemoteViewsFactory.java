package com.gustavohidalgo.quaiscalingudum.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;

import java.util.ArrayList;

/**
 * Created by Gustavo on 15/04/2018.
 */

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private ArrayList<BusNotification> mBusNotificationList = new ArrayList<>();
    private ArrayList<String> mNotificationNameList = new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mMenuEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static FirebaseUser sUser;
    private int appWidgetId;


    public ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                sUser = firebaseAuth.getCurrentUser();
                if (sUser != null){
                    mBusNotificationList = new ArrayList<>();
                    mNotificationNameList = new ArrayList<>();
                    populateListItem();
                }
            }
        };
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);


    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mBusNotificationList.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        BusNotification busNotification = mBusNotificationList.get(position);
        String row = busNotification.getName() + " - " + String.valueOf(busNotification.getActive());
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_row);
        views.setTextViewText(R.id.widget_row, row);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the ListView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void populateListItem() {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
        mMenuEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                mBusNotificationList.add(value);
                mNotificationNameList.add(value.getName());
                BusNotificationEnablingService.startActionUpdateNotificationWidgets(mContext);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                int index = mNotificationNameList.indexOf(value.getName());
                mBusNotificationList.set(index, value);
                BusNotificationEnablingService.startActionUpdateNotificationWidgets(mContext);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                int index = mNotificationNameList.indexOf(value.getName());
                if (index != -1) {
                    mNotificationNameList.remove(index);
                    NotificationUtils.deleteJob(mContext, mBusNotificationList.get(index));
                    mBusNotificationList.remove(index);
                    BusNotificationEnablingService.startActionUpdateNotificationWidgets(mContext);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException();
            }
        };
        mDatabaseReference.addChildEventListener(mMenuEventListener);
    }
}
