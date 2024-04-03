package com.example.carassistant.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.data.models.Expense;
import com.example.carassistant.R;
import com.example.carassistant.databinding.DiagramItemBinding;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.Set;

public class DiagramAdapter extends RecyclerView.Adapter<DiagramAdapter.DiagramViewHolder> {


    private final HashMap<String, Integer> map;
    private final Set<String> keys;
    public DiagramAdapter(ArrayList<Expense> data) {
        map = new HashMap<>();
        for (Expense i: data
        ) {
            if (map.containsKey(i.getCategory()))
                map.put(i.getCategory(), map.get(i.getCategory()) + Integer.parseInt(i.getExpense()));
            else
                map.put(i.getCategory(), Integer.parseInt(i.getExpense()));
        }
        keys = map.keySet();


        Log.d("CarAssWork", keys.toString());
    }


    @NonNull
    @Override
    public DiagramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiagramItemBinding binding = DiagramItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new DiagramViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagramAdapter.DiagramViewHolder holder, int position) {
        String local_key  = (String) keys.toArray()[position];
        switch (local_key){
            case "Топливо":
                holder.binding.iconCategory.setImageResource(R.drawable.gasoline_pump);
                break;
            case "Запчасти":
                holder.binding.iconCategory.setImageResource(R.drawable.spare_parts);
                break;
            case "Шины":
                holder.binding.iconCategory.setImageResource(R.drawable.tire);
                break;
            case "Диски":
                holder.binding.iconCategory.setImageResource(R.drawable.rim);
                break;
            case "Работа сервиса":
                holder.binding.iconCategory.setImageResource(R.drawable.mechanical);
                break;
            case "Автомойка":
                holder.binding.iconCategory.setImageResource(R.drawable.car_wash);
                break;
            case "Другое":
                holder.binding.iconCategory.setImageResource(R.drawable.question);
                break;
        }

        holder.binding.tvCategory.setText(local_key);

        holder.binding.tvCost.setText(String.valueOf(map.get(local_key)));
    }

    @Override
    public int getItemCount() {return map.size();}

    public static class DiagramViewHolder extends RecyclerView.ViewHolder {
        DiagramItemBinding binding;
        public DiagramViewHolder(@NonNull DiagramItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
