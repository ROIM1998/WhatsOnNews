package com.java.whatsonnews;

import android.app.Application;
import com.orm.SugarContext;

public class WhatsOnNewsApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        SugarContext.terminate();
    }
}
