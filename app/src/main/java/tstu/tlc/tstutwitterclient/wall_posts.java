package tstu.tlc.tstutwitterclient;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.Comparator;

import tstu.tlc.tstutwitterclient.adapter.FriendsAdapter;
import tstu.tlc.tstutwitterclient.adapter.WallAdapter;

public class wall_posts extends AppCompatActivity {

    private VKClient vkClient;
    private RecyclerView tweetsRecyclerView;
    private WallAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_posts);

        initRecyclerView();

        Intent intent = getIntent();
        int userId  = (int)intent.getExtras().get("userId");

        vkClient = TSTUApplication.getVkClient();

        setTitle("Список постов");

        // ПОЛУЧАЕМ СПИСОК ПОСТОВ
        vkClient.getUserWall(new VKRequest.VKRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKPostArray array = new VKPostArray();
                Log.d("TSTU", response.json.toString());
                try {
                    array.parse(response.json);

                }
                catch (Exception e) {
                    Log.d("TSTU","PARSE EXCEPTION");
                    array = null;
                }

                OnFriendsListResponded(array);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("TSTU","REQUEST FRIEND ERROR" + error);
            }
        }, String.valueOf(userId));
    }

    private void initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.wall_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendsAdapter = new WallAdapter(new WallAdapter.WallClickListener() {
            @Override
            public void OnClick(VKApiPost userFull) {
//                Intent intent = new Intent (wall_posts.this, UserWall.class);
//                intent.putExtra("userId",userFull.id);
//                startActivity(intent);
            }
        });
        tweetsRecyclerView.setAdapter(friendsAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OnFriendsListResponded(VKPostArray usersArray)  {
        if (usersArray != null) Log.d("TSTU","GET " + usersArray.size() + " post");

        friendsAdapter.setItems(usersArray);
    }
}
