package com.jecna.task.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TokenInterceptor implements Interceptor {

    private String token;

    public TokenInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(request);
    }
}
