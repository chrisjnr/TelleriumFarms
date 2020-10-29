package com.iconic_dev.telleriumfarms.ui.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.iconic_dev.telleriumfarms.api.config.PrefsUser;

/**
 * Created by manuelchris-ogar on 16/07/2020.
 */
public class UserPrefs {

    private static Gson gson = new Gson();

    private static UserPrefs userPreferences;

    private SharedPreferences sharedPreferences;

    public static String PREFS_NAME = "telfarms";
    private String MOST_RECENT_USER =  "MOST_RECENT_USER";



    public static UserPrefs getInstance(Context context) {
        userPreferences = new UserPrefs(context);
        return userPreferences;
    }


    public UserPrefs(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }




    public void setMostRecentUser(String username, String password){
        PrefsUser savedUser  = new PrefsUser();
        savedUser.setUsername(username);
        savedUser.setPassword(password);
        String savedUserJson = gson.toJson(savedUser);
        sharedPreferences.edit().putString(MOST_RECENT_USER, savedUserJson).apply();
    }


    public PrefsUser getMostRecentUser() {
        PrefsUser prefsUser;
        try{
            prefsUser = gson.fromJson(sharedPreferences.getString(MOST_RECENT_USER, ""), PrefsUser.class);
            return prefsUser;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
