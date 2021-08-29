package com.hackmty;

import android.app.Application;

import com.hackmty.models.Classe;
import com.hackmty.models.Message;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.SchoolClass;
import com.parse.BuildConfig;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(SchoolClass.class);
        ParseObject.registerSubclass(ClassRoom.class);
        ParseObject.registerSubclass(Message.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("1FTMAcVDP8yXabW7p9ZRUSNt1lhLh1W0Wy7r1yFT")
                .clientKey("G1YjKYl9FwWipzjxqFIThkcgIERLVFBM4wQEtagJ")
                .server("https://parseapi.back4app.com")
                .enableLocalDataStore()
                .build());
    }
}
