package devintensive.softdesign.com.devintensive.utils;

public interface ConstantManager {

    String TAG_PREFIX = "DEV ";

    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_6_KEY";

    String USER_FIRST_NAME = "USER_FIRST_NAME";
    String USER_SECOND_NAME = "USER_SECOND_NAME";

    String USER_AVATAR_KEY = "USER_AVATAR_KEY";

    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";

    String USER_RAITING_VALUE = "USER_RAITING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";

    String USER_LOGIN_KEY = "USER_LOGIN_KEY";
    String USER_PASSWORD_KEY = "USER_PASSWORD_KEY";
    String USER_SAVEME_KEY = "USER_SAVEME_KEY";

    int VIEW_MODE = 0;
    int EDIT_MODE = 1;

    int MAKE_CALL   = 0;
    int SEND_EMAIL  = 1;
    int OPEN_VK     = 2;
    int OPEN_GITHUB = 3;

    int USER_RAITING = 0;
    int USER_CODE_LINES = 1;
    int USER_PROJECT = 2;

    int LOAD_PROFILE_PHOTO = 1;

    int LOAD_FROM_GALLERY = 0;
    int LOAD_FROM_CAMERA = 1;

    int REQUEST_GALLERY_PICTURE = 88;
    int REQUEST_CAMERA_PICTURE = 99;
    int PERMISSION_REQUEST_SETTING_CODE = 100;
    int CAMERA_REQUEST_PERMISSION_CODE = 101;

    String PARCELABLE_KEY = "PARCELABLE_KEY";

    int RESPONSE_OK         = 200;
    int RESPONSE_ACC_DENIED = 404;
    int RESPONSE_FORBIDDEN  = 403;


}
