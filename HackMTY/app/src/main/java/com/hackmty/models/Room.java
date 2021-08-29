package com.hackmty.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Room")
public class Room extends ParseObject
{
    public static final String KEY_ID = "objectId";
    public static final String KEY_NAME = "roomname";
    public static final String KEY_HOST = "host";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PASSCODE = "passcode";
    public static final String KEY_CHAT = "chatEnabled";
    public static final String KEY_MEETING = "meetingUrl";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_USERS = "users";
    public static final String KEY_CLASSNAME = "classname";

    public String getId() { return getString(KEY_ID); }

    public String getName() { return getString(KEY_NAME); }
    public void setName(String roomname) { put(KEY_NAME, roomname); }

    public ParseUser getHost() { return getParseUser(KEY_HOST); }
    public void setHost(ParseUser host) { put(KEY_HOST, host); }

    public String getDescription() { return getString(KEY_DESCRIPTION); }
    public void setDescription(String description) { put(KEY_DESCRIPTION, description); }

    public String getPasscode() { return getString(KEY_PASSCODE); }
    public void setPasscode(String passcode) { put(KEY_PASSCODE, passcode); }

    public Boolean getChatEnabled() { return getBoolean(KEY_CHAT); }
    public void setChatEnabled(Boolean chat) { put(KEY_CHAT, chat); }

    public String getZoom() { return getString(KEY_MEETING); }
    public void setZoom(String meeting) { put(KEY_MEETING, meeting); }

    public List<String> getTags() { return getList(KEY_TAGS); }
    public void setTags(List<String> tags) { put(KEY_TAGS, tags); }

    public List<User> getUsers() { return getList(KEY_USERS); }
    public void setUsers(List<User> users) { put(KEY_USERS, users); }

    public ParseObject getClassname() { return getParseObject(KEY_CLASSNAME); }
    public void setClassname(ParseObject classname) { put(KEY_CLASSNAME, classname); }
}
