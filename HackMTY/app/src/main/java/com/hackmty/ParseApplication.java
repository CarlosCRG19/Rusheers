package com.hackmty;

import android.app.Application;

import com.hackmty.models.Classe;
import com.hackmty.models.Message;
import com.hackmty.models.Room;
import com.parse.BuildConfig;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        ParseObject.registerSubclass(Classe.class);
        ParseObject.registerSubclass(Room.class);
        ParseObject.registerSubclass(Message.class);

        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("1FTMAcVDP8yXabW7p9ZRUSNt1lhLh1W0Wy7r1yFT")
                .clientKey("G1YjKYl9FwWipzjxqFIThkcgIERLVFBM4wQEtagJ")
                .server("https://parseapi.back4app.com")
                .enableLocalDataStore()
                .build());
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
