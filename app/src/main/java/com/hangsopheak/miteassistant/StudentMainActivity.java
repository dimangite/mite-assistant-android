package com.hangsopheak.miteassistant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hangsopheak.miteassistant.adapter.StudentCoursesAdapter;
import com.hangsopheak.miteassistant.data.remote.RetrofitHelper;
import com.hangsopheak.miteassistant.data.remote.StudentService;
import com.hangsopheak.miteassistant.data.response.APIError;
import com.hangsopheak.miteassistant.data.response.CourseResponse;
import com.hangsopheak.miteassistant.data.response.StudentCourseListResponse;
import com.hangsopheak.miteassistant.data.response.UserResponse;
import com.hangsopheak.miteassistant.util.DialogFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by hangsopheak on 12/19/17.
 */

public class StudentMainActivity extends AppCompatActivity {
    /**
     * We will query geonames with this service
     */
    @NonNull
    private StudentService mStudentService;

    /**
     * Collects all subscriptions to unsubscribe later
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    private List<CourseResponse> courseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StudentCoursesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        recyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // init controls
        loadCourseList();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(StudentMainActivity.this, "You clicked " + position, Toast.LENGTH_SHORT).show();
                    }
                }
                )
        );
    }

    protected void loadCourseList(){
        mStudentService = new RetrofitHelper().getStudentService();

        final ProgressDialog progressDialog = new ProgressDialog(StudentMainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Trigger our request and display afterwards
        mCompositeDisposable.add(mStudentService.getCourses(18)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
                .subscribe(new Consumer<StudentCourseListResponse>() {
                    @Override
                    public void accept(StudentCourseListResponse studentCourseListResponse) throws Exception {
                        courseList = studentCourseListResponse.getCourses();
                        mAdapter = new StudentCoursesAdapter(courseList);
                        recyclerView.setAdapter(mAdapter);
                        progressDialog.dismiss();
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        if (throwable instanceof HttpException) {
                            HttpException httpException = ((HttpException) throwable);
                            ResponseBody errorBody = httpException.response().errorBody();
                            if (errorBody != null) {
                                try {
                                    BufferedReader reader;
                                    reader = new BufferedReader(new InputStreamReader(errorBody.byteStream()));
                                    APIError header = new Gson().fromJson(reader, APIError.class);
                                    if (header != null) {
                                        Toast.makeText(StudentMainActivity.this, header.getError(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        DialogFactory.createSimpleOkErrorDialog(StudentMainActivity.this, "Error", header.getError());
                                    }
                                } catch (Exception ex) {
                                    DialogFactory.createSimpleOkErrorDialog(StudentMainActivity.this, "Error", "Something went wrong!").show();
                                }
                            } else {
                                DialogFactory.createSimpleOkErrorDialog(StudentMainActivity.this, "Error", "Something went wrong!").show();
                            }
                        }
                        DialogFactory.createSimpleOkErrorDialog(StudentMainActivity.this, "Error", "Something went wrong!").show();
                        progressDialog.dismiss();
                    }
                })
        );
    }
}
