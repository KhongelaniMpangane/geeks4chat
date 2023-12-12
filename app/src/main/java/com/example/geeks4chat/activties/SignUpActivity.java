package com.example.geeks4chat.activties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geeks4chat.databinding.ActivitySignUpBinding;
import com.example.geeks4chat.tools.PreferenceManager;
import com.example.geeks4chat.tools.PublicConstants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setListeners();

    }

    private void setListeners() {
        binding.buttonLoginFromSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ActivityLogIn.class)));
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(PublicConstants.KEY_NAME, binding.editTextSignUpUsername.getText().toString());
        user.put(PublicConstants.KEY_EMAIL, binding.editTextSignUpEmail.getText().toString());
        user.put(PublicConstants.KEY_PASSWORD, binding.editTextSignUpPassword.getText().toString());

        database.collection(PublicConstants.KEY_COLLECTION_USERS).add(user).addOnSuccessListener(documentReference -> {
            loading(false);
            preferenceManager.putBoolean(PublicConstants.KEY_IS_SIGNED_IN, true);
            preferenceManager.putString(PublicConstants.KEY_USER_ID, documentReference.getId());
            preferenceManager.putString(PublicConstants.KEY_NAME, binding.editTextSignUpUsername.getText().toString());

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }).addOnFailureListener(exception -> {
            loading(false);
            showToast(exception.getMessage());
        });
    }
    private Boolean isValidSignUpDetails(){

         if(binding.editTextSignUpUsername.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
        }else if (binding.editTextSignUpEmail.getText().toString().trim().isEmpty()){
            showToast("Enter email");
            return  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextSignUpEmail.getText().toString()).matches()) {
            showToast("Enter valid email address!");
            return  false;
        } else if (binding.editTextSignUpPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return  false;
        } else if (binding.editTextSignUpPassword.getText().toString().isEmpty()) {
            showToast("Confirm your password");
            return  false;

        }else {
            return true;
        }

    }
    public void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}