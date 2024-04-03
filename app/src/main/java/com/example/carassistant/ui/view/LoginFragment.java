package com.example.carassistant.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_carsFragment);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        binding.btnGoToRegistration.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_registrationFragment));


        binding.btnLogIn.setOnClickListener(v -> {
            if(binding.etInputLogin.getText().toString().isEmpty() || binding.etInputPassword.getText().toString().isEmpty()){
                Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            }else{
                String email = binding.etInputLogin.getText().toString();
                String password = binding.etInputPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(container.getContext(), "Login success.",
                                        Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.action_loginFragment_to_carsFragment);

                            } else {
                                Toast.makeText(container.getContext(), "Login failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

        return binding.getRoot();
    }
}