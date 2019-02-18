package com.v2infotech.android.tiktok.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class CommonMethod {
    private static Editor editor;

    static class C04941 implements OnClickListener {
        C04941() {
        }

        public void onClick(DialogInterface dialog, int id) {
        }
    }

    public static void setPrefsData(Context context, String prefsKey, String prefValue) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(prefsKey, prefValue);
        editor.commit();
    }

    public static void setPrefsLoginStatus(Context context, String prefsKey, boolean prefValue) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(prefsKey, prefValue);
        editor.commit();
    }

    public static boolean getPrefsLoginData(Context context, String prefsKey, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(prefsKey, defaultValue);
    }

    public static String getPrefsData(Context context, String prefsKey, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(prefsKey, defaultValue);
    }

    public static void showAlert(String message, Activity context) {
        Builder builder = new Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new C04941());
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public static boolean isEmailValid(String email) {
        if (Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 2).matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        try {
            @SuppressLint("WrongConstant") NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getEmployeeStatus(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("status", "");
    }



    public static String getEmployeeName(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Name", "");
    }

    public static String getEmployeeID(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("EmpId", "");
    }

    public static String getDOB(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("DOB", "");
    }

    public static String getEmployeeGender(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Gender", "");
    }

    public static String getCategory(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Category", "");
    }

    public static String getNationality(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Nationality", "");
    }

    public static String getEmployeeDOJ(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("DOJ", "");
    }

    public static String getemployeetype(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("ClassTeacher", "");
    }

    public static String getSpecialization(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Specialization", "");
    }

    public static String getAddress(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("Address", "");
    }

    public static String getCity(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("City", "");
    }

    public static String getState(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("State", "");
    }

    public static String getMobileNo(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("MobileNo", "");
    }

    public static String getemail(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("email", "");
    }

    public static String getMobileNumber(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("MobileNo", "");
    }

    public static String getleave(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("LeaveType", "");
    }

    public static String getsrno(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("srno", "");
    }

    public static String getProfilePhoto(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("ProfilePhoto", "");
    }

    public static String getapprovel_id(Activity conteActivity) {
        return PreferenceManager.getDefaultSharedPreferences(conteActivity).getString("is_approver", "");
    }
    public static  void clearPrefences(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
    public static void saveLoginPreferences(Context context, String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                int bytesRead = input.read(buffer);
                if (bytesRead == -1) {
                    return true;
                }
                output.write(buffer, 0, bytesRead);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @SuppressLint("WrongConstant")
    public static void closeKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setLoginPreferences(Context context, String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
