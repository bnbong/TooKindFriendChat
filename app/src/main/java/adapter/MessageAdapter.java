package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tookindfriendchat.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Message;

public class MessageAdapter extends BaseAdapter {
    ArrayList<Message> messageItems;
    LayoutInflater layoutInflater;

    public MessageAdapter(ArrayList<Message> messageItems, LayoutInflater layoutInflater) {
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Message item = messageItems.get(position);

        if (getItemViewType(position) == 0) {
            view = layoutInflater.inflate(R.layout.chat_item_me, null);
        } else {
            view = layoutInflater.inflate(R.layout.chat_item_friend, null);
        }

        CircleImageView iv;
        TextView tvName, tvMsg, tvTime;

        if (item.getSentBy().equals(Message.SENT_BY_ME)) {
            iv = view.findViewById(R.id.me_iv);
            tvName = view.findViewById(R.id.me_tv_name);
            tvMsg = view.findViewById(R.id.me_tv_msg);
            tvTime = view.findViewById(R.id.me_tv_time);
        } else {
            iv = view.findViewById(R.id.friend_iv);
            tvName = view.findViewById(R.id.friend_tv_name);
            tvMsg = view.findViewById(R.id.friend_tv_msg);
            tvTime = view.findViewById(R.id.friend_tv_time);
        }

        tvName.setText(item.getName());
        tvMsg.setText(item.getMessage());
        tvTime.setText(item.getTime());

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageItems.get(position).getSentBy().equals(Message.SENT_BY_ME)) {
            return 0; // for chat_item_me
        } else {
            return 1; // for chat_item_friend
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // because we have two types of views
    }
}
