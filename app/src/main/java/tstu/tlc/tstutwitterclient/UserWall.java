package tstu.tlc.tstutwitterclient;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.TextInputEditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import org.json.JSONException;

public class UserWall extends AppCompatActivity {
    VKClient vkClient;

    private ImageView userImage;
    private TextView userName;
    private TextView userOnline;
    private TextView userAbout;
    private TextView userAges;
    private TextView userSex;
    private TextView userCity;

    private Button toMessagesButton;
    private TextInputEditText userPostInput;
    private Button toPostButton;
    private Button toWallButton;

    private VKApiUserFull filledUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);
        userName = findViewById(R.id.user_wall_name);
        userImage = findViewById(R.id.user_wall_image_user);
        userOnline = findViewById(R.id.user_wall_online_text);
        userAbout = findViewById(R.id.user_wall_about);
        userAges = findViewById(R.id.user_wall_ages);
        userSex = findViewById(R.id.user_wall_sex);
        userCity = findViewById(R.id.user_wall_country_city);

        toMessagesButton = findViewById(R.id.user_wall_to_messages);
        userPostInput = findViewById(R.id.user_wall_post_input);
        toPostButton = findViewById(R.id.user_wall_post);
        toWallButton = findViewById(R.id.user_wall_go);

        vkClient = TSTUApplication.getVkClient();

        setTitle("Страница профиля");

        Intent intent = getIntent();
        int userId  = (int)intent.getExtras().get("userId");

        vkClient.getUserById(new VKRequest.VKRequestListener() {
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
                fillPage(array.get(0));
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

                Log.d("TSTU","USER DOWNLOAD ERROR");
            }
        }, userId, "photo_400_orig","online","about","bdate","city","country","sex");


    }

    private void fillPage(VKApiUserFull user)
    {
        filledUser = user;

        userName.setText(user.first_name + " " + user.last_name);
        userOnline.setText(user.online ? "online" : "offline");
        userAbout.setText(user.about);
        userAges.setText(user.bdate.split(".").length < 3 ? "День рождения скрыт"  : user.bdate);
        userSex.setText(user.sex == 1 ? "Женский пол" : "Мужской пол");
        userCity.setText("Страна " + user.country + " город " + user.city);

        String tweetPhotoUrl = user.photo_400_orig;
        Glide.with(userImage.getContext()).load(tweetPhotoUrl).into(userImage);
//        Glide.with(userImage.getContext()).load(tweetPhotoUrl).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                int w = resource.getIntrinsicHeight();
//                int h = resource.getIntrinsicWidth();
//                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, userImage.getWidth(), (userImage.getWidth() / w) * h, true));
//                ViewGroup.LayoutParams params = userImage.getLayoutParams();
//                params.width = userImage.getWidth();
//                params.height = (userImage.getWidth() / w) * h;
//                Log.d("TSTU", "WIDTH: " + userImage.getWidth() + "; HEIGHT: " + ((userImage.getWidth() / w) * h));
//                userImage.setLayoutParams(params);
//                userImage.setImageDrawable(d);
//
//                Log.d("TSTU", "WIDTH: " + userImage.getWidth() + "; HEIGHT: " + userImage.getHeight());
//            }
//        });


        toPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vkClient.postOnWall(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                    }
                }, String.valueOf(filledUser.id),userPostInput.getText().toString());
            }
        });

        toWallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (UserWall.this, wall_posts.class);
                intent.putExtra("userId",user.id);
                startActivity(intent);
            }
        });
    }
}
