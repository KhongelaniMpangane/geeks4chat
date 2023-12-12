package com.example.geeks4chat.activties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geeks4chat.databinding.ActivityLogInBinding;
import com.example.geeks4chat.tools.PreferenceManager;
import com.example.geeks4chat.tools.PublicConstants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class    ActivityLogIn extends AppCompatActivity {
ActivityLogInBinding binding;
private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager=new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(PublicConstants.KEY_IS_SIGNED_IN)){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
     setListeners();
    }


    // sign in activity
    private void setListeners() {
        binding.buttonSignUp.setOnClickListener
                (v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonLogin.setOnClickListener(v ->{
            if (isValidSignInDetails()){
                signIn();
            }
        });
    }
    //sign in method
    private void signIn(){
        loading(true);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection(PublicConstants.KEY_COLLECTION_USERS)
                .whereEqualTo(PublicConstants.KEY_EMAIL,binding.editTextUsername.getText().toString())
                .whereEqualTo(PublicConstants.KEY_PASSWORD,binding.editTextPassword.getText().toString()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() &&task.getResult()!=null &&task.getResult().getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(PublicConstants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(PublicConstants.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(PublicConstants.KEY_NAME,documentSnapshot.getString(PublicConstants.KEY_NAME));
                        preferenceManager.putString(PublicConstants.KEY_IMAGE,documentSnapshot.getString(PublicConstants.KEY_IMAGE));
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonLogin.setVisibility(View.INVISIBLE);

        } else {

            binding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.editTextUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false ;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextUsername.getText().toString()).matches()) {
            showToast("Enter Valid Email!");
            return false;
        } else if (binding.editTextPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }
}