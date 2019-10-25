package com.finports.took_pos_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName;
        TextView mProductVolume;
        TextView mProductPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductName = itemView.findViewById(R.id.productName);
            mProductVolume = itemView.findViewById(R.id.productVol);
            mProductPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    private ArrayList<RecyclerItem> recyclerItemArrayList;
    RecyclerAdapter(ArrayList<RecyclerItem> recyclerItemArrayList) {
        this.recyclerItemArrayList = recyclerItemArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.mProductName.setText(recyclerItemArrayList.get(position).productName);
        itemViewHolder.mProductVolume.setText(String.valueOf(recyclerItemArrayList.get(position).productVolume));
        itemViewHolder.mProductPrice.setText(recyclerItemArrayList.get(position).productPrice);
    }

    @Override
    public int getItemCount() {
        return recyclerItemArrayList.size();
    }
}
