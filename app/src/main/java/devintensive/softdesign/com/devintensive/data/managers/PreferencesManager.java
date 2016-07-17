package devintensive.softdesign.com.devintensive.data.managers;


import android.content.SharedPreferences;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import devintensive.softdesign.com.devintensive.utils.ConstantManager;
import devintensive.softdesign.com.devintensive.utils.DevintensiveApplication;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_BIO_KEY
    };

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RAITING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE,
    };

    private static final String[] USER_INFO = {
            ConstantManager.USER_FIRST_NAME,
            ConstantManager.USER_SECOND_NAME
    };

    public PreferencesManager() {
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for(int i=0; i<USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {

        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "null"));

        return userFields;
    }

    public List<String> loadUserProfileValues() {

        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RAITING_VALUE, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE, "0"));
        return userValues;
    }

    public void saveUserProfileValues (int[] userValues) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for(int i=0; i<USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public List<String> loadUserProfileInfo() {

        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME, ""));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME, ""));
        return userValues;
    }

    public void saveUserInfo(List<String> userInfo) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for(int i=0; i<USER_INFO.length; i++) {
            editor.putString(USER_INFO[i], userInfo.get(i));
        }
        editor.apply();
    }

    public void saveUserPhoto (Uri photoUri){

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, photoUri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto () {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.devintensive/drawable/user_photo"));
    }

    public void saveUserAvatar (Uri avatarUri){

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, avatarUri.toString());
        editor.apply();
    }

    public Uri loadAvatar () {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resource://com.devintensive/drawable/avatarUri"));
    }

    public void saveAuthToken (String authToken) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken.toString());
        editor.apply();
    }

    public String getAuthToken () {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId (String authToken) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, authToken.toString());
        editor.apply();
    }

    public String getUserId () {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public boolean isSaveMeEnabled() {
        return mSharedPreferences.getBoolean(ConstantManager.USER_SAVEME_KEY, false);
    }

    public void saveMeEnabled(boolean isEnabled) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ConstantManager.USER_SAVEME_KEY, isEnabled);
        editor.apply();
    }

    public String loadUserLogin () {
        return mSharedPreferences.getString(ConstantManager.USER_LOGIN_KEY, "");
    }

    public void saveUserLogin (String userLogin) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_LOGIN_KEY, userLogin);
        editor.apply();
    }

    public String loadUserPassword () {
        return mSharedPreferences.getString(ConstantManager.USER_PASSWORD_KEY, "");
    }

    public void saveUserPassword (String userPassword) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PASSWORD_KEY, userPassword);
        editor.apply();
    }
}
