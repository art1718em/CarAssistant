package com.example.carassistant.ui.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentRegistationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    private FragmentRegistationBinding binding;
    private FirebaseAuth mAuth;

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
        binding = FragmentRegistationBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        binding.btnGoBackToLogin.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_registrationFragment_to_loginFragment));


        binding.btnRegistration.setOnClickListener(v -> {

            Pattern patternPassword = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
            Pattern patternEmail = Pattern.compile("^\\S+@\\S+\\.\\S+$");

            if(binding.etInputLoginRegistration.getText().toString().isEmpty() || binding.etInputPasswordRegistration.getText().toString().isEmpty() ||
                binding.etInputPasswordAgainRegistration.getText().toString().isEmpty()){
                Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            }else if(!binding.etInputPasswordRegistration.getText().toString().equals(binding.etInputPasswordAgainRegistration.getText().toString())) {
                Toast.makeText(container.getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }else if(!patternPassword.matcher(binding.etInputPasswordRegistration.getText().toString()).matches()){
                Toast.makeText(container.getContext(), "Ваш пароль ненадежный", Toast.LENGTH_SHORT).show();
            }else if(!patternEmail.matcher(binding.etInputLoginRegistration.getText().toString()).matches()){
                Toast.makeText(container.getContext(), "Вы ввели некорректный логин", Toast.LENGTH_SHORT).show();
            } else{
                String email = binding.etInputLoginRegistration.getText().toString();
                String password = binding.etInputPasswordRegistration.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(container.getContext(), "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.action_registrationFragment_to_carsFragment);

                            } else {
                                Log.w("12345", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(container.getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });




        return binding.getRoot();
    }
}