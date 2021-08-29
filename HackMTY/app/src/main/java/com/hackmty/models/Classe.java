package com.hackmty.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Classe")
public class Classe extends ParseObject {

    public static final String TAG = "Class";
    public static final String KEY_NAME = "className";
    public static final String KEY_PROFESSORS = "professors";
    public static final String KEY_CODE = "code";
    public static final String KEY_DESCRIPTION = "description";

    public String getName() { return getString(KEY_NAME); }
    public void setName(String roomname) { put(KEY_NAME, roomname); }

    public List<String> getProfessors() { return getList(KEY_PROFESSORS); }
    public void setProfessors(List<String> professors) { put(KEY_PROFESSORS, professors); }

    public String getCode() { return getString(KEY_CODE); }
    public void setCode(String code) { put(KEY_CODE, code); }

    public String getDescription() { return getString(KEY_DESCRIPTION); }
    public void setDescription(String description) { put(KEY_DESCRIPTION, description); }

}