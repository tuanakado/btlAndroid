package com.example.truongle.btlandroid_appraoban;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by truongle on 06/04/2017.
 */

public class Tab1 extends Fragment {
    private RecyclerView mList;
    private DatabaseReference root;
    private SearchView searchView;
    private Spinner spnFilter;
    private ArrayList<String> filter ;
    private Query queryFilter ;//
    private Query querySearch ;
    private Query queryGetAll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1, container, false);

        root = FirebaseDatabase.getInstance().getReference().child("Product").child("Đồ Điện Tử");
        queryGetAll = root.orderByChild("name_lower").startAt("").endAt(""+"\uf8ff");
        mList = (RecyclerView) rootView.findViewById(R.id.LaptopList);
        searchView = (SearchView) rootView.findViewById(R.id.searchViewTab1);
        spnFilter = (Spinner) rootView.findViewById(R.id.spnFilter);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new GridLayoutManager(getContext(),2));
        InitSpinner();
        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                queryFilter = root.orderByChild("category").equalTo(filter.get(position));
                if(position == 0){
                    search(queryGetAll);
                }else
                    search(queryFilter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                querySearch = root.orderByChild("name_lower").startAt(search).endAt(search+"\uf8ff");
                search(querySearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    search(queryGetAll);
                return false;
            }
        });

        return rootView;
    }

    private void InitSpinner() {
        filter = new ArrayList<>();
        filter.add("--Tất cả--");
        filter.add("Laptop");
        filter.add("Điện Thoại");
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(adapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View view;

        public BlogViewHolder(View itemView) {

            super(itemView);
            view = itemView;
        }

        public void setName(String name) {
            TextView txtName = (TextView) view.findViewById(R.id.textViewLaptopName);
            txtName.setText(name);
        }

        public void setCost(String cost) {
            TextView txtCost = (TextView) view.findViewById(R.id.textViewCost);
            txtCost.setText(cost);
        }


        public void setImage(Context ctx, String image) {
            ImageView img_view = (ImageView) view.findViewById(R.id.imageViewLaptop);
            Picasso.with(ctx).load(image)
                    .placeholder(R.drawable.wait)
                    .into(img_view);
        }
    }
    public void search(Query query){
        FirebaseRecyclerAdapter<Product, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, BlogViewHolder>(
                Product.class,
                R.layout.tab1_row,
                BlogViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Product model, int position) {
                final String product_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getName());
                viewHolder.setCost(model.getCost());
                viewHolder.setImage(getContext(), model.getImage());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SingleProductActivity.class);
                        intent.putExtra("product_key", product_key);
                        intent.putExtra("tab_value", "Đồ Điện Tử");
                        startActivity(intent);
                    }
                });
            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }
}
