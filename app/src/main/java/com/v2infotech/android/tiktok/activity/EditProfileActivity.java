package com.v2infotech.android.tiktok.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CircleTransform;
import com.v2infotech.android.tiktok.Utils.Utility;
import com.v2infotech.android.tiktok.database.DbHelper;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.videotrimmer.VideoPicker;
import com.v2infotech.android.tiktok.videotrimmer.VideoTrimmerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.videotrimmer.utils.FileUtils;

import static com.v2infotech.android.tiktok.videotrimmer.Constants.EXTRA_VIDEO_PATH;

@SuppressWarnings("ALL")
public class EditProfileActivity extends AppCompatActivity {
    //videtrim.........
//    ActivityMainBinding mBinder;
    public static final int PERMISSION_STORAGE = 100;
    private final int REQUEST_VIDEO_TRIMMER_RESULT = 342;

    private final int REQUEST_VIDEO_TRIMMER = 0x12;
    private File thumbFile;
    private String selectedVideoName = null, selectedVideoFile = null;
    private RequestOptions simpleOptions;
    //................
    private ImageView inside_imageview, outside_imageview, outside_video;
    private TextView save_icon, back_arrow_icon;
    private EditText profileName, bio_edt,tiktok_id_edt;
    DbHelper dbHelper;
    private static final int INTENT_REQUEST_CODE = 100;
    //  public static final String URL = BASE_URL;
    private String mImageUrl = "";
    private String userChoosenTask;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public final int REQUEST_CAMERA = 101;
    public final int SELECT_PHOTO = 102;
    Uri uri = null;
    Uri selectedVideoUri=null;
    Bitmap bitmapImage;
    Bitmap bm;
    String base64Image;
    AppCompatImageView inside_video;
    private MediaController mediaControls;

    VideoView video_view;


    // Use this string for part 2 (load media from the internet).
    private static final String VIDEO_SAMPLE =
            "https://developers.google.com/training/images/tacoma_narrows.mp4";


    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";


    // Request code for user select video file.
    private static final int REQUEST_CODE_SELECT_VIDEO_FILE = 1;

    // Request code for require android READ_EXTERNAL_PERMISSION.
    private static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 2;

    // Save local video file uri.
    private Uri videoFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dbHelper = new DbHelper(EditProfileActivity.this);
        getIds();

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        inside_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForPermission()) {
                    selectVideoDialog();
                }

            }
        });
        simpleOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.blackOverlay)
                .error(R.color.blackOverlay)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            inside_video.pause();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
//        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
//        outState.putInt(PLAYBACK_TIME, inside_video.getCurrentPosition());
    }

    private void getIds() {
        inside_imageview = findViewById(R.id.inside_imageview);
        outside_imageview = findViewById(R.id.outside_imageview);
        inside_video = findViewById(R.id.inside_video);
        outside_video = findViewById(R.id.outside_video);
        save_icon = findViewById(R.id.save_icon);
        profileName = findViewById(R.id.profileName);
        tiktok_id_edt = findViewById(R.id.tiktok_id_edt);
        bio_edt = findViewById(R.id.bio_edt);
        back_arrow_icon = findViewById(R.id.back_arrow_icon);
        video_view = findViewById(R.id.video_view);


//        final VideoView videoView = (VideoView)
//                findViewById(R.id.videoView1);

//        inside_video.setVideoPath(
//                "http://www.ebookfrenzy.com/android_book/movie.mp4");
//


        SharedPreferences sp = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String email = sp.getString("name", "");
        String id_tiktok = sp.getString("tiktok_id", "");
        String biooo = sp.getString("bio", "");
        String image_uri1= sp.getString("image_uri", "");
        String video_uri1 = sp.getString("video_uri", "");
        if (email != null && id_tiktok != null ) {
            tiktok_id_edt.setText(id_tiktok);
            profileName.setText(email);
           // bio_edt.setText(biooo);
        }
        if (video_uri1 != null) {
            Uri uri_video = Uri.parse(video_uri1);
            Picasso.with(this).load(uri_video).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(inside_video);
        }  if (image_uri1 != null) {
            Uri uri_image = Uri.parse(image_uri1);
            Picasso.with(this).load(uri_image).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(inside_imageview);
        }

        if (biooo != null) {
            bio_edt.setText(biooo);
        }

        LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(email);
        if (loginResponseData != null) {
            profileName.setText(loginResponseData.getUserName());
            tiktok_id_edt.setText(id_tiktok);
        }

        back_arrow_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        outside_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkRuntimePermission()) {
                        selectImage();
                    }
                }
            }
        });


       /* // If user click browse video file button.
        outside_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check whether user has granted read external storage permission to this activity.
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                // If not grant then require read external storage permission.
                if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(EditProfileActivity.this, requirePermission, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                } else {
                }
            }
        });*/

        Picasso.with(this).load(R.drawable.user_profile).transform(new CircleTransform()).into(inside_imageview);
        Picasso.with(this).load(R.drawable.user_profile).transform(new CircleTransform()).into(inside_video);

        save_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = profileName.getText().toString();
                String tiktok_id = tiktok_id_edt.getText().toString();
                String bio = bio_edt.getText().toString();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_PREFS", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("name", user_name);
                editor.putString("tiktok_id", tiktok_id);
                editor.putString("bio", bio);
                editor.putString("image_uri", String.valueOf(uri));
                editor.putString("video_uri", String.valueOf(selectedVideoUri));
                editor.commit();
