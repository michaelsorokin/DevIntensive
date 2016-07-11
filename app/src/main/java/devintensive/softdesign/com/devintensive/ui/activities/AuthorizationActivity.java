package devintensive.softdesign.com.devintensive.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;

public class AuthorizationActivity extends AppCompatActivity  {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Auth Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }
}
