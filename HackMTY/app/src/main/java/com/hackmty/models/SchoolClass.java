package com.hackmty.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("SchoolClass")
public class SchoolClass extends ParseObject {

    public static final String KEY_CLASS_NAME = "classname";

    public String getName(){
        return getString(KEY_CLASS_NAME);
    }

}
