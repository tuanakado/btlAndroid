package com.example.truongle.btlandroid_appraoban.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.Chat.model.Message;
import com.example.truongle.btlandroid_appraoban.R;

import java.util.ArrayList;

/**
 * Created by truongle on 25/04/2017.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Message> list;
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

        TextView txtnName = (TextView) view.findViewById(R.id.textViewNameUser);
        TextView txtMess = (TextView) view.findViewById(R.id.textViewLastMess);

        Message message = list.get(position);

        return view;
    }
}
