package tstu.tlc.tstutwitterclient;

import android.app.Application;
import android.content.Context;

public class TSTUApplication extends Application {
    private static Context context;
    private static VKClient vkClient;

    public void onCreate() {
        super.onCreate();
        TSTUApplication.context = getApplicationContext();
        vkClient = new VKClient();
    }

    public static Context getAppContext() {
        return TSTUApplication.context;
    }

    public static VKClient getVkClient() {
        return TSTUApplication.vkClient;
    }
}
