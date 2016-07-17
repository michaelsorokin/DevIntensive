package devintensive.softdesign.com.devintensive.data.managers;

import devintensive.softdesign.com.devintensive.data.network.RestService;
import devintensive.softdesign.com.devintensive.data.network.ServiceGenerator;
import devintensive.softdesign.com.devintensive.data.network.req.UserLoginReq;
import devintensive.softdesign.com.devintensive.data.network.res.AuthModelRes;
import devintensive.softdesign.com.devintensive.data.network.res.UserListRes;
import devintensive.softdesign.com.devintensive.data.network.res.UserModelRes;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

public class DataManager {

    private static DataManager INSTANCE = null;

    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    private DataManager () {

        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getInstance() {

        if(INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    // region ========================= network =================================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<ResponseBody> uploadPhoto(MultipartBody.Part file){
        return mRestService.uploadPhoto(getPreferencesManager().getUserId(), file);
    }

    public Call<AuthModelRes> checkUser (String userId) {return mRestService.checkUser(userId); }

    public Call<UserListRes> getUserList() {
        return mRestService.getUserList();
    }
    // endregion
}
