package com.hackmty.Controllers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * This class is in charge of verifying if a permission has already been granted in the app.
 */
public class PermissionsController {

    /**
     * Verifies is the user has already accepted the write external permission, returns true or false
     * depending the case.
     * @param context
     */
    public static boolean checkWriteExternalPermission(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            return false;
        else
            return true;
    }
}
