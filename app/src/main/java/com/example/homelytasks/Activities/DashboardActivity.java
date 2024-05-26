package com.example.homelytasks.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homelytasks.AdapterClass.GroceryAdapter;
import com.example.homelytasks.ModelClass.GroceryModel;
import com.example.homelytasks.R;
import com.example.homelytasks.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreRef;
    private FirebaseUser firebaseUser;

    private ArrayList<GroceryModel> groceryList;
    private GroceryAdapter groceryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Firebase Firestore Section
        mAuth = FirebaseAuth.getInstance();
        firestoreRef = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //Shared Prefs
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseUser==null){
                    try {
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finishAffinity();
                    } catch (Exception e){

                    }
                }
            }
        });

        //Recycler Section
        groceryList = new ArrayList<GroceryModel>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.historyRecyclerView.setLayoutManager(layoutManager);

        //Fetch from Firestore on Recycler
        fetchFirestoreData();

        //Refresh
        binding.refreshBtn.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, DashboardActivity.class));
                finishAffinity();
            } catch(Exception e){

            }
        });

        //Logout
        binding.signOutBtn.setOnClickListener(v ->  {

            showLogoutDialog();

        });

        //Floating Buttons
        binding.addFloatingBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddGroceryActivity.class));
        });

        binding.clearFloatingBtn.setOnClickListener(v -> {

            showClearListDialog();

        });

    }

    private void showClearListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to clear the list?");

        builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform your action on positive button click
                    clearGroceryList();
            }
        });

        // Negative button ("Cancel")
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform your action on negative button click or do nothing
                dialogInterface.cancel();
            }
        });

        // Customize the positive button color
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF5151"));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
            }
        });

        alertDialog.show();

    }

    private void clearGroceryList() {



        firestoreRef.collection("Grocery").document(firebaseUser.getUid())
                .collection("List").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {

                            GroceryModel model = documentSnapshot.toObject(GroceryModel.class);

                            String id = model.getId().toString();
                            firestoreRef.collection("Grocery").document(firebaseUser.getUid())
                                    .collection("List").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DashboardActivity.this, "List Cleared!\nPlease refresh the page", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });
    }

    private void fetchFirestoreData() {

        firestoreRef.collection("Grocery").document(firebaseUser.getUid())
                .collection("List").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        groceryList.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            GroceryModel model = documentSnapshot.toObject(GroceryModel.class);

                            groceryList.add(model);

                        }

                        groceryAdapter = new GroceryAdapter(groceryList, DashboardActivity.this);
                        binding.historyRecyclerView.setAdapter(groceryAdapter);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        dialog.cancel();
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


}