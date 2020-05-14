package com.example.truongle.btlandroid_appraoban.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.R;
import com.example.truongle.btlandroid_appraoban.SingleProductActivity;
import com.example.truongle.btlandroid_appraoban.model.DSSP;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by truongle on 11/05/2017.
 */

public class DsSpLuuAdapter extends BaseAdapter {
    Activity context;
    ArrayList<DSSP> list;

    public DsSpLuuAdapter(Activity context, ArrayList<DSSP> list) {
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
        view = inflater.inflate( R.layout.ds_luu_row,null);

        final DSSP sp  = list.get(position);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setName(sp.getName());
        viewHolder.setAddress(sp.getAddress());
        viewHolder.setImage(sp.getImage());
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("product_key", sp.getProduct_key());
                intent.putExtra("tab_value", sp.getCategory());
                context.startActivity(intent);
            }
        });
        return view;
    }
    public class ViewHolder{
        View view;

        public ViewHolder(View view) {
            this.view = view;
        }
        public void setImage (String image){
            ImageView img = (ImageView) view.findViewById(R.id.imageViewLuu);
            Picasso.with(context).load(image).into(img);
        }
        public void setName (String name){
            TextView txtName = (TextView) view.findViewById(R.id.textViewNameLuu);
            txtName.setText(name);
        }
        public void setAddress (String address){
            TextView txtAddress = (TextView) view.findViewById(R.id.textViewAddressLuu);
            txtAddress.setText(address);
        }
    }
}