//                DbHelper dbHelper=new DbHelper(getApplicationContext());
//                UserProfileResponse userProfileResponse=new UserProfileResponse();
//                userProfileResponse.setUser_Name(user_name);
//                userProfileResponse.setUser_tiktok_id(tiktok_id);
//                userProfileResponse.setUser_Bio(bio);
//                dbHelper.upsertUserProfileData(userProfileResponse);

                onBackPressed();
            }
        });

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //open camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //select image from android.widget.Gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(intent, SELECT_PHOTO);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_PHOTO) {
//                onSelectFromGalleryResult(data);
//            } else if (requestCode == REQUEST_CAMERA) {
//                onCaptureImageResult(data);
//            }
//        }
    // Identify activity by request code.
//        if (requestCode == REQUEST_CODE_SELECT_VIDEO_FILE) {
    // If the request is success.
//            if (resultCode == RESULT_OK) {
    // To make example simple and clear, we only choose video file from local file,
    // this is easy to get video file real local path.
    // If you want to get video file real local path from a video content provider
    // Please read another article.
//                videoFileUri = data.getData();
//
//                String videoFileName = videoFileUri.getLastPathSegment();

    // videoPathEditor.setText("You select video file is " + videoFileName);

    //playVideoButton.setEnabled(true);

    // pauseVideoButton.setEnabled(false);

    // replayVideoButton.setEnabled(false);
