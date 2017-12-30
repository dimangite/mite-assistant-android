
package com.hangsopheak.miteassistant.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("course_day")
    @Expose
    private Integer courseDay;
    @SerializedName("course_time")
    @Expose
    private String courseTime;
    @SerializedName("lecturer")
    @Expose
    private LecturerResponse lecturer;
    @SerializedName("subject")
    @Expose
    private SubjectResponse subject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public Integer getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(Integer courseDay) {
        this.courseDay = courseDay;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public LecturerResponse getLecturer() {
        return lecturer;
    }

    public void setLecturer(LecturerResponse lecturer) {
        this.lecturer = lecturer;
    }

    public SubjectResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponse subject) {
        this.subject = subject;
    }

}

