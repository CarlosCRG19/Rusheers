package com.hackmty;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.material.textfield.TextInputLayout;
import com.hackmty.Controllers.ImagesController;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.parse.SaveCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;
import static com.hackmty.Controllers.ActionsController.setUserValues;
import static com.hackmty.Controllers.ActionsController.validateField;
import static com.hackmty.Controllers.CustomAlertDialogs.errorDialog;
import static com.hackmty.Controllers.CustomAlertDialogs.loadingDialog;
import static com.hackmty.Controllers.CustomAlertDialogs.successDialog;
import static com.hackmty.Controllers.ImagesController.loadFileImage;
import static com.hackmty.Controllers.ImagesController.loadTakenImage;
import static com.hackmty.Controllers.PermissionsController.checkWriteExternalPermission;

/**
 * This class is in charge of creating a new user in the application and saving it in the database.
 */
public class SignUpActivity extends AppCompatActivity {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    public static final String TAG = "SignUpActivity";
    private File photoFile;
    private String photoFileName = "photo.jpg";

    private TextInputLayout tilName, tilUsername, tilDescription, tilEmail, tilPassword;
    private EditText etName, etUsername, etDescription, etEmail, etPassword;
    private ImageView ivProfileImage;
    private Button btnSignUp;
    private String placeId, placeName, placeAddress, generalLocation;
    private Double placeLat, placeLng;
    private SweetAlertDialog loadingDialog, successDialog, errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get fields from view
        tilName = findViewById(R.id.tilName);
        tilUsername = findViewById(R.id.tilUsername);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnSignUp = findViewById(R.id.btnSignUp);
        generalLocation = "";

        RequestOptions circleProp = new RequestOptions();
        circleProp = circleProp.transform(new CircleCrop());
        Glide.with(getBaseContext())
                .load(R.drawable.profile_image_empty)
                .apply(circleProp)
                .into(ivProfileImage);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(SignUpActivity.this);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*check if all fields are filled, displaying an error message bellow every one if
                it's not filled.*/
                int count = 0;
                if (validateField(tilName, etName))
                    count++;
                if (validateField(tilUsername, etUsername))
                    count++;
                if (validateField(tilPassword, etPassword))
                    count++;
                if (validateField(tilEmail, etEmail))
                    count++;
                if (count == 4) //If all the fields were filled...
                    preCreateUser();
                else {
                    //If not all the fields were filled, an error dialog is shown.
                    errorDialog = errorDialog(SignUpActivity.this, "Please verify that all the fields are filled");
                    errorDialog.show();
                }
            }
        });
    }

    /**
     * This function opens an AlertDialogFragment with the options that the user has to set their profile picture.
     *
     * @param context
     */
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                String value = options[item].toString();
                switch (value) {
                    case "Take Photo":
                        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        // Create a File reference for future access
                        photoFile = ImagesController.getPhotoFileUri(photoFileName, TAG, SignUpActivity.this);
                        Uri fileProvider = FileProvider.getUriForFile(SignUpActivity.this, "com.codepath.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                        // So as long as the result is not null, it's safe to use the intent.
                        if (takePictureIntent.resolveActivity(SignUpActivity.this.getPackageManager()) != null) {
                            // Start the image capture intent to take photo
                            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                        break;
                    case "Choose from Gallery":
                        if (checkWriteExternalPermission(getApplicationContext()))
                            PickPhoto();
                        else
                            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        break;
                    case "Cancel":
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Calls an implicit intent to the camera roll, where the user can pick a photo.
     */
    private void PickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    /**
     * Called when the user clicks the SignUp button. Creates a ParseFile for the photo and goes to CreateAccount.
     */
    private void preCreateUser() {
        //Creates a SweetAlertDialog object of loading type, assigns it to loadingDialog and shows it.
        loadingDialog = loadingDialog(SignUpActivity.this);
        loadingDialog.show();

        if (photoFile != null) {
            //saves the photo in parse, if the save is successful, the CreateAccount function is called.
            ParseFile photo = new ParseFile(photoFile);
            photo.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error while saving" + e, Toast.LENGTH_SHORT).show();
                        return;
                    } else
                        CreateAccount(photo);
                }
            });
        } else
            CreateAccount(null);
    }

    /**
     * Creates the user and sends it to the Parse Database
     *
     * @param photo profile picture
     */
    private void CreateAccount(ParseFile photo) {
        ParseUser user = new ParseUser();
        setUserValues(user, etName.getText().toString(), etUsername.getText().toString(),
                etPassword.getText().toString(), etEmail.getText().toString(), photoFile, photo);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                //dismisses the loadingDialog
                loadingDialog.dismissWithAnimation();
                if (e != null) {
                    //creates an errorDialog showing the error.
                    errorDialog = errorDialog(SignUpActivity.this, e.getMessage());
                    errorDialog.show();
                } else {
                    //creates an successDialog showing the confirmation.
                    successDialog = successDialog(SignUpActivity.this, "Signed up successfully");
                    successDialog.show();
                    successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                            successDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    /**
     * Called when the user selects a Photo from their Gallery or after taking a new one with the camera.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: //TAKE PICTURE
                    if (resultCode == RESULT_OK) { //If the image took the picture..
                        // by this point we have the camera photo on disk
                        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        // with this we are sure of the correct orientation.
                        takenImage = ImagesController.rotateBitmapOrientation(photoFile.getAbsolutePath());
                        // Load the taken image into a preview
                        loadTakenImage(takenImage, ivProfileImage, getApplicationContext());
                    } else { // Result was a failure
                        Toast.makeText(SignUpActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case SELECT_IMAGE_ACTIVITY_REQUEST_CODE: //CHOOSE PICTURE
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                photoFile = new File(picturePath);
                                loadFileImage(photoFile, ivProfileImage, getApplicationContext());
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * This function is called when the user accepted or rejected the permission of writing in the
     * external storage.
     *
     * @param requestCode  permission's request code
     * @param permissions  permissions requested
     * @param grantResults result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            PickPhoto();
        else
            Toast.makeText(SignUpActivity.this, "Permission DENIED!", Toast.LENGTH_SHORT).show();
    }
}
