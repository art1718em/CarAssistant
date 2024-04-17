package com.example.carassistant.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Success;
import com.example.carassistant.databinding.ActivityMainBinding;
import com.example.carassistant.ui.viewModels.LoginViewModel;


import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentListener, true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentListener);
    }

    private final FragmentManager.FragmentLifecycleCallbacks fragmentListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            if(navController != Navigation.findNavController(v)) {
                navController = Navigation.findNavController(v);
            }
        }
    };

    private boolean isStartDestnation(NavDestination destination){ //Проверка находимся(в данном случае передали) ли мы на стартовом экране
        if (destination == null) return false;
        NavGraph graph = destination.getParent();
        if (graph == null) return false;
        List<Integer> startDestinations = Collections.singletonList(R.id.panelFragment);
        return startDestinations.contains(destination.getId());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {

        if (isStartDestnation(navController.getCurrentDestination()) || navController.getBackQueue().isEmpty()){ //Если не создали navController или пользователь не залогинен
            if (back_pressed + 2500 > System.currentTimeMillis()) {
                super.onBackPressed();
            }
            else {
                Toast.makeText(getBaseContext(), "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
        else{
            if(navController.getBackQueue().getSize() > 3) {
                navController.popBackStack();

                return;
            }
            else
                super.onBackPressed();
            if (navController.getGraph().getId() != R.id.nav_graph)
                navController.setGraph(R.navigation.nav_graph);
        }


    }

    public void navRestart(){
        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        isLogin();
        navController.setGraph(navGraph);
    }

    private void isLogin(){
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.isLogIn();

        SharedPreferences sharedPreferences = getSharedPreferences(LoginFragment.sharedPreferencesName, MODE_PRIVATE);
        String idGuestUser = sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null");

        viewModel.resultOfLogin.observe(this, result -> {
            if (result instanceof Success || !idGuestUser.equals("null")){
                ifLogin();
            }
        });
    }

    private void ifLogin(){
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        navGraph.setStartDestination(R.id.panelFragment);
        navController.setGraph(navGraph);
    }
}