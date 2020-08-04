package com.ahmettekin.instacloneparse;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {

    @Override
    public void onCreate() {

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wlMAVi6cjyiVulzQPNP66jKsAso7E3xxWyiNcZlH")
                .clientKey("TVaRBHIM3GnVuonCWICM8m3oH4lKmIFXN9h3clYf")
                .server("https://parseapi.back4app.com/")
                .build()

        );
        super.onCreate();
    }
}
