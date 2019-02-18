package com.v2infotech.android.tiktok.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    LruCache<String, Bitmap> mMemoryCache;

    public static File getFilePath(Context context) {
        File konnectDirectory = new File(context.getFilesDir(), Contants.APP_DIRECTORY);
        return konnectDirectory;
    }

    public static int getAppVersion(Context context) {
        int version = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);
        }
        return version;
    }

    //get app version name
    public static String getAppVersionName(Context context) {
        String version = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return version;
    }

    //get app package name
    public static String getAppPackageName(Context context) {
        String pkName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            pkName = packageInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return pkName;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }

    public static Date parseDateFromString(String strDate) {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toCalendar(final String iso8601string) throws ParseException {
        //GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        String s = iso8601string;
        Date date = null;
        try {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 26) + s.substring(27);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(s);
        } catch (Exception tc) {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 22) + s.substring(23);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s);
        }

//        nahi hua kuch b show
        //calendar.setTime(date);
        return date;
    }

    //convert date for get day month year hours min am/pm
    public static String convertDate(Date date) {
        StringBuilder sb = new StringBuilder();
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        String hours = (String) android.text.format.DateFormat.format("h", date); //20
        String min = (String) android.text.format.DateFormat.format("mm", date); //20
        String ampm = (String) android.text.format.DateFormat.format("aa", date); //20
        //Log.d(Contants.LOG_TAG, "dayOfTheWeek*********" + dayOfTheWeek);
        //Log.d(Contants.LOG_TAG, "year*********" + year);
        //Log.d(Contants.LOG_TAG, "stringMonth*********" + stringMonth+"  "+intMonth);
           /* Log.d(Contants.LOG_TAG, "day*********" + day);
            Log.d(Contants.LOG_TAG, "hours*********" + hours);
            Log.d(Contants.LOG_TAG, "min*********" + min);
            Log.d(Contants.LOG_TAG, "ampm*********" + ampm);*/
        List<String> list = new ArrayList<String>();
        list.add(dayOfTheWeek);
        list.add(day);
        list.add(stringMonth);
        list.add(hours);
        list.add(min);
        list.add(ampm);
        list.add(year);
        list.add(intMonth);
        String delim = "";
        for (String i : list) {
            sb.append(delim).append(i);
            delim = ",";
        }
        return sb.toString();
    }


    //calculate total days between to days
    public static int daysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    //encode image into base64 string
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(compressFormat, quality, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    //decode base64 string into image
    public static Bitmap decodeBase64(String input) {
        Bitmap bitmap = null;
        if (input != null) {
            byte[] decodedBytes = Base64.decode(input, 0);
            bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return bitmap;
    }

    //    //check online
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }






    //get device id for  GCM


    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //set location....................................
    public static void setLocationId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("LocalityId", id);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Uri uri = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path != null) {
            uri = Uri.parse(path);
        }
        return uri;
    }

 /*   //chack run time EXTERNAL_STORAGE permission
    public boolean checkExternalStoragePermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission Necessary");
                    alertBuilder.setMessage("External Storage Permission is Necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }*/

    //set store id
    public static void setStoreIDFromSharedPreferences(Context context, int storeId) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("StoreIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("StoreId", storeId);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    public static void setPhoneNoVerifyOrNotSharedPreferences(Context context, Boolean flag) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("NoVerifyOrNotPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("NoVerify", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }
}
