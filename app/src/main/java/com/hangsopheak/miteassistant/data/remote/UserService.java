package com.hangsopheak.miteassistant.data.remote;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Body;

import com.hangsopheak.miteassistant.data.request.LoginModel;
import com.hangsopheak.miteassistant.data.response.UserResponse;

public interface UserService {
    @POST("users/login")
    Observable<UserResponse> login(@Body LoginModel loginModel);
}

