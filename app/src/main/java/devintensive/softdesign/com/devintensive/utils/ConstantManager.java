package devintensive.softdesign.com.devintensive.utils;

public interface ConstantManager {

    String TAG_PREFIX = "DEV ";

    int VIEW_MODE = 0;
    int EDIT_MODE = 1;

    int MAKE_CALL   = 0;
    int SEND_EMAIL  = 1;
    int OPEN_VK     = 2;
    int OPEN_GITHUB = 3;

    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_6_KEY";

    int LOAD_PROFILE_PHOTO = 1;

    int LOAD_FROM_GALLERY = 0;
    int LOAD_FROM_CAMERA = 1;

    int REQUEST_GALLERY_PICTURE = 88;
    int REQUEST_CAMERA_PICTURE = 99;
    int PERMISSION_REQUEST_SETTING_CODE = 100;
    int CAMERA_REQUEST_PERMISSION_CODE = 101;
}
