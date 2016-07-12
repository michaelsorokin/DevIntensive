package devintensive.softdesign.com.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener  {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Auth Activity";

    private Button mSignIn;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        mSignIn = (Button) findViewById(R.id.auth_enter_button);
        mRememberPassword = (TextView) findViewById(R.id.forgot_pass_button);
        mLogin = (EditText) findViewById(R.id.auth_login_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        mDataManager = DataManager.getInstance();

        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

        checkTheWayToStart();
    }

    private void checkTheWayToStart() {

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<AuthModelRes> resp = mDataManager.checkUser(mDataManager.getPreferencesManager().getUserId());

            resp.enqueue(new Callback<AuthModelRes>() {
                @Override
                public void onResponse(Call<AuthModelRes> call, Response<AuthModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess();
                    }
                }

                @Override
                public void onFailure(Call<AuthModelRes> call, Throwable t) {

                }
            });
        } else {
            showSnackbar("Ошибка связи с сервером");
        }
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {
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
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void rememberPassword() {

        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void signIn() {
        if(NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {

                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль.");
                    } else {
                        showSnackbar("Какая то другая ошибка.");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // Обработать ошибки ретрофита
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
