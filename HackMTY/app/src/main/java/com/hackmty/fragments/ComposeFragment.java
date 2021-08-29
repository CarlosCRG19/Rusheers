package com.hackmty.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.hackmty.BitmapScaler;
import com.hackmty.R;
import com.hackmty.models.ClassRoom;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends DialogFragment {

    // MEMBER VARIABLES

    public static final String TAG = "ComposeFragment"; // TAG for log messages

    // CODES TO CALL MEDIA ACTIONS (TAKE PHOTO AND GET FROM CONTENT)
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    // VIEWS
    private ImageView ivNote;
    private Button btnCapture, btnSubmit;

    // MEDIA FILE VARIABLES
    File file;
    ParseFile photoFile;
    String photoFileName = "photo.jpg";

    // ROOM FOR THIS NOTE
    ClassRoom room;

    // Required empty public constructor
    public ComposeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get room from arguments
        room = (ClassRoom) getArguments().getParcelable(ClassRoom.TAG);

        // Set views from specified layout
        setViews(view);
        // Set listeners for different buttons
        setClickListeners();
    }

    // VIEWS METHODS

    // Gets views from layout
    private void  setViews(View view) {
        // Note photo placeholder
        ivNote = view.findViewById(R.id.ivNote);
        // Submit action
        btnCapture = view.findViewById(R.id.btnCapture);
        btnSubmit = view.findViewById(R.id.btnSubmit);

    }

    // Sets listeners for each button
    private void setClickListeners() {
        // Calls launchCamera to open camera and take a picture
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        // Checks that all the required fields aren't empty and saves the post in database
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if there is an image loaded
                if(photoFile == null || ivNote.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save the note in the room object
                saveNote(photoFile);
            }
        });
    }

    // SAVE (POST) METHODS

    // Creates adds note to the room object and saves it into database
    private void saveNote(ParseFile photoFile) {
        room.addNote(photoFile);
        // Save into database using background thread
        room.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Check for errors
                if(e != null) {
                    Toast.makeText(getContext(), "Error al publicar nota!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Notify user using a toast
                Toast.makeText(getContext(), "Nota publicada!", Toast.LENGTH_SHORT).show();
                dismiss();
                // Empty views for note
                ivNote.setImageResource(0);
            }
        });
    }

    // MEDIA METHODS (provided by CodePath)

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        file = getPhotoFileUri(photoFileName);
        // Set value for photoFile so it can be saved into database
        photoFile = new ParseFile(file);
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        // Start the image capture intent to take photo
        // TODO: intent.resolveActivity is null
        if(intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    // RESULT METHOD

    // Handles the result of both media actions (Pick from media or take photo)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) { // Handle Take Photo
            if (resultCode == RESULT_OK) {
                // By this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 500); // resize image using helper
                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                File resizedFile = getPhotoFileUri(photoFileName + "_resized");
                try {
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    // Write the bytes of the bitmap to file
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the taken image into a preview
                ivNote.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(getContext(), "No se tomó ninguna foto!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}