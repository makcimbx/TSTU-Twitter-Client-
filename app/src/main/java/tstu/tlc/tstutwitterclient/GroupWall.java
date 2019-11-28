package tstu.tlc.tstutwitterclient;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiCommunityFull;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;

import tstu.tlc.tstutwitterclient.adapter.WallAdapter;

public class GroupWall extends AppCompatActivity {

    private VKClient vkClient;
    private VKApiCommunityFull fullGroup;
    private RecyclerView tweetsRecyclerView;
    private WallAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_wall);


        vkClient = TSTUApplication.getVkClient();

        setTitle("Страница группы");

        Intent intent = getIntent();
        int groupId  = (int)intent.getExtras().get("groupId");

        vkClient.getGroupById(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiCommunityFull> array = new VKList<VKApiCommunityFull>();
                Log.d("TSTU", response.json.toString());
                try {
                    array.parse(response.json);

                }
                catch (Exception e) {
                    Log.d("TSTU","PARSE EXCEPTION");
                    array = null;
                }
                OnFriendsListResponded(array.get(0));
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

                Log.d("TSTU","USER DOWNLOAD ERROR");
            }
        }, String.valueOf(groupId), "members_count");
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

    private void OnFriendsListResponded(VKApiCommunityFull usersArray)  {

//        friendsAdapter.setItems(usersArray);
    }
}
