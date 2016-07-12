package devintensive.softdesign.com.devintensive.data.network;

import devintensive.softdesign.com.devintensive.data.network.req.UserLoginReq;
import devintensive.softdesign.com.devintensive.data.network.res.AuthModelRes;
import devintensive.softdesign.com.devintensive.data.network.res.UserModelRes;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);

    @Multipart
    @POST ("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> uploadPhoto(@Path("userId") String userId, @Part MultipartBody.Part file);

    @GET("user/{userId}")
    Call<AuthModelRes> checkUser (@Path("userId") String userId);

}
