package com.yelp.fusion.client.connection.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yduan on 5/7/2017.
 */

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        originalRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return chain.proceed(originalRequest);
    }
}
