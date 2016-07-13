package devintensive.softdesign.com.devintensive.data.network.req;

import android.net.Uri;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class UploadFileReq {

    private MultipartBody.Part mBody;

    public MultipartBody.Part photo(Uri uri){

        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        return mBody;
    }

    public MultipartBody.Part avatar(Uri uri){

        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        return mBody;
    }
}