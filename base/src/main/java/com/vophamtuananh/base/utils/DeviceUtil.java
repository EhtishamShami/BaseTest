package com.vophamtuananh.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class DeviceUtil {

    public static final int PERMISSION_CAMERA_REQUEST_CODE = 1001;
    public static final int PERMISSION_READ_EXTERNAL_REQUEST_CODE = 1002;
    public static final int PERMISSION_CALL_PHONE_REQUEST_CODE = 1003;
    public static final int PERMISSION_WRITE_STORAGE_REQUEST_CODE = 1004;

    public static final int CAMERA_REQUEST_CODE = 1011;
    public static final int GALLERY_REQUEST_CODE = 1012;

    public static boolean checkWriteStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkCameraPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkReadStoragePermision(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkPhonePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void openCamera(Activity activity, File tempFile) {
        if (checkCameraPermission(activity.getApplicationContext())) {
            camera(activity, tempFile);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    private static void camera(Activity activity, File tempFile) {
        Uri capturedFileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            capturedFileUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationContext().getPackageName() + ".provider", tempFile);
        } else {
            capturedFileUri = Uri.fromFile(tempFile);
        }
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(activity.getPackageManager()) != null) {
            takePhotoIntent.putExtra("return-data", true);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedFileUri);
            Intent chooserIntent = Intent.createChooser(takePhotoIntent, "Selection Photo");
            if (chooserIntent != null)
                activity.startActivityForResult(chooserIntent, CAMERA_REQUEST_CODE);
        }
    }

    public static void openGallery(Activity activity) {
        if (checkReadStoragePermision(activity.getApplicationContext())) {
            gallery(activity);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_REQUEST_CODE);
        }
    }

    private static void gallery(Activity activity) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        if (photoPickerIntent.resolveActivity(activity.getPackageManager()) != null) {
            photoPickerIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Selection Photo");
            if (chooserIntent != null)
                activity.startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    public static void callToPhoneNumber(Activity activity, String phoneNumber) {
        if (checkPhonePermission(activity.getApplicationContext())) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            activity.startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_PHONE_REQUEST_CODE);
        }
    }
}
