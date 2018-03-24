package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;
import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.adapters.NotificationsAdapter;
import com.gustavohidalgo.quaiscalingudum.models.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.OLD_NOTIFICATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static FirebaseUser sUser;

    Notification mNotification;
    List<Notification> mNotificationList;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mMenuEventListener;
    NotificationsAdapter mNotificationsAdapter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.test_tv) TextView testTv;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addNotification();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            initializeFirebase();
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(NOTIFICATION);
            if (bundle != null) {
                mNotification = bundle.getParcelable(NOTIFICATION);
                writeNewNotification(sUser.getUid(), mNotification);
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
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                initializeFirebase();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
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
                    //mDatabase = FirebaseDatabase.getInstance().getReference();
                } else {
                    // First time sUser.

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder()
                                    .setScopes(Collections.singletonList(Scopes.PROFILE)).build());
                    //new AuthUI.IdpConfig.FacebookBuilder().build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    //.setTheme(R.style.LoginTheme)
                                    .build(),
                            RC_SIGN_IN);
                    goHome();
                }
            }
        };
    }

    private void goHome() {
        Toast.makeText(this, "goHome", Toast.LENGTH_SHORT).show();
        // Write a message to the database
        mNotificationList = new ArrayList<>();
        // mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
    }

    private void updateUserInformationUI() {
        Toast.makeText(this, "updateUserInformationUI", Toast.LENGTH_SHORT).show();
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
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("message");
//        // Read from the database
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                testTv.setText(value);
//                Log.d("gugu", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                String text = "Failed to read value.";
//                testTv.setText(text);
//                Log.w("gugu", "Failed to read value.", error.toException());
//            }
//        });
//
//        databaseReference.setValue("Novo teste!");
        Log.d("gugu", "Value is: ");
        mNotificationList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + sUser.getUid());
        mMenuEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                refreshAnnouncementsListValues(dataSnapshot.getValue(Notification.class), false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                refreshAnnouncementsListValues(dataSnapshot.getValue(Notification.class), true);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                refreshAnnouncementsListValues(dataSnapshot.getValue(Notification.class), true);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                refreshAnnouncementsListValues(dataSnapshot.getValue(Notification.class), true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException();
            }
        };
        mDatabaseReference.addChildEventListener(mMenuEventListener);

        mNotificationsAdapter = new NotificationsAdapter(this, mNotificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
        startActivity(intent);
    }

    private void writeNewNotification(String userId, Notification notification) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users/" + userId);
        mDatabaseReference.child(notification.getName()).setValue(notification);
    }

    private void refreshAnnouncementsListValues(Notification value, boolean needsClear) {
        if (needsClear) {
            mNotificationList.clear();
        }
        mNotificationList.add(value);
        mAnnouncementAdapter.notifyDataSetChanged();
    }
}
