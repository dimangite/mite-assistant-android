package com.hangsopheak.miteassistant.data.remote;

/**
 * Created by hangsopheak on 12/13/17.
 */

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class initializes retrofit with a default configuration.
 * You can use this class to initialize the different services.
 */

public class RetrofitHelper {
    static final String baseUrl = "http://10.0.2.2/mite-attendance/public/api/v1/";
    /**
     * The CityService communicates with the json api of the city provider.
     */
    public UserService getUserService() {
        final Retrofit retrofit = createRetrofit();
        return retrofit.create(UserService.class);
    }

    public StudentService getStudentService(){
        final Retrofit retrofit = createRetrofit();
        return retrofit.create(StudentService.class);
    }

    /**
     * This custom client will append the "username=demo" query after every request.
     */
    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();

                final HttpUrl url = originalHttpUrl.newBuilder()
                        .build();

                // Request customization: add request headers
                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }

    /**
     * Creates a pre configured Retrofit instance
     */
    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // <- add this
                .client(createOkHttpClient())
                .build();
    }
}