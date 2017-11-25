package com.livetyping.moydom;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ivan on 25.11.2017.
 */

public class App extends Application{
    private static App sInstance;

    public App() {
        sInstance = this;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
