package com.example.homelytasks.AdapterClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelytasks.ModelClass.GroceryModel;
import com.example.homelytasks.R;
import com.example.homelytasks.databinding.ItemGroceryBinding;

import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {

    private ArrayList<GroceryModel> arrayList;
    private Context context;
    public GroceryAdapter(ArrayList<GroceryModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroceryModel data = arrayList.get(position);
        holder.binding.itemName.setText(data.getName());
        holder.binding.itemQuantity.setText(data.getQuantity());
        holder.binding.itemPrice.setText("Rs. " + data.getPrice());

        holder.itemView.setOnClickListener(v ->{

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure?");

            // Positive button ("Check") with green color
            builder.setPositiveButton("Check", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Perform your action on positive button click
                    Toast.makeText(context, "Checked!", Toast.LENGTH_SHORT).show();
                    holder.binding.checkItem.setVisibility(View.VISIBLE);
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
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#11D99B"));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF5151"));
                }
            });

            alertDialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ItemGroceryBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGroceryBinding.bind(itemView);
        }
    }

}