//            }
//        }
//    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                uri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                // bitmapImage=bm;
                bm = BitmapFactory.decodeFile(String.valueOf(uri));
                base64Image = Utility.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);

                //uploadBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (uri != null) {
            // profile_image.setImageURI(uri);
            Picasso.with(this).load(uri).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(inside_imageview);
            // Picasso.with(getActivity()).load(uri).resize(800, 800).into(user_profile);
            addUriAsFile(uri);
        }
    }


    //add bitmap
    private Boolean addUriAsFile(final Uri uri) {
        new AsyncTask<Void, Void, Boolean>() {
            Bitmap bm;

            @Override
            protected Boolean doInBackground(Void... voids) {

                Boolean flag = false;
                File sdcardPath = Utility.getFilePath(EditProfileActivity.this);
                sdcardPath.mkdirs();
                File imgFile = new File(sdcardPath, System.currentTimeMillis() + ".png");
                try {
                    InputStream iStream = EditProfileActivity.this.getContentResolver().openInputStream(uri);
                    byte[] inputData = Utility.getBytes(iStream);

                    FileOutputStream fOut = new FileOutputStream(imgFile);
                    fOut.write(inputData);
                    //   bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    iStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                Bitmap bm = decodeSampledBitmapFromResource(imgFile.getAbsolutePath(), 800, 800);
                if (bm != null) {
                    base64Image = Utility.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (bm != null) {
                    Picasso.with(EditProfileActivity.this).load(uri).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(inside_imageview);
                    //  inside_imageview.setImageBitmap(bm);
                    // user_profile.setImageBitmap(bm);

                }
            }
        }.execute();

        return true;
    }

    //create bitmap
    public Bitmap decodeSampledBitmapFromResource(String str,
                                                  int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    //calculate size
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void onCaptureImageResult(Intent data) {
        // bm = BitmapFactory.decodeFile
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        uri = Utility.getImageUri(this, thumbnail);
        bitmapImage = thumbnail;
        //uploadBitmap(thumbnail);
        base64Image = Utility.encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);

        if (uri != null) {
            Toast.makeText(this, "image: " + uri, Toast.LENGTH_SHORT).show();
            // profile_image.setImageURI(uri);
            Picasso.with(this).load(uri).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(inside_imageview);

            //Picasso.with(context).load(uri).into(img_user_profile);
            //Picasso.with(getActivity()).load(uri).into(user_profile);
            // addUriAsFile(uri);
        }

    }

    //check storage and camera run time permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkRuntimePermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write external storage");
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
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
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


    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        //  mBufferingTextView.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.
//        Uri videoUri = getMedia(VIDEO_SAMPLE);
//        inside_video.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
//        inside_video.setOnPreparedListener(
//                new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {

        // Hide buffering message.
        // mBufferingTextView.setVisibility(VideoView.INVISIBLE);

        // Restore saved position, if available.
//                        if (mCurrentPosition > 0) {
//                            inside_video.seekTo(mCurrentPosition);
//                        } else {
        // Skipping to 1 shows the first frame of the video.
//                            inside_video.seekTo(1);
//                        }

        // Start playing!
//                        inside_video.start();
//                    }
//                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
//        inside_video.setOnCompletionListener(
//                new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        Toast.makeText(EditProfileActivity.this,
//                                "Done",
//                                Toast.LENGTH_SHORT).show();

        // Return the video position to the start.
//                        inside_video.seekTo(0);
//                    }
//                });
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
//    private void releasePlayer() {
//        inside_video.stopPlayback();
//    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
//    private Uri getMedia(String mediaName) {
//        if (URLUtil.isValidUrl(mediaName)) {
//             Media name is an external URL.
//            return Uri.parse(mediaName);
//        } else {
//             Media name is a raw resource embedded in the app.
//            return Uri.parse("android.resource://" + getPackageName() +
//                    "/raw/" + mediaName);
//        }
//    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////


    //video trimmer.........
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
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write external storage");
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
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void selectVideoDialog() {
        new VideoPicker(this) {
            @Override
            protected void onCameraClicked() {
                openVideoCapture();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void onGalleryClicked() {
                Intent intent = new Intent();
                intent.setTypeAndNormalize("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Selet Video"), REQUEST_VIDEO_TRIMMER);
            }
        }.show();
    }

    private void openVideoCapture() {
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoCapture, REQUEST_VIDEO_TRIMMER);
    }

    private void startTrimActivity( Uri uri) {
        Intent intent = new Intent(this, VideoTrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivityForResult(intent, REQUEST_VIDEO_TRIMMER_RESULT);
    }

    private File getFileFromBitmap(Bitmap bmp) {
        /*//create a file to write bitmap data*/
        thumbFile = new File(this.getCacheDir(), "thumb_" + selectedVideoName + ".png");
        try {
            thumbFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*//Convert bitmap to byte array*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        /*//write the bytes in file*/
        try {
            FileOutputStream fos = new FileOutputStream(thumbFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_VIDEO_TRIMMER:
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startTrimActivity(selectedUri);
                    } else {
//                        showToastShort(getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                    break;
                case REQUEST_VIDEO_TRIMMER_RESULT:
                      selectedVideoUri = data.getData();

                    if (selectedVideoUri != null) {
                        selectedVideoFile = data.getData().getPath();
                        selectedVideoName = data.getData().getLastPathSegment();
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoUri.getPath(),
                                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                        Glide.with(this)
                                .load(getFileFromBitmap(thumb))
                                .apply(RequestOptions.circleCropTransform())
                                //.into(inside_video);
                                .into(inside_video);

//                        String udhdhdh=selectedVideoUri.toString();
//                        video_view.setVideoPath(udhdhdh);

                    } else {
//                        showToastShort(getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                    break;
            }
        }
    }
}






