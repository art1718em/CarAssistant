package com.example.carassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.databinding.FragmentPanelBinding;

public class PanelFragment extends Fragment {

    FragmentPanelBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPanelBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = ((NavHostFragment)getChildFragmentManager().findFragmentById(R.id.nav_view_container)).getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        Bundle carBundle = requireArguments();
        Log.d("carasswork", carBundle.toString());
        getChildFragmentManager().findFragmentById(R.id.nav_view_container).getChildFragmentManager().setFragmentResult(DiagramFragment.key, carBundle);
    }

}