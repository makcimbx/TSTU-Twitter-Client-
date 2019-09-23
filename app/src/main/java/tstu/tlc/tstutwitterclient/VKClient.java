package tstu.tlc.tstutwitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

public class VKClient {

    public VKClient(){ VKSdk.initialize(TSTUApplication.getAppContext()); }

    public void Login(Activity activity){ VKSdk.login(activity,"friends","photos","wall","groups","notifications"); }

    public boolean isLoggedIn() { return VKSdk.isLoggedIn(); }

    public boolean onVKLogin(int requestCode, int resultCode, Intent data, VKCallback<VKAccessToken> callback) {
        return VKSdk.onActivityResult(requestCode, resultCode, data,callback);
    }

    public void getFriendsList(VKRequest.VKRequestListener listener) {
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50, contacts", "city", "nickname"));
        request.executeWithListener(listener);
    }
}
