package com.example.truongle.btlandroid_appraoban.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.Chat.model.UserContact;
import com.example.truongle.btlandroid_appraoban.Chat.view.ChatFrame;
import com.example.truongle.btlandroid_appraoban.R;

import java.util.ArrayList;

/**
 * Created by truongle on 06/05/2017.
 */

public class UserContactAdapter extends BaseAdapter {
    Activity context;
    ArrayList<UserContact> list;

    public UserContactAdapter(Activity context, ArrayList<UserContact> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.usercontact_row, null);


        final UserContact contact = list.get(position);
        ViewHolder holder = new ViewHolder(view);
        holder.setName(contact.getName());
        holder.setLastMessage(contact.getLastMessage());
        holder.setImage(R.drawable.user_icon);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatFrame chatFrame = new ChatFrame(context);
                chatFrame.Chat(contact.getFrom(),contact.getTo());



            }
        });
        return view;
    }
    public class ViewHolder{
        View view;

        public ViewHolder(View view) {
            this.view = view;
        }
        public void setName(String name){
            TextView txtName = (TextView) view.findViewById(R.id.textViewNameUser);
            txtName.setText(name);
        }
        public void setLastMessage(String lastMessage){
            TextView txtLastMessage = (TextView) view.findViewById(R.id.textViewLastMess);
            txtLastMessage.setText(lastMessage);
        }
        public void setImage(int image){
            ImageView img = (ImageView) view.findViewById(R.id.imageViewUser);
            img.setImageResource(image);
        }
    }
}
