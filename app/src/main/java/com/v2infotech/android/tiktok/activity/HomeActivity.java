package com.v2infotech.android.tiktok.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.BottomNavigationViewHelper;
import com.v2infotech.android.tiktok.camera.PortrateActivity;
import com.v2infotech.android.tiktok.fragment.DashboardFragment;
import com.v2infotech.android.tiktok.fragment.FeedbackFragment;
import com.v2infotech.android.tiktok.fragment.ProfileFragment;
import com.v2infotech.android.tiktok.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;
    private String userChoosenTask;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public final int REQUEST_CAMERA = 101;
    public final int SELECT_PHOTO = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
        String session_id = settings.getString("session_id", "");
        if (session_id == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        } else {
            toolbar = getSupportActionBar();
            // load the store fragment by default
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
            loadFragment(new DashboardFragment());
        }
        // toolbar.setTitle("Shop");
    }


    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(new DashboardFragment());
                return true;
            case R.id.navigation_dashboard:
                loadFragment(new SearchFragment());
                return true;
            case R.id.navigation_notifications:
                loadFragment(new FeedbackFragment());
                return true;
            case R.id.navigation_profile:
                SharedPreferences sp = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
                String session_id = sp.getString("session_id", "");
                if (session_id != null) {
                    loadFragment(new ProfileFragment());
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.navigation_video:
//               loadFragment(new VideoFragment());
                if (checkForPermission()) {
                    Intent intent = new Intent(HomeActivity.this, PortrateActivity.class);
                    startActivity(intent);
                }
                return true;
        }

        return false;
    }


    private boolean checkForPermission() {
/*        new BaseActivity().requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_STORAGE, new BaseActivity.setPermissionListener() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        selectVideoDialog();
                    }

                    @Override
                    public void onPermissionDenied(int requestCode) {
//                        showSnackbar(mBinder.getRoot(), getString(R.string.critical_permission_denied),
//                                Snackbar.LENGTH_INDEFINITE, getString(R.string.allow), new OnSnackbarActionListener() {
//                                    @Override
//                                    public void onAction() {
//                                        checkForPermission();
//                                    }
//                                });
                    }

                    @Override
                    public void onPermissionNeverAsk(int requestCode) {
//                        showPermissionSettingDialog(getString(R.string.permission_gallery_camera));
                    }
                });*/
        List<String> permissionsNeeded = new ArrayList<String>();


        final List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Storage");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write external storage");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return false;
        }
        return true;
    }

    //add run time permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}