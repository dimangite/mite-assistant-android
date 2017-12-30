package com.hangsopheak.miteassistant.data.response;

/**
 * Created by hangsopheak on 12/19/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentCourseListResponse {

    @SerializedName("data")
    @Expose
    private List<CourseResponse> courses = null;

    public List<CourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponse> courses) {
        this.courses = courses;
    }

}