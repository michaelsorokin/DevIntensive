package devintensive.softdesign.com.devintensive.ui.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;
import devintensive.softdesign.com.devintensive.utils.RoundedAvatarDrawable;
import devintensive.softdesign.com.devintensive.utils.RoundedAvatarProvider;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = ConstantManager.VIEW_MODE;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private ImageView mAvatar;

    private DataManager mDataManager;

    private EditText mUserPhone;
    private EditText mUserMail;
    private EditText mUserVk;
    private EditText mUserGit;
    private EditText mUserBio;

    private List<EditText> mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mDataManager = DataManager.getInstance();

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.mail_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.git_et);
        mUserBio = (EditText) findViewById(R.id.bio_et);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserVk);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserBio);

        mFab.setOnClickListener(this);

        setupToolbar();
        setupDrawer();

        if(savedInstanceState == null) {
            mCurrentEditMode = ConstantManager.VIEW_MODE;
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, ConstantManager.VIEW_MODE);
        }

        changeEditMode(mCurrentEditMode);
        loadUserInfoValue();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK &&
                mNavigationDrawer.isDrawerOpen(GravityCompat.START) &&
                event.getRepeatCount() == 0) {

            mNavigationDrawer.closeDrawer(GravityCompat.START);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.fab:

                if(mCurrentEditMode == ConstantManager.VIEW_MODE) {

                    changeEditMode(ConstantManager.EDIT_MODE);
                    mCurrentEditMode = ConstantManager.EDIT_MODE;
                } else {

                    changeEditMode(ConstantManager.VIEW_MODE);
                    mCurrentEditMode = ConstantManager.VIEW_MODE;
                }
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    private void showSnackbar (String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        ImageView navImgView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar_id);
        Bitmap bitmap = (Bitmap) ((BitmapDrawable) getResources().getDrawable(R.drawable.avatar)).getBitmap();
        /* TODO сделать третьим способом
                RoundedAvatarDrawable roundedAvatarDrawable = new RoundedAvatarDrawable(bitmap);
                navImgView.setImageBitmap(roundedAvatarDrawable.getBitmap());
        */
        navImgView.setImageBitmap(RoundedAvatarProvider.getRoundedBitmap(bitmap));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode(int mode) {

        if(mode == ConstantManager.EDIT_MODE) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for(EditText userEntry: mUserInfo) {
                userEntry.setEnabled(true);
                userEntry.setFocusable(true);
                userEntry.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for(EditText userEntry: mUserInfo) {
                userEntry.setEnabled(false);
                userEntry.setFocusable(false);
                userEntry.setFocusableInTouchMode(false);

            }
            saveUserInfoValue();
        }
    }

    private void loadUserInfoValue() {

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int  i = 0;  i < userData.size();  i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }


}
