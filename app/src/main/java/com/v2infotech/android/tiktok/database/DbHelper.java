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

import java.util.ArrayList;
import java.util.List;

import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_CONTACT;
import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_EMAIL;
import static com.v2infotech.android.tiktok.Utils.Contants.TIKTOK_USER_NAME;
import static com.v2infotech.android.tiktok.Utils.Contants.USER_BIO;
import static com.v2infotech.android.tiktok.Utils.Contants.USER_PROFILE_PIC;


public class DbHelper extends SQLiteOpenHelper {
    // If you change the bl.womanguardian.database schema, you must increment the bl.womanguardian.database version.
    Context ctx;
    String DBNAME;
    String DBPATH;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;
    public static final String TABLE_USER_PROFILE = "user_profile_table";

    public static final String USER_DATA = "userData";
    public static final String EMAIL = "emailAddress";
    public static final String NAME = "userName";
    public static final String PASSWORD = "userPassword";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
        this.DBNAME = DATABASE_NAME;
        this.DBPATH = this.ctx.getDatabasePath(DBNAME).getAbsolutePath();
        Log.e("Path 1", DBPATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = " CREATE TABLE " + USER_DATA + " ( "
                + EMAIL + " TEXT PRIMARY KEY, "
                + NAME + " TEXT, "
                + PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);


        String CREATE_USER_PROFILE_TABLE = " CREATE TABLE " + TABLE_USER_PROFILE + " ( "
                + TIKTOK_EMAIL + " TEXT PRIMARY KEY, "
                + TIKTOK_USER_NAME + " TEXT, "
                + TIKTOK_CONTACT + " TEXT, "
                + USER_PROFILE_PIC + " TEXT, "
                + USER_BIO + " TEXT " + ")";  //
        db.execSQL(CREATE_USER_PROFILE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
        }
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
        Log.d("insert", ob.getEmailAddress() + " " + ob.getUserName() + " " + ob.getUserPassword());
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
    public LoginResponseData getUserDataa() {
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
            Log.d("getLoginData", data.getEmailAddress() + " " + data.getUserPassword());
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
        i = db.update(USER_DATA, values, EMAIL + " = '" + ob.getEmailAddress() + "' ", null);

        Log.d("insert", ob.getEmailAddress() + " " + ob.getUserName() + " " + ob.getUserPassword());
        db.close();
        return i > 0;
    }

    //
    // delete User Data from Login id
    public boolean deleteUserData(String id) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = EMAIL + " = " + id + " ";
        db.delete(USER_DATA, query, null);
        db.close();
        return result;
    }

    //Get and set User profile data

    //***************************************UserProfileData*************************************//

    public boolean upsertUserData(UserProfileResponse ob) {
        boolean done = false;
        UserProfileResponse data = null;
        if (ob.getEmail() != null) {
            data = getUserDataById(ob.getEmail());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserata(ob);
            }
        }
        return done;
    }
    //id TEXT,helplineName TEXT,helplineNumber TEXT,helplineImage TEXT,helplineWebsite TEXT,description TEXT,)";

    private void populateUserData(Cursor cursor, UserProfileResponse ob) {
        ob.setEmail(cursor.getString(0));
        ob.setName(cursor.getString(1));
        ob.setContact(cursor.getString(2));
        ob.setProfile_Pic(cursor.getString(3));
        ob.setDescription(cursor.getString(4));
    }


    public boolean insertUserData(UserProfileResponse ob) {
        ContentValues values = new ContentValues();
        values.put(TIKTOK_EMAIL, ob.getEmail());
        values.put(TIKTOK_USER_NAME, ob.getName());
        values.put(TIKTOK_CONTACT, ob.getContact());
        values.put(USER_PROFILE_PIC, ob.getProfile_Pic());
        values.put(USER_BIO, ob.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.insert(TABLE_USER_PROFILE, null, values);
        Log.d("insertUserData", String.valueOf(values));
        db.close();
        return i > 0;
    }


    public UserProfileResponse getUserData() {
        String query = "Select * FROM " + TABLE_USER_PROFILE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // UserList data = new UserList("address", "userMobile", "userName", "email", "profile", "latitude", "longitude", "distance","current_location");//
        UserProfileResponse data = new UserProfileResponse();//
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserData(cursor, data);
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //get all Addresses list data........................
    public List<UserProfileResponse> GetAllUserListData() {
        List<UserProfileResponse> list = new ArrayList<UserProfileResponse>();
        String query = "Select * FROM " + TABLE_USER_PROFILE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                //   UserList ob = new UserList("address", "userMobile", "userName", "email", "profile", "latitude", "longitude", "distance","current_location");//,"current_location"
                UserProfileResponse ob = new UserProfileResponse();//,"current_location"
                // populateUserData(cursor, ob);
                ob.setEmail(cursor.getString(0));
                ob.setName(cursor.getString(1));
                ob.setContact(cursor.getString(2));
                ob.setProfile_Pic(cursor.getString(3));
                ob.setDescription(cursor.getString(4));
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    public UserProfileResponse getUserDataById(String email) {
        String query = "Select * FROM " + TABLE_USER_PROFILE + " WHERE " + TIKTOK_EMAIL + " = '" + email + "'";
        // String query = "Select * FROM " + TABLE_USER_PROFILE + " WHERE " + TIKTOK_EMAIL + " = " + email ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //  UserList ob = new UserList("address", "userMobile", "userName", "email", "profile", "latitude", "longitude", "distance","current_location");//,"current_location"
        UserProfileResponse ob = new UserProfileResponse();//,"current_location"
        try {
            if (cursor.moveToNext()) {
                ob.setEmail(cursor.getString(0));
                ob.setName(cursor.getString(1));
                ob.setContact(cursor.getString(2));
                ob.setProfile_Pic(cursor.getString(3));
                ob.setDescription(cursor.getString(4));
            } else {
                ob = null;
            }

        } catch (Exception ex) {

        } finally {
            cursor.close();
            db.close();
        }
        return ob;

    }

    //id TEXT,latitude TEXT,longitude TEXT,userName TEXT,userMobile TEXT,address TEXT)";
    public boolean updateUserata(UserProfileResponse ob) {
        ContentValues values = new ContentValues();
        values.put(TIKTOK_EMAIL, ob.getEmail());
        values.put(TIKTOK_USER_NAME, ob.getName());
        values.put(TIKTOK_CONTACT, ob.getContact());
        values.put(USER_PROFILE_PIC, ob.getProfile_Pic());
        values.put(USER_BIO, ob.getDescription());
        //  values.put("current_location", ob.getCurrent_location());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update(TABLE_USER_PROFILE, values, TIKTOK_EMAIL + " ='" + ob.getEmail() + "' ", null);
        Log.d("updateUserata", String.valueOf(values));
        db.close();
        return i > 0;
    }

    public void deleteDeviceLatLongData() {
        getWritableDatabase().delete(TABLE_USER_PROFILE, null, null);
    }


}
