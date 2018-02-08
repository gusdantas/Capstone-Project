package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.gustavohidalgo.quaiscalingudum.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static FirebaseUser sUser;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
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

        initializeFirebase();
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
}
