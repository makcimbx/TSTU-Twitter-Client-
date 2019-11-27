package tstu.tlc.tstutwitterclient.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tstu.tlc.tstutwitterclient.R;
import tstu.tlc.tstutwitterclient.TSTUApplication;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private ArrayList<VKApiDialog> tweetList = new ArrayList<VKApiDialog>();

    private UserClickListener userClickListener;
    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class MessagesViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private CircleImageView userImageView;
        private CircleImageView userOnlineView;
        private TextView nameTextView;
        private TextView lastMessageTextView;
        private RelativeLayout relativeLayout;
        private VKApiDialog userFull;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public MessagesViewHolder(View itemView, UserClickListener listener) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.message_imageview_user);
            nameTextView = itemView.findViewById(R.id.message_text_view_name);
            lastMessageTextView = itemView.findViewById(R.id.message_text_view_last_message);
            relativeLayout = itemView.findViewById(R.id.message_element_layout);
            userOnlineView = itemView.findViewById(R.id.message_online_user);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClick(userFull);
                }
            });
        }

        public void bind(VKApiDialog userFull) {
            this.userFull = userFull;

            //nameTextView.setText(userFull.first_name + " " + userFull.last_name);

            //String tweetPhotoUrl = userFull.photo_100;
//            Picasso.with(itemView.getContext()).load(tweetPhotoUrl).into(userImageView);
            //Glide.with(itemView.getContext()).load(tweetPhotoUrl).into(userImageView);

            Drawable onlineColor = new ColorDrawable(ContextCompat.getColor(TSTUApplication.getAppContext(), R.color.colorOnline));
            Drawable offlineColor = new ColorDrawable(ContextCompat.getColor(TSTUApplication.getAppContext(), R.color.colorOffline));
//            LayerDrawable ld = new LayerDrawable(new Drawable[]{userFull.online ? onlineColor : offlineColor});
            //userOnlineView.setImageDrawable(userFull.online ? onlineColor : offlineColor);
        }
    }

    public void setItems(VKList<VKApiDialog> users) {
        tweetList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    public MessagesAdapter(UserClickListener listener) {
        userClickListener = listener;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_element, parent, false);
        return new MessagesViewHolder(view,userClickListener);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        holder.bind(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public interface UserClickListener {
        void OnClick(VKApiDialog userFull);
    }
}