package com.example.carassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.databinding.ExpenseItemBinding;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {



    private final ArrayList<Expense> data;
    public ExpenseAdapter(ArrayList<Expense> data) {this.data = data;};

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpenseItemBinding binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        Expense item  = data.get(position);
        holder.binding.tvCategory.setText(item.getCategory());
        holder.binding.tvCost.setText(item.getExpense());


    }

    @Override
    public int getItemCount() {return data.size();}

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ExpenseItemBinding binding;
        public ExpenseViewHolder(@NonNull ExpenseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
