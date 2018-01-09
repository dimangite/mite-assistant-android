package com.hangsopheak.miteassistant.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by hangsopheak on 1/9/18.
 */

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MITEAssistantLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ROLE = "userRole";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setUserId(int id){
        editor.putInt(KEY_USER_ID, id);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public int getUserId(){
        return pref.getInt(KEY_USER_ID, 0);
    }

    public void setUserRole(int role){
        editor.putInt(KEY_USER_ROLE, role);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public int getUserRole(){
        return pref.getInt(KEY_USER_ROLE, 0);
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
