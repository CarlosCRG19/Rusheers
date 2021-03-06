package com.hackmty;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * This class is the responsible for opening the images in full screen and allowing the user
 * to zoom and scale them.
 */
public class FullSizeImageActivity extends AppCompatActivity {

    SubsamplingScaleImageView ivProfileImage;
    //ImageView ivProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get url of the photo clicked.
        String photoUrl = getIntent().getStringExtra("photoUrl");

        setContentView(R.layout.activity_full_size_image);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        //load the image with Glide into the SubsamplingScaleImageView slot.
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(photoUrl != null ? photoUrl : R.drawable.profile_image_empty)
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                ivProfileImage.setImage(ImageSource.bitmap(resource));
            }
        });
    }
}