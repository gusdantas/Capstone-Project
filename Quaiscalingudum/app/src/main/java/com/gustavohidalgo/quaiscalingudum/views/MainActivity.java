package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.adapters.NotificationsAdapter;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnRecyclerViewClickListener;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.ALARM_TIME;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.IS_ACTIVE;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION_LIST;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOT_ACTIVE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static FirebaseUser sUser;

    BusNotification mBusNotification;
    List<BusNotification> mBusNotificationList;
    ArrayList<String> mNotificationNameList;
    DatabaseReference mDatabaseReference;
    private ChildEventListener mNotificationEventListener;
    NotificationsAdapter mNotificationsAdapter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.notification_rv) RecyclerView mNotificationRV;
    ImageView mProfilePictureIv;
    TextView mProfileEmailTv;
    TextView mProfileNameTv;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setupDrawerHeader();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotification();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        initializeFirebase();

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(NOTIFICATION);
            if (bundle != null) {
                mBusNotification = bundle.getParcelable(NOTIFICATION);

                if (mBusNotification.getActive() == 1){
                    int secondsToAlarm = NotificationUtils
                            .secondsToAlarm(mBusNotification).get(ALARM_TIME);
                    NotificationUtils.scheduleJob(this, mBusNotification, secondsToAlarm);
                }
                writeNewNotification(mBusNotification);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                initializeFirebase();
            } else {
                // Sign in failed, check response for error code
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_add:
                addNotification();
                break;
            case R.id.nav_sign_out:
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {// ...

                    }
                });
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                sUser = firebaseAuth.getCurrentUser();
                if (sUser != null) {
                    // Already exists and it's logged.
                    updateUserInformationUI();
                } else {
                    // First time sUser.
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder()
                                    .setScopes(Collections.singletonList(Scopes.PROFILE)).build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void updateUserInformationUI() {
        if (sUser.getPhotoUrl() != null) {
            Picasso.with(this).load(sUser.getPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher_round).into(mProfilePictureIv);
        }
        if (sUser.getDisplayName() != null) {
            mProfileNameTv.setText(sUser.getDisplayName());
        }
        if (sUser.getEmail() != null) {
            mProfileEmailTv.setText(sUser.getEmail());
        }

        mBusNotificationList = new ArrayList<>();
        mNotificationNameList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
//        mDatabaseReference = database.getReference("users/" + sUser.getUid());
//        mDatabaseReference.keepSynced(true);
        mNotificationEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                mBusNotificationList.add(value);
                mNotificationNameList.add(value.getName());
                mNotificationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                int index = mNotificationNameList.indexOf(value.getName());
                mBusNotificationList.set(index, value);
                mNotificationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                BusNotification value = dataSnapshot.getValue(BusNotification.class);
                int index = mNotificationNameList.indexOf(value.getName());
                if (index != -1) {
                    mNotificationNameList.remove(index);
                    NotificationUtils.deleteJob(getApplicationContext(),
                            mBusNotificationList.get(index));
                    mBusNotificationList.remove(index);
                    mNotificationsAdapter.notifyDataSetChanged();
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
        mDatabaseReference.addChildEventListener(mNotificationEventListener);

        mNotificationsAdapter = new NotificationsAdapter(mBusNotificationList,
                new OnRecyclerViewClickListener() {
            @Override
            public void onTurnOn(int position) {
                int actual = mBusNotificationList.get(position).getActive();
                if (actual == NOT_ACTIVE) {
                    actual = IS_ACTIVE;
                    mBusNotificationList.get(position).setActive(actual);
                    int secondsToAlarm = NotificationUtils
                            .secondsToAlarm(mBusNotificationList.get(position)).get(ALARM_TIME);
                    NotificationUtils.scheduleJob(getApplicationContext(),
                            mBusNotificationList.get(position), secondsToAlarm);
                    writeNewNotification(mBusNotificationList.get(position));
                }
            }

            @Override
            public void onTurnOff(int position) {
                int actual = mBusNotificationList.get(position).getActive();
                if (actual == IS_ACTIVE) {
                    actual = NOT_ACTIVE;
                    NotificationUtils.deleteJob(getApplicationContext(),
                            mBusNotificationList.get(position));
                    mBusNotificationList.get(position).setActive(actual);
                    writeNewNotification(mBusNotificationList.get(position));
                }
            }

            @Override
            public void onDeleteClicked(int position) {
                deleteNotification(mBusNotificationList.get(position));
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mNotificationRV.setLayoutManager(mLayoutManager);
        mNotificationRV.setAdapter(mNotificationsAdapter);
    }

    private void setupDrawerHeader() {
        /* Workaround due to navigation mDrawer issue. =(
        * https://github.com/JakeWharton/butterknife/issues/406
        * https://code.google.com/p/android/issues/detail?id=190226
        * */
        View headerLayout = mNavigationView.getHeaderView(0);
        mProfileNameTv = ButterKnife.findById(headerLayout, R.id.userNameTextView);
        mProfileEmailTv = ButterKnife.findById(headerLayout, R.id.userEmailTextView);
        mProfilePictureIv = ButterKnife.findById(headerLayout, R.id.userImageView);
    }

    private void addNotification(){
        Intent intent = new Intent(this, AddNotificationActivity.class);
        intent.putStringArrayListExtra(NOTIFICATION_LIST, mNotificationNameList);
        startActivity(intent);
    }

    private void writeNewNotification(BusNotification busNotification) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
        mDatabaseReference.child(busNotification.getName()).setValue(busNotification);
    }

    private void deleteNotification(BusNotification busNotification) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
        mDatabaseReference.child(busNotification.getName()).removeValue();
    }

    private void detachDatabaseReadListener() {
        if (mNotificationEventListener != null) {
            mDatabaseReference.removeEventListener(mNotificationEventListener);
            mNotificationEventListener = null;
        }
    }
}
