package com.example.asm_and103.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_and103.Model.Response;
import com.example.asm_and103.databinding.DialogAddBinding;
import com.example.asm_and103.databinding.ItemDistributorBinding;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.databinding.ItemDistributorBinding;
import com.example.asm_and103.service.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterDistributor extends RecyclerView.Adapter<AdapterDistributor.ViewHolder> {
    private ArrayList<Distributor> list;
    private Context context;
    private DistributorClick distributorClick;
    private HttpRequest httpRequest = new HttpRequest();

    public AdapterDistributor(ArrayList<Distributor> list, Context context, DistributorClick distributorClick) {
        this.list = list;
        this.context = context;
        this.distributorClick = distributorClick;

    }

    public interface DistributorClick {
        void delete(Distributor distributor);
        void edit(Distributor distributor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDistributorBinding binding = ItemDistributorBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Distributor distributor = list.get(position);
        holder.binding.tvId.setText("ID: " + distributor.get_id());
        holder.binding.tvName.setText("Name: " + distributor.getName());
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorClick.delete(distributor);
            }
        });

        holder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorClick.edit(distributor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDistributorBinding binding;
        public ViewHolder(ItemDistributorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
