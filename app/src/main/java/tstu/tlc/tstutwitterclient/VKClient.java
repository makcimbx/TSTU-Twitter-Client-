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
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

import java.util.Locale;

public class VKClient {

    public VKClient(){ VKSdk.initialize(TSTUApplication.getAppContext()); }

    public void Login(Activity activity){ VKSdk.login(activity,"friends","photos","wall","groups","notifications", "photos"); }

    public boolean isLoggedIn() { return VKSdk.isLoggedIn(); }

    public boolean onVKLogin(int requestCode, int resultCode, Intent data, VKCallback<VKAccessToken> callback) {
        return VKSdk.onActivityResult(requestCode, resultCode, data,callback);
    }

    public void getCurrentUserID(VKRequest.VKRequestListener listener){
        VKRequest request = VKApi.users().get();
        request.executeWithListener(listener);
    }

    public void getFriendsList(VKRequest.VKRequestListener listener) {
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_100, contacts", "city", "online"));
        request.executeWithListener(listener);
    }

    public void getUserById(VKRequest.VKRequestListener listener, int userId, String... fields) {
        String field = "";
        for(String item: fields) {
            field += item + ",";
        }
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, userId, VKApiConst.FIELDS, field));
        request.executeWithListener(listener);
    }

    public void postOnWall(VKRequest.VKRequestListener listener,String userId, String field){
        VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, userId, VKApiConst.MESSAGE, field));
        request.attempts = 5;
        request.executeWithListener(listener);
    }

    public void getUserDialogs(VKRequest.VKRequestListener listener) {
        VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, "5"));
        request.attempts = 5;
        request.executeWithListener(listener);
    }

    public void getUserWall(VKRequest.VKRequestListener listener, String userId) {
        VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, userId, VKApiConst.COUNT, 100));
        request.attempts = 5;
        request.executeWithListener(listener);
    }

    public void getPhotoById(VKRequest.VKRequestListener listener, String userId, String photoId) {
        VKRequest request = new VKRequest("photos.getById", VKParameters.from(VKApiConst.PHOTOS, photoId));
        request.attempts = 5;
        request.executeWithListener(listener);
    }

    public void getCommentsById(VKRequest.VKRequestListener listener, String userId, String postId) {

    }
}
