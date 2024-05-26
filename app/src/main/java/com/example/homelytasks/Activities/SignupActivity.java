package com.example.homelytasks.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homelytasks.ModelClass.AuthModel;
import com.example.homelytasks.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.tvMoveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editName.getText().toString().trim();
                String email = binding.editEmail.getText().toString().trim();
                String password = binding.editPassword.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    binding.editName.setError("Name is Required!");
                } else if (email.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    binding.editEmail.setError("Email is Required!");
                } else if (password.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    binding.editPassword.setError("Password is Required!");
                } else {
                    AuthModel authData = new AuthModel(name, email, password);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Account Signed Up", Toast.LENGTH_SHORT).show();
                                    binding.editName.setText("");
                                    binding.editEmail.setText("");
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Failed! Try again", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(SignupActivity.this, e -> {
                                Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }
}