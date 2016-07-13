package devintensive.softdesign.com.devintensive.ui.activities;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vicmikhailau.maskededittext.MaskedWatcher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.data.network.req.UploadFileReq;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;
import devintensive.softdesign.com.devintensive.utils.RoundedAvatarProvider;
import devintensive.softdesign.com.devintensive.utils.UserInfoWatcher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private int mCurrentEditMode = ConstantManager.VIEW_MODE;

    @BindView(R.id.main_coordinator_container)  CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.profile_placeholder)         RelativeLayout mProfileHolder;
    @BindView(R.id.toolbar)                     Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)           DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)                         FloatingActionButton mFab;
    @BindView(R.id.collapsing_toolbar)          CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout)               AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_image)            ImageView mProfileImage;

    @BindViews({ R.id.phone_et, R.id.mail_et, R.id.vk_et, R.id.git_et, R.id.bio_et })
    List<EditText> mUserInfo;

    @BindViews({ R.id.makeCall, R.id.sendEmail, R.id.vk, R.id.gitHub })
    List<ImageView> mUserAction;

    @BindViews({ R.id.phone_layout, R.id.mail_layout, R.id.vk_layout, R.id.github_layout })
    List<TextInputLayout> mUserInfoLayouts;

    @BindViews({ R.id.raiting_tv, R.id.code_lines_tv, R.id.project_tv})
    List<TextView> mUserValuesViews;

    private AppBarLayout.LayoutParams mAppBarParams;
    private DataManager mDataManager;

    private File mPhotoFile;
    private Uri mSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        subscribeOnListeners();
        setupToolbar();
        setupDrawer();
        initUserInfoValue();
        initUserFields();
        initUserInfo();

        mUserInfo.get(ConstantManager.MAKE_CALL).addTextChangedListener(new MaskedWatcher("*#(###)###-##-##"));

        for (int i = 1; i < mUserInfo.size()-1; i++) {
            mUserInfo.get(i).addTextChangedListener(new UserInfoWatcher(getBaseContext(),
                    mUserInfo.get(i),
                    mUserAction.get(i),
                    mUserInfoLayouts.get(i)));
        }

        if(savedInstanceState == null) {
            mCurrentEditMode = ConstantManager.VIEW_MODE;
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, ConstantManager.VIEW_MODE);
        }

        changeEditMode(mCurrentEditMode);

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo) //TODO нужно сделать сюда плейсхолдер + трансформ + кроп.
                .into(mProfileImage);
    }

    private void subscribeOnListeners() {
        mFab.setOnClickListener(this);
        mProfileHolder.setOnClickListener(this);

        for (int i = 0; i < mUserAction.size(); i++) {
            mUserAction.get(i).setOnClickListener(this);
        }
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
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.makeCall:
                String telephone = mUserInfo.get(ConstantManager.MAKE_CALL).getText().toString(); //mUserPhone.getText().toString();
                if(!telephone.isEmpty()) {
                    Intent makeCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", telephone, null));
                    startActivity(makeCall);
                }
                break;
            case R.id.sendEmail:
                String email = mUserInfo.get(ConstantManager.SEND_EMAIL).getText().toString(); //mUserMail.getText().toString();
                if(!email.isEmpty()) {
                    Intent sendEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                    sendEmail.setType("plain/text");
                    sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email});
                    startActivity(Intent.createChooser(sendEmail, "Выберите email клиент: "));
                }
                break;
            case R.id.vk:
                String vk = mUserInfo.get(ConstantManager.OPEN_VK).getText().toString(); //mUserVk.getText().toString();
                if(!vk.isEmpty()) {
                    Intent openVK = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + vk));
                    startActivity(openVK);
                }
                break;
            case R.id.gitHub:
                String gitHub = mUserInfo.get(ConstantManager.OPEN_GITHUB).getText().toString(); //mUserGit.getText().toString();
                if(!gitHub.isEmpty()) {
                    Intent openGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + gitHub));
                    startActivity(openGitHub);
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {

            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if(resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    if(!mDataManager.getPreferencesManager().loadUserPhoto().equals(mSelectedImage)) {

                        String[] proj = { MediaStore.Images.Media.DATA };
                        Cursor cursor = this.getContentResolver().query(data.getData(),  proj, null, null, null);
                        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(index);
                        cursor.close();

                        uploadPhotoToServer(Uri.parse(path));
                    }
                    insertProfileImage(mSelectedImage);
                }
                break;

            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    if(!mDataManager.getPreferencesManager().loadUserPhoto().equals(mSelectedImage)) {
                        uploadPhotoToServer(mSelectedImage);
                    }
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_profile));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItem) {

                        switch (selectedItem){
                            case ConstantManager.LOAD_FROM_GALLERY:
                                loadPhotoFromGalery();
                                break;
                            case ConstantManager.LOAD_FROM_CAMERA:
                                loadPhotoFromCamera();
                                break;
                            default:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    private void uploadPhotoToServer(Uri imageUri){
        Call<ResponseBody> call = mDataManager.uploadPhoto(new UploadFileReq().photo(imageUri));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showSnackbar("Успешно обновили");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackbar("Что то пошло не так!");
            }
        });
    }

    private void showSnackbar (String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);

        ImageView navImgView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar_id);

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
            mUserInfo.get(ConstantManager.MAKE_CALL).requestFocus();
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for(EditText userEntry: mUserInfo) {
                userEntry.setEnabled(false);
                userEntry.setFocusable(false);
                userEntry.setFocusableInTouchMode(false);
            }
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            saveUserFields();
        }
    }

    private void initUserFields() {

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int  i = 0;  i < userData.size();  i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }


    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValuesViews.get(i).setText(userData.get(i));
        }
    }

    private void initUserInfo() {

        List<String> userInfo = mDataManager.getPreferencesManager().loadUserProfileInfo();
        String personInfo = userInfo.get(0) + " " + userInfo.get(0);
    }

    private void loadPhotoFromGalery () {

        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)),
                ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera () {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                 }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы приложения нужно добавить разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO нам дали разрешение на работу с камерой, надо это показать пользователю и возможно как то обработать.
            }

            if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //TODO нам дали разрешение на запись на внешнее устройство, надо это показать пользователю и возможно как то обработать.
            }
        }
    }

    private void hideProfilePlaceholder () {
        mProfileHolder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder () {
        mProfileHolder.setVisibility(View.VISIBLE);
    }

    private void insertProfileImage(Uri selectedImage) {

        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.user_photo) //TODO нужно сделать сюда плейсхолдер + трансформ + кроп.
                .into(mProfileImage);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    private void lockToolbar () {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar () {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void openApplicationSettings () {

        Intent appSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettings, ConstantManager.PERMISSION_REQUEST_SETTING_CODE);
    }
}
