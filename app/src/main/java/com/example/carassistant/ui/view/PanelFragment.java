package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentPanelBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PanelFragment extends Fragment {

    FragmentPanelBinding binding;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPanelBinding.inflate(inflater, container, false);
//
//        Bundle carBundle = requireArguments();
//        getChildFragmentManager().beginTransaction()
//                .replace(binding.fragmentViewContainer.getId(), DiagramFragment.class, carBundle).commit();
//        binding.bottomNavigationView.setSelectedItemId(R.id.expenses_graph);
//
//
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
//            menuItem.setChecked(true);
//            if (menuItem.getItemId() == R.id.static_graph){
//                getChildFragmentManager().beginTransaction()
//                        .replace(binding.fragmentViewContainer.getId(), StatisticsFragment.class, carBundle).commit();
//            }else if (menuItem.getItemId() == R.id.expenses_graph){
//                getChildFragmentManager().beginTransaction()
//                        .replace(binding.fragmentViewContainer.getId(), DiagramFragment.class, carBundle).commit();
//            }else if (menuItem.getItemId() == R.id.storage_graph){
//                getChildFragmentManager().beginTransaction()
//                        .replace(binding.fragmentViewContainer.getId(), StorageFragment.class, carBundle).commit();
//            }
//            return false;
//        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = ((NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragment_view_container)).getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        Bundle bundle = requireArguments();

        CarBundle carBundle = CarBundle.init();

        carBundle.setBundle(bundle);


        Log.d("12345", "panel" + carBundle);
    }

}