package com.hangsopheak.miteassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hangsopheak.miteassistant.data.remote.RetrofitHelper;
import com.hangsopheak.miteassistant.data.remote.UserService;
import com.hangsopheak.miteassistant.data.response.APIError;

import com.hangsopheak.miteassistant.data.request.LoginModel;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.hangsopheak.miteassistant.data.response.UserResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import com.google.gson.Gson;
import com.hangsopheak.miteassistant.helper.SessionManager;
import com.hangsopheak.miteassistant.util.DialogFactory;
import android.app.ProgressDialog;
/**
 * Created by hangsopheak on 12/11/17.
 */

public class LoginActivity extends AppCompatActivity {

    /**
     * We will query geonames with this service
     */
    @NonNull
    private UserService mUserService;

    /**
     * Collects all subscriptions to unsubscribe later
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected UserResponse user;
    protected EditText etEmail;
    protected EditText etPassword;
    protected Button btnLogin;

    protected boolean loginResult = false;
    protected SessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Session manager
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = null;
            if(session.getUserRole() == UserResponse.ROLE_STUDENT){
                intent = new Intent(LoginActivity.this, StudentMainActivity.class);
            }else if(session.getUserRole() == UserResponse.ROLE_LECTURER){
                intent = new Intent(LoginActivity.this, LecturerMainActivity.class);
            }
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init controls
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyLogin();
            }
        });
    }

    protected void verifyLogin()  {
        String email = (String) etEmail.getText().toString();
        String password = (String) etPassword.getText().toString();

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(email);
        loginModel.setPassword(password);

        // Initialize the city endpoint
        mUserService = new RetrofitHelper().getUserService();
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        // Trigger our request and display afterwards
        mCompositeDisposable.add(mUserService.login(loginModel)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
                .subscribe(new Consumer<UserResponse>() {
                    @Override
                    public void accept(UserResponse userResponse){
                        session.setLogin(true);
                        session.setUserId(userResponse.getId());
                        session.setUserRole(userResponse.getRoleId());
                        Intent intent = null;
                        if(userResponse.getRoleId() == UserResponse.ROLE_LECTURER){
                            intent = new Intent(LoginActivity.this, LecturerMainActivity.class);
                        }else if(userResponse.getRoleId() == UserResponse.ROLE_STUDENT){
                            intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                        }
                        startActivity(intent);

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
                                        Toast.makeText(LoginActivity.this, header.getError(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Error", header.getError());
                                    }
                                } catch (Exception ex) {
                                    DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Error", "Something went wrong!").show();
                                }
                            } else {
                                DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Error", "Something went wrong!").show();
                            }
                        }
                        DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Error", "Something went wrong!").show();
                        progressDialog.dismiss();
                    }
                })
        );
    }
    protected void alert(String title, String body, DialogInterface dialogInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                LoginActivity.this);
        builder.setTitle("Error");
        builder.setMessage("One Action Button Alert");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
