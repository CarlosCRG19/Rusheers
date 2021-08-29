package com.hackmty.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("SchoolClass")
public class SchoolClass extends ParseObject {

    public static final String KEY_CLASS_NAME = "classname";
    public static final String KEY_PROFESSORS = "professors";
    public static final String KEY_CODE = "code";
    public static final String KEY_IMAGE = "classImage";

    public String getName(){
        return getString(KEY_CLASS_NAME);
    }

    public List<String> getProfessors() { return getList(KEY_PROFESSORS); }

    public String getCode() { return getString(KEY_CODE); }

    public ParseFile getImage() { return (ParseFile) get("classImage"); }

}
