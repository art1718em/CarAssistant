package com.example.carassistant.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.databinding.CarItemBinding;
import com.example.carassistant.databinding.FragmentAccountBinding;
import com.example.carassistant.ui.view.AccountFragment;
import com.example.carassistant.ui.view.AddCarFragment;
import com.example.carassistant.ui.viewModels.AccountViewModel;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>{



    private final List<CarDto> data;
    private final FragmentAccountBinding binding;

    private int indexActiveCar;

    private final AccountViewModel accountViewModel;
    public CarAdapter(
            List<CarDto> data,
            int indexActiveCar,
            FragmentAccountBinding binding,
            AccountViewModel accountViewModel
    ) {
        this.data = data;
        this.binding=binding;
        this.indexActiveCar = indexActiveCar;
        this.accountViewModel = accountViewModel;
    }


    @NonNull
    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarItemBinding binding = CarItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CarAdapter.CarViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CarDto item  = data.get(position);
        holder.binding.tvMark.setText(item.getMark());
        holder.binding.tvModel.setText(item.getModel());
        holder.binding.tvColor.setText(item.getColor());

        if (position == indexActiveCar){
            int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_200);
            holder.binding.constraintLayoutCarItem.setBackgroundColor(color);
        } else {
            holder.binding.constraintLayoutCarItem.setBackgroundColor(android.R.color.white);
        }

        holder.binding.constraintLayoutCarItem.setOnClickListener(v -> {
            if (indexActiveCar != position){
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                accountViewModel.changeActiveCar(indexActiveCar, position);
            }
        });

        holder.binding.iconInformation.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(AddCarFragment.carIdKey, item.getId());
            bundle.putInt(AccountFragment.indexCar, position);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_accountFragment_to_carDescriptionFragment, bundle);
        });
    }

    public int getIndexActiveCar() {
        return indexActiveCar;
    }

    public void setIndexActiveCar(int indexActiveCar) {
        this.indexActiveCar = indexActiveCar;
    }

    @Override
    public int getItemCount() {return data.size();}

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        CarItemBinding binding;
        public CarViewHolder(@NonNull CarItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
