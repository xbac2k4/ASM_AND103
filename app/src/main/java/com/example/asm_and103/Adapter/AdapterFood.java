package com.example.asm_and103.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_and103.Model.Food;
import com.example.asm_and103.R;

import java.util.ArrayList;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.ViewHolder> {
    private Context context;
    private ArrayList<Food> list;
    public AdapterFood(Context context, ArrayList<Food> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Glide.with(context)
                .load(list.get(i).getImage())
                .centerCrop()
                .into(holder.imgAvatar);
        holder.tvID.setText("ID: " + list.get(i).getId());
        holder.tvName.setText("Name: " + list.get(i).getName());
        holder.tvPrice.setText("Price: $" + list.get(i).getPrice());
        holder.tvEvaluate.setText("Quantity: " + list.get(i).getQuantity());
        holder.tvDescribe.setText("Describe: " + list.get(i).getDescribe());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvPrice, tvEvaluate, tvDescribe;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = (TextView) itemView.findViewById(R.id.tv_id);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvEvaluate = (TextView) itemView.findViewById(R.id.tv_evaluate);
            tvDescribe = (TextView) itemView.findViewById(R.id.tv_describe);
        }
    }
}
