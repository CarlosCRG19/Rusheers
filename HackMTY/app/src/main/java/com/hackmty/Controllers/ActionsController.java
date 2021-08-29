package com.hackmty.Controllers;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.hackmty.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class ActionsController {
    /**
     * This functions manages so see if the EditText is empty or not.
     * @param textInputLayout
     * @param editText
     * @return
     */
    public static boolean validateField(TextInputLayout textInputLayout, EditText editText) {
        if(editText.getText().toString().isEmpty()) {
            textInputLayout.setError("Field can't be empty");
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    /**
     * Assigns the required values to a user, either to create a new one or edit an existing one.
     * @param user ParseUser
     * @param name user's name
     * @param username user's username
     * @param password user's password
     * @param email user's email
     * @param photoFile user's profile photoFile
     * @param photo user's profile photo
     */
    public static void setUserValues(ParseUser user, String name, String username, String password,
                                     String email, File photoFile, ParseFile photo) {
        user.put(User.KEY_NAME, name);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if (photoFile != null)
            user.put(User.KEY_PFP, photo);
    }
}
