package com.example.truongle.btlandroid_appraoban.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.Chat.model.Message;
import com.example.truongle.btlandroid_appraoban.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by truongle on 25/04/2017.
 */

public class ChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<Message> list;

    public ChatAdapter(Context context, ArrayList<Message> list) {
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
        view = inflater.inflate( R.layout.chat_row,null);

        TextView txtMess = (TextView) view.findViewById(R.id.textViewMessage);

        Message message = list.get(position);
        boolean POSITION = message.isPosition();
        textChatColor(txtMess,POSITION);

        String current_date = getDate();
        if(current_date.equals(message.getDate()))
              txtMess.setText(message.getMess()+"\n\t"+message.getTime());
        else
            txtMess.setText(message.getMess()+"\n\t"+message.getDate());


        return view;
    }
    public String getDate(){
        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);
    }

    public void textChatColor(TextView txtMess, boolean POSITION){
        txtMess.setBackgroundResource(POSITION ? R.drawable.bubble_left_gray : R.drawable.bubble_right_green);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if(!POSITION)
        {
            params.gravity = Gravity.RIGHT;
        }
        else
        {
            params.gravity = Gravity.LEFT;
        }
        txtMess.setLayoutParams(params);
    }
}
