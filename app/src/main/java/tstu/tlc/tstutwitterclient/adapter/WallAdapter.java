package tstu.tlc.tstutwitterclient.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;

import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tstu.tlc.tstutwitterclient.R;
import tstu.tlc.tstutwitterclient.TSTUApplication;
import tstu.tlc.tstutwitterclient.VKClient;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.WallViewHolder> {

    private ArrayList<VKApiPost> tweetList = new ArrayList<>();

    private WallClickListener userClickListener;
    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class WallViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private TextView someTextView;
        private ImageView wallImageView;
        private RelativeLayout relativeLayout;
        private VKApiPost post;
        private VKClient vkClient;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public WallViewHolder(View itemView, WallClickListener listener) {
            super(itemView);

            vkClient = TSTUApplication.getVkClient();
            someTextView = itemView.findViewById(R.id.wall_text_view_name);
            wallImageView = itemView.findViewById(R.id.wall_image_view);
            relativeLayout = itemView.findViewById(R.id.wall_element_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClick(post);
                }
            });
        }

        public void bind(VKApiPost post) {
            this.post = post;

            someTextView.setText(post.text);

            if(post.attachments.getCount() > 0 && post.attachments.get(0).getType() == VKAttachments.TYPE_PHOTO) {
                String tweetPhotoUrl =  post.attachments.get(0).toAttachmentString().toString().replace("photo", "");
                Log.d("TSTU", tweetPhotoUrl);
                vkClient.getPhotoById(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Log.d("TSTU" ,response.json.toString());
                        URL url = null;
                        VKApiPhoto photo = new VKApiPhoto();
                        try {
                            Log.d("TSTU" ,"FIRST");
                            photo.parse(response.json);
                            Log.d("TSTU" ,"SECOND");
                        }
                        catch (Exception e) {
                            Log.d("TSTU","PARSE EXCEPTION" + e);
                            photo = null;
                        }

                        Log.d("TSTU", photo.photo_604);

                        //Glide.with(wallImageView.getContext()).load(photo.photo_604).into(wallImageView);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Log.e("TSTU" ,"OSHIBKA PIZA " + error);
                    }
                }, String.valueOf(post.to_id) ,tweetPhotoUrl);

            }
        }
    }

    public void setItems(VKPostArray users) {
        tweetList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    public WallAdapter(WallClickListener listener) {
        userClickListener = listener;
    }

    @Override
    public WallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_element, parent, false);
        return new WallViewHolder(view,userClickListener);
    }

    @Override
    public void onBindViewHolder(WallViewHolder holder, int position) {
        holder.bind(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public interface WallClickListener {
        void OnClick(VKApiPost userFull);
    }
}