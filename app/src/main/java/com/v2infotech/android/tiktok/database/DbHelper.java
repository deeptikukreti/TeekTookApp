package com.v2infotech.android.tiktok.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.v2infotech.android.tiktok.Utils.Contants;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.model.UserProfileResponse;

import static com.v2infotech.android.tiktok.Utils.Contants.IMAGE_PROFILE;
import static com.v2infotech.android.tiktok.Utils.Contants.IMAGE_VIDEO;
import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_ID;
import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_USER_BIO;
import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_USER_NAME;


public class DbHelper extends SQLiteOpenHelper {
    // If you change the bl.womanguardian.database schema, you must increment the bl.womanguardian.database version.
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;
    public static final String TABLE_USER_PROFILE = "user_profile_table";

    public static final String USER_DATA = "userData";
    public static final String EMAIL = "emailAddress";
    public static final String NAME = "userName";
    public static final String PASSWORD = "userPassword";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = " CREATE TABLE " + USER_DATA + " ( "
                + EMAIL + " TEXT PRIMARY KEY, "
                + NAME + " TEXT, "
                + PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);


        String CREATE_USER_PROFILE_TABLE = " CREATE TABLE " + TABLE_USER_PROFILE + " ( "
                + TIKTOK_ID + " TEXT PRIMARY KEY, "
                + IMAGE_PROFILE + " TEXT, "
                + IMAGE_VIDEO + " TEXT, "
                + TIKTOK_USER_NAME + " TEXT, "
                + TIKTOK_USER_BIO + " TEXT " + ")";  //
        db.execSQL(CREATE_USER_PROFILE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //--------------------------SignUpuserData-------------------------------------------//
    public boolean upsertUserData(LoginResponseData ob) {
        boolean done = false;
        LoginResponseData data = null;
        if (ob.getEmailAddress() != null) {
            data = getUserDataByLoginId(ob.getEmailAddress());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserData(ob);
            }
        }
        return done;
    }

    //
    // for user data..........
    private void populateUserData(Cursor cursor, LoginResponseData ob) {
        ob.setEmailAddress(cursor.getString(0));
        ob.setUserName(cursor.getString(1));
        ob.setUserPassword(cursor.getString(2));
    }

    //..insert userData data.............
    public boolean insertUserData(LoginResponseData ob) {
        ContentValues values = new ContentValues();
        values.put(EMAIL, ob.getEmailAddress());
        values.put(NAME, ob.getUserName());
        values.put(PASSWORD, ob.getUserPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert(USER_DATA, null, values);
        Log.d("insert",ob.getEmailAddress()+" "+ob.getUserName()+" "+ob.getUserPassword());
        db.close();
        return i > 0;
    }

    // for userData data.............
    private void populateUserformation(Cursor cursor, LoginResponseData ob) {
        ob.setEmailAddress(cursor.getString(0));
        ob.setUserName(cursor.getString(1));
        ob.setUserPassword(cursor.getString(2));
    }

    //userData data
    public LoginResponseData getUserData() {
        String query = "Select * FROM userData";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
      //  LoginResponseData data = new LoginResponseData(EMAIL,NAME, PASSWORD);
        LoginResponseData data = new LoginResponseData();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //userData data
    public LoginResponseData getUserDataByLoginId(String id) {
        String query = "Select * FROM " + USER_DATA + " WHERE " + EMAIL + " ='" + id + "'";
       // String query = "Select * FROM userData WHERE id = " + id + " ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LoginResponseData data = new LoginResponseData();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);
            Log.d("getLoginData",data.getEmailAddress()+" "+data.getUserPassword());
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update user data
    public boolean updateUserData(LoginResponseData ob) {
        ContentValues values = new ContentValues();
        values.put(EMAIL, ob.getEmailAddress());
        values.put(NAME, ob.getUserName());
        values.put(PASSWORD, ob.getUserPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update(USER_DATA, values, EMAIL+" = '" + ob.getEmailAddress() + "' ", null);

        Log.d("insert",ob.getEmailAddress()+" "+ob.getUserName()+" "+ob.getUserPassword());
        db.close();
        return i > 0;
    }

    //
    // delete User Data from Login id
    public boolean deleteUserData(String id) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = EMAIL+" = " + id + " ";
        db.delete(USER_DATA, query, null);
        db.close();
        return result;
    }


    //Get and set User profile data

    //***************************************UserProfileData*************************************//

    public boolean upsertUserProfileData(UserProfileResponse ob) {
        boolean done = false;
        UserProfileResponse data = null;
        if (ob.getUser_tiktok_id() != null) {
            data = getUserProfileData(ob.getUser_tiktok_id());
            if (data == null) {
                done = insertUserProfileData(ob);
            } else {
                done = updateUserProfileData(ob);
            }
        }
        return done;
    }

    public boolean insertUserProfileData(UserProfileResponse ob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIKTOK_ID, ob.getUser_tiktok_id());
        values.put(IMAGE_PROFILE, ob.getImage_profile());
        values.put(IMAGE_VIDEO, ob.getImage_video());
        values.put(TIKTOK_USER_NAME, ob.getUser_Name());
        values.put(TIKTOK_USER_BIO, ob.getUser_Bio());
        long i = db.insert(TABLE_USER_PROFILE, null, values);
        db.close();
        Log.d("", "insertUserProfileData " + values);
        return true;
    }

    public UserProfileResponse getUserProfileData(String tiktok_id) {
        String query = "Select * FROM " + TABLE_USER_PROFILE + " WHERE " + TIKTOK_ID + " ='" + tiktok_id + "'";
        // String deleteQuery = "DELETE FROM Students where StudentId='"+ id +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        UserProfileResponse ob = new UserProfileResponse();
        if (cursor != null && cursor.moveToFirst()) {
            ob.setUser_tiktok_id(cursor.getString(0));
            ob.setImage_profile(cursor.getString(1));
            ob.setImage_video(cursor.getString(2));
            ob.setUser_Name(cursor.getString(3));
            ob.setUser_Bio(cursor.getString(4));
            cursor.moveToFirst();
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    public boolean updateUserProfileData(UserProfileResponse ob) {
        ContentValues values = new ContentValues();
        values.put(TIKTOK_ID, ob.getUser_tiktok_id());
        values.put(IMAGE_PROFILE, ob.getImage_profile());
        values.put(IMAGE_VIDEO, ob.getImage_video());
        values.put(TIKTOK_USER_NAME, ob.getUser_Name());
        values.put(TIKTOK_USER_BIO, ob.getUser_Bio());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update(TABLE_USER_PROFILE, values, TIKTOK_ID + " = '" + ob.getUser_tiktok_id() + "' ", null);
        db.close();
        return i > 0;
    }

    public void deleteAllUserProfile() {
        getWritableDatabase().delete(TABLE_USER_PROFILE, null, null);

    }


}
