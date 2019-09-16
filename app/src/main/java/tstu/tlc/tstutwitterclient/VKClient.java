package tstu.tlc.tstutwitterclient;

import android.app.Activity;

import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

public class VKClient {

    public VKClient()
    {
        VKSdk.initialize(TSTUApplication.getAppContext());
    }

    public void Login(Activity activity)
    {
        VKSdk.login(activity);
    }
}
