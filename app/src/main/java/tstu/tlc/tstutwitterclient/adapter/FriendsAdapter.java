package tstu.tlc.tstutwitterclient.adapter;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;
import tstu.tlc.tstutwitterclient.FriendsActivity;
import tstu.tlc.tstutwitterclient.R;
import tstu.tlc.tstutwitterclient.TSTUApplication;
import tstu.tlc.tstutwitterclient.UserWall;
import tstu.tlc.tstutwitterclient.VKClient;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private ArrayList<VKApiUserFull> tweetList = new ArrayList<>();

    private UserClickListener userClickListener;
    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class FriendsViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private CircleImageView userImageView;
        private CircleImageView userOnlineView;
        private TextView nameTextView;
        private RelativeLayout relativeLayout;
        private VKApiUserFull userFull;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public FriendsViewHolder(View itemView, UserClickListener listener) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.imageview_user);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            relativeLayout = itemView.findViewById(R.id.user_element_layout);
            userOnlineView = itemView.findViewById(R.id.online_user);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClick(userFull);
                }
            });
        }

        public void bind(VKApiUserFull userFull) {
            this.userFull = userFull;

            nameTextView.setText(userFull.first_name + " " + userFull.last_name);

            String tweetPhotoUrl = userFull.photo_100;
//            Picasso.with(itemView.getContext()).load(tweetPhotoUrl).into(userImageView);
            Glide.with(itemView.getContext()).load(tweetPhotoUrl).into(userImageView);

            Drawable onlineColor = new ColorDrawable(ContextCompat.getColor(TSTUApplication.getAppContext(), R.color.colorOnline));
            Drawable offlineColor = new ColorDrawable(ContextCompat.getColor(TSTUApplication.getAppContext(), R.color.colorOffline));
//            LayerDrawable ld = new LayerDrawable(new Drawable[]{userFull.online ? onlineColor : offlineColor});
            userOnlineView.setImageDrawable(userFull.online ? onlineColor : offlineColor);
        }
    }

    public void setItems(VKUsersArray users) {
        tweetList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    public FriendsAdapter(UserClickListener listener) {
        userClickListener = listener;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_element, parent, false);
        return new FriendsViewHolder(view,userClickListener);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.bind(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public interface UserClickListener {
        void OnClick(VKApiUserFull userFull);
    }
}