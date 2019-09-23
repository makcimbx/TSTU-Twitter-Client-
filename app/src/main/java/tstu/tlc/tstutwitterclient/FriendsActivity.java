package tstu.tlc.tstutwitterclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v7.widget.AppCompatDrawableManager.get;

public class FriendsActivity extends AppCompatActivity {

    private VKClient vkClient;
    private RelativeLayout layout;
    private ListView lvMain;

    private ArrayList<HashMap<String, Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        layout = (RelativeLayout) findViewById(R.id.frinds_layout);
        lvMain = (ListView) findViewById(R.id.lvMain);

        vkClient = TSTUApplication.getVkClient();

        // ПОЛУЧАЕМ СПИСОК ДРУЗЕЙ
        vkClient.getFriendsList(new VKRequest.VKRequestListener() {
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

    private void OnFriendsListResponded(VKUsersArray usersArray)  {
        if (usersArray != null) Log.d("TSTU","GET " + usersArray.size() + " friends");

        // Упаковываем данные
        data = new ArrayList<>(usersArray.size());
        HashMap<String, Object> map;
        for (VKApiUserFull item: usersArray) {
            map = new HashMap<>();
            map.put("FirstName", item.first_name);
            ImageView view = new ImageView(this);
            Picasso.with(this)
                    .load(item.photo_50)
                    .resize(50,50).into(view);
            map.put("Icon",view);
            data.add(map);
        }

// Массив имен атрибутов, из которых будут читаться данные
        String[] from = {"FirstName", "Icon"};

// Массив идентификаторов компонентов, в которые будем вставлять данные
        int[] to = {R.id.text_view_phone, R.id.imageview_cat};

// создаем адаптер
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.user_element,
                from, to);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
    }
}
