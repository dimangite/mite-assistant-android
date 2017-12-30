package com.hangsopheak.miteassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangsopheak.miteassistant.R;
import com.hangsopheak.miteassistant.data.response.CourseResponse;

import java.util.List;

/**
 * Created by hangsopheak on 12/20/17.
 */

public class StudentCoursesAdapter extends RecyclerView.Adapter<StudentCoursesAdapter.MyViewHolder>
{
    private List<CourseResponse> courseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectName, lecturerName;

        public MyViewHolder(View view) {
            super(view);
            subjectName = (TextView) view.findViewById(R.id.subject_name);
            lecturerName = (TextView) view.findViewById(R.id.lecturer_name);
        }
    }


    public StudentCoursesAdapter(List<CourseResponse> courseList) {
        this.courseList = courseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_course_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CourseResponse course = courseList.get(position);
        holder.subjectName.setText(course.getSubject().getName());
        holder.lecturerName.setText(course.getLecturer().getName());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
