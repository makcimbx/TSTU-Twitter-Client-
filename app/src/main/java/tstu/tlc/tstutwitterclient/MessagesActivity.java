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
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKNotesArray;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.Comparator;

import tstu.tlc.tstutwitterclient.adapter.FriendsAdapter;
import tstu.tlc.tstutwitterclient.adapter.MessagesAdapter;

public class MessagesActivity extends AppCompatActivity {

    private VKClient vkClient;
    private RecyclerView tweetsRecyclerView;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        initRecyclerView();

        vkClient = TSTUApplication.getVkClient();

        vkClient.getUserDialogs(new VKRequest.VKRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiDialog> array = new VKList<VKApiDialog>();
                Log.d("TSTU", response.json.toString());
                try {
                    array.parse(response.json);

                }
                catch (Exception e) {
                    Log.d("TSTU","PARSE EXCEPTION");
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
        tweetsRecyclerView = findViewById(R.id.messages_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new MessagesAdapter(new MessagesAdapter.UserClickListener() {
            @Override
            public void OnClick(VKApiDialog userFull) {
                Intent intent = new Intent (MessagesActivity.this, UserWall.class);
                intent.putExtra("userId",userFull.getId());
                startActivity(intent);
            }
        });
        tweetsRecyclerView.setAdapter(messagesAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OnDialogListResponded(VKList<VKApiDialog> usersArray)  {
        if (usersArray != null) Log.d("TSTU","GET " + usersArray.size() + " friends");

        messagesAdapter.setItems(usersArray);
    }
}
