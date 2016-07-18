package devintensive.softdesign.com.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.data.network.req.UserLoginReq;
import devintensive.softdesign.com.devintensive.data.network.res.AuthModelRes;
import devintensive.softdesign.com.devintensive.data.network.res.UserModelRes;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;
import devintensive.softdesign.com.devintensive.utils.NetworkStatusChecker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends BaseActivity {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Auth Activity";

    @BindView(R.id.auth_enter_button)   Button mSignIn;
    @BindView(R.id.forgot_pass_button)  TextView mRememberPassword;
    @BindView(R.id.auth_login_editText) EditText mLogin;
    @BindView(R.id.password_editText)   EditText mPassword;
    @BindView(R.id.coordinator_layout)  CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.login_checkbox)      CheckBox mSaveMeCheckBox;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        defineStartingRoute();
    }

    private void defineStartingRoute() {

        if(mDataManager.getPreferencesManager().isSaveMeEnabled()){

            String login = mDataManager.getPreferencesManager().loadUserLogin();
            String password = mDataManager.getPreferencesManager().loadUserPassword();
            /*если мы ранее сохраняли пароль и логин и они непустые, то выставялем галку - запомнить меня,
            * достаем логин и пароль из настроек и добавляем их в форму */
            if(!login.isEmpty() && !password.isEmpty()) {

                mSaveMeCheckBox.setChecked(true);
                mLogin.setText(login);
                mPassword.setText(password);
            }
        }

        if(NetworkStatusChecker.isNetworkAvailable(this) &&
                !mDataManager.getPreferencesManager().getUserId().isEmpty() &&
                !mDataManager.getPreferencesManager().getAuthToken().isEmpty()) {
            loginSuccess();
        }

    }

    @OnClick({R.id.auth_enter_button, R.id.forgot_pass_button})
    void submitAuthButton(View v) {

        switch (v.getId()) {
            case R.id.auth_enter_button:
                signIn();
                break;
            case R.id.forgot_pass_button:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loginSuccess(UserModelRes userModel) {

        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserProfileInfo(userModel);
        saveUserPhoto(userModel);
        saveUserAvatar(userModel);
        loginSuccess();
    }

    private void loginSuccess() {
        Intent loginIntent = new Intent(this, UserListActivity.class);
        startActivity(loginIntent);
    }

    private void rememberPassword() {

        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void signIn() {

        if(mSaveMeCheckBox.isChecked()) {
            mDataManager.getPreferencesManager().saveMeEnabled(true);
            /*TODO нужно переделать сохраниение в зашифрованную базу, что бы не хранить пароль в настройках*/
            mDataManager.getPreferencesManager().saveUserLogin(mLogin.getText().toString());
            mDataManager.getPreferencesManager().saveUserPassword(mPassword.getText().toString());
        }

        showProgress();

        if(NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {

                    if(response.isSuccessful()) {
                        loginSuccess(response.body());
                    } else {
                        hideProgress();
                        if (response.code() == ConstantManager.RESPONSE_ACC_DENIED) {
                            showSnackbar(getString(R.string.wrong_login_password_error));
                        } else if (response.code() == ConstantManager.RESPONSE_FORBIDDEN) {
                            showSnackbar(getString(R.string.not_enought_privileges_error));
                        } else {
                            showSnackbar(getString(R.string.unknown_network_error));
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    hideProgress();
                    if (!NetworkStatusChecker.isNetworkAvailable(getApplicationContext())) {
                        showSnackbar(getString(R.string.no_network_connection_error));
                    } else {
                        showSnackbar(String.format("%s: %s", getString(R.string.user_auth_error), t.getMessage()));
                    }
                }
            });
        } else {
            showSnackbar("Сеть не доступна, попробуйте позже!");
        }
    }

    private void saveUserValues(UserModelRes userModel) {

        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    private void saveUserProfileInfo(UserModelRes userModel) {

        List<String> userInfo = new ArrayList<>();
        userInfo.add(userModel.getData().getUser().getFirstName());
        userInfo.add(userModel.getData().getUser().getSecondName());
        mDataManager.getPreferencesManager().saveUserInfo(userInfo);

        List<String> userData = new ArrayList<>();
        userData.add(userModel.getData().getUser().getContacts().getPhone());
        userData.add(userModel.getData().getUser().getContacts().getEmail());
        userData.add(userModel.getData().getUser().getContacts().getVk());
        userData.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userData.add(userModel.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void saveUserPhoto(UserModelRes userModel) {
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto()));
    }

    private void saveUserAvatar(UserModelRes userModel) {
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar()));
    }
}
