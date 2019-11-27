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

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.Comparator;

import tstu.tlc.tstutwitterclient.adapter.FriendsAdapter;

public class FriendsActivity extends AppCompatActivity {

    private VKClient vkClient;
    private RelativeLayout layout;
    private RecyclerView tweetsRecyclerView;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        layout = findViewById(R.id.frinds_layout);
        initRecyclerView();

        vkClient = TSTUApplication.getVkClient();

        setTitle("Список друзей");

        // ПОЛУЧАЕМ СПИСОК ДРУЗЕЙ
        vkClient.getFriendsList(new VKRequest.VKRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray array = new VKUsersArray();
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
                Log.d("TSTU","REQUEST FRIEND ERROR");
            }
        });
    }

    private void initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendsAdapter = new FriendsAdapter(new FriendsAdapter.UserClickListener() {
            @Override
            public void OnClick(VKApiUserFull userFull) {
                Intent intent = new Intent (FriendsActivity.this, UserWall.class);
                intent.putExtra("userId",userFull.id);
                startActivity(intent);
            }
        });
        tweetsRecyclerView.setAdapter(friendsAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OnFriendsListResponded(VKUsersArray usersArray)  {
        if (usersArray != null) Log.d("TSTU","GET " + usersArray.size() + " friends");

        usersArray.sort(new LexicographicComparator());
        friendsAdapter.setItems(usersArray);
    }

    class LexicographicComparator implements Comparator<VKApiUserFull> {
        @Override
        public int compare(VKApiUserFull a, VKApiUserFull b) {
            return a.first_name.compareToIgnoreCase(b.first_name);
        }
    }
}
