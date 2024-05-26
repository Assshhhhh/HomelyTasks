package com.example.homelytasks.Activities;

import static android.widget.Toast.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.homelytasks.ModelClass.GroceryModel;
import com.example.homelytasks.R;
import com.example.homelytasks.databinding.ActivityAddGroceryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.UUID;

public class AddGroceryActivity extends AppCompatActivity {

    private ActivityAddGroceryBinding binding;
    private FirebaseFirestore firestoreRef;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGroceryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestoreRef = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        binding.buttonAddItem.setOnClickListener(v -> {

            String id = UUID.randomUUID().toString();
            String item_name = binding.editGroceryItemAdd.getText().toString().trim();
            String item_quantity = binding.editQuantityAdd.getText().toString().trim();
            String item_price = binding.editPriceAdd.getText().toString().trim();

            if (item_name.equals("")){
                makeText(this, "Please enter an item", LENGTH_SHORT).show();
            }
            else if (item_quantity.equals("")){
                makeText(this, "Please enter a quantity!", LENGTH_SHORT).show();
            } else {

                GroceryModel groceryModel = new GroceryModel(id, item_name, item_quantity, item_price);

                firestoreRef.collection("Grocery").document(firebaseUser.getUid())
                        .collection("List").document(id).set(groceryModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    binding.editGroceryItemAdd.setText("");
                                    binding.editQuantityAdd.setText("");
                                    binding.editPriceAdd.setText("");
                                    try {
                                        Intent intent = new Intent(AddGroceryActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                makeText(AddGroceryActivity.this, e.toString(), LENGTH_SHORT).show();
                            }
                        });

            }


        });

    }
}