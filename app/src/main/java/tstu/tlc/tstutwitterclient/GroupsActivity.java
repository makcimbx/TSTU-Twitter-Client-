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
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import tstu.tlc.tstutwitterclient.adapter.GroupsAdapter;
import tstu.tlc.tstutwitterclient.adapter.MessagesAdapter;

public class GroupsActivity extends AppCompatActivity {

    private VKClient vkClient;
    private RecyclerView tweetsRecyclerView;
    private GroupsAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        initRecyclerView();

        vkClient = TSTUApplication.getVkClient();

        vkClient.getUserGroups(new VKRequest.VKRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiCommunityArray array = new VKApiCommunityArray();
                Log.d("TSTU", response.json.toString());
                try {
                    array.parse(response.json);

                }
                catch (Exception e) {
                    Log.d("TSTU","GROUPS PARSE EXCEPTION");
                    array = null;
                }

                OnDialogListResponded(array);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("TSTU","REQUEST MESSAGES ERROR");
            }
        });
    }

    private void initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.group_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new GroupsAdapter(new GroupsAdapter.UserClickListener() {
            @Override
            public void OnClick(VKApiCommunity userFull) {
//                Intent intent = new Intent (GroupsActivity.this, UserWall.class);
//                intent.putExtra("userId",userFull.getId());
//                startActivity(intent);
            }
        });
        tweetsRecyclerView.setAdapter(messagesAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OnDialogListResponded(VKApiCommunityArray usersArray)  {
        if (usersArray != null) Log.d("TSTU","GET " + usersArray.size() + " groups");

        messagesAdapter.setItems(usersArray);
    }
}
