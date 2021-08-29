package com.hackmty;

import android.app.Application;

import com.hackmty.models.Message;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.SchoolClass;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

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
