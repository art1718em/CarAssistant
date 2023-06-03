package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentStatisticsBinding;


public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private int mileage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }
}