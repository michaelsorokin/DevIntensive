package devintensive.softdesign.com.devintensive.data.network.interseptors;


import java.io.IOException;

import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterseptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        DataManager dataManager = DataManager.getInstance();
        String authToken = dataManager.getPreferencesManager().getAuthToken();
        String userId = dataManager.getPreferencesManager().getUserId();

        Request original = chain.request();
        Request.Builder builder = original.newBuilder().
                header("X-Access-Token", authToken).
                header("Request-User-Id", userId).
                header("User-Agent", "DevIntensive");

        Request request = builder.build();
        return chain.proceed(request);
    }
}