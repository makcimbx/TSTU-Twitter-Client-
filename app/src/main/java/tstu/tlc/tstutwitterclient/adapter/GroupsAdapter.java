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

import com.bumptech.glide.Glide;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKApiCommunityFull;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tstu.tlc.tstutwitterclient.R;
import tstu.tlc.tstutwitterclient.TSTUApplication;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {

    private ArrayList<VKApiCommunity> tweetList = new ArrayList<VKApiCommunity>();

    private UserClickListener userClickListener;
    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class GroupsViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private CircleImageView userImageView;
        private TextView nameTextView;
        private TextView lastMessageTextView;
        private RelativeLayout relativeLayout;
        private VKApiCommunity userFull;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public GroupsViewHolder(View itemView, UserClickListener listener) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.group_imageview_user);
            nameTextView = itemView.findViewById(R.id.group_text_view_name);
            lastMessageTextView = itemView.findViewById(R.id.group_text_view_last_message);
            relativeLayout = itemView.findViewById(R.id.group_element_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClick(userFull);
                }
            });
        }

        public void bind(VKApiCommunity userFull) {
            this.userFull = userFull;

            nameTextView.setText(userFull.name);
            lastMessageTextView.setText(userFull.is_closed == 1 ? "Закрытое сообщество" : "Открытое сообщество");

            String tweetPhotoUrl = userFull.photo_100;
            Glide.with(itemView.getContext()).load(tweetPhotoUrl).into(userImageView);

        }
    }

    public void setItems(VKApiCommunityArray users) {
        tweetList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    public GroupsAdapter(UserClickListener listener) {
        userClickListener = listener;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_element, parent, false);
        return new GroupsViewHolder(view,userClickListener);
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        holder.bind(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public interface UserClickListener {
        void OnClick(VKApiCommunity userFull);
    }
}