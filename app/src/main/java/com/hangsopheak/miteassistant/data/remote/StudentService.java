package com.hangsopheak.miteassistant.data.remote;

import com.hangsopheak.miteassistant.data.response.StudentCourseListResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StudentService {
    @GET("students/{id}/courses")
    Observable<StudentCourseListResponse> getCourses(@Path("id") int id);
}

