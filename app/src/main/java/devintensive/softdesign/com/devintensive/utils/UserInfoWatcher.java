package devintensive.softdesign.com.devintensive.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import devintensive.softdesign.com.devintensive.R;

public class UserInfoWatcher implements TextWatcher {

    private final String mailAdressPattern = "^[\\w-]{3,}\\@[\\w-]{2,}\\.[a-z]{2,3}$";
    private final String vkAdressPattern = "^vk.com/[\\w-]{3,}$";
    private final String gitAdressPattern = "^github.com/[\\w-]{3,}$";

    private Context mContext;
    private TextInputLayout mTextInputLayout;
    private EditText mUserInfo;
    private int mResId;
    private String mError;
    private ImageView mActionBtn;

    private Pattern mPattern;
    private Matcher mMatcher;


    public UserInfoWatcher (Context context, EditText userInfo, ImageView button, TextInputLayout textInputLayout) {

        this.mContext = context;
        this.mTextInputLayout = textInputLayout;
        this.mUserInfo = userInfo;
        this.mResId = mUserInfo.getId();
        this.mActionBtn = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.toString().toLowerCase().contains("https://")) {
            s = s.toString().replaceAll("https://", "");
            mUserInfo.setText(s);
        }
        if (s.toString().toLowerCase().contains("http://")) {
            s = s.toString().replaceAll("http://", "");
            mUserInfo.setText(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        switch (mResId) {
            case R.id.mail_et:
                mError = mContext.getString(R.string.error_user_mail_message);
                check(mailAdressPattern, s.toString());
                break;
            case R.id.vk:
                mError = mContext.getString(R.string.error_user_vk_message);
                check(vkAdressPattern, s.toString());
                break;
            case R.id.git_et:
                mError = mContext.getString(R.string.error_user_github_message);
                check(gitAdressPattern, s.toString());
                break;
        }
    }

    private void check(String pattern, String value) {
        mPattern = Pattern.compile(pattern);
        mMatcher = mPattern.matcher(value);

        if (!mMatcher.matches()) {
            mActionBtn.setEnabled(false);
            mActionBtn.setColorFilter(mContext.getResources().getColor(R.color.error_info));
            mTextInputLayout.setError(mError);

        } else {
            mActionBtn.setEnabled(true);
            mActionBtn.setColorFilter(mContext.getResources().getColor(R.color.correct_info));
            mTextInputLayout.setErrorEnabled(false);
        }
    }
}