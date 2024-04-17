package com.example.food2.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food2.Activity.DetailActivity;
import com.example.food2.Domain.Foods;
import com.example.food2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.viewholder> {

    ArrayList<Foods> items;
    Context context;
    DatabaseReference itemRef;
    Dialog dialogProduct;
    Button cerrarDialogProduct;
    public EditProductAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public EditProductAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_edit_product, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProductAdapter.viewholder holder, int position) {
        itemRef = FirebaseDatabase.getInstance().getReference().child("Foods");

        // ---------------- DIALOG PRODUCT ---------------- //

        dialogProduct = new Dialog(context);
        dialogProduct.setContentView(R.layout.pop_up_edit_product);
        dialogProduct.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogProduct.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialog_bg));
        dialogProduct.setCancelable(false);

        cerrarDialogProduct = dialogProduct.findViewById(R.id.cancelPop);

        cerrarDialogProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProduct.dismiss();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            dialogProduct.show();
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProduct.show();
            }
        });

        // ---------------- DIALOG PRODUCT ---------------- //

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot foodSnapshot = snapshot.child(String.valueOf(items.get(position).getId()));
                    String titulo;

                    String language = Locale.getDefault().getLanguage();
                    if (language.equals("es") && foodSnapshot.child("texts").child("ESP").exists()) {

                        titulo = foodSnapshot.child("texts").child("ESP").child("name").getValue(String.class);



                    } else {

                        titulo = foodSnapshot.child("texts").child("ENG").child("name").getValue(String.class);

                    }

                    holder.titleTxt.setText(titulo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar evento de cancelación si es necesario
            }
        });
        holder.priceTxt.setText((items.get(position).getPrice()+ " €").replace('.',','));

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt;
        Button editBtn;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            editBtn = itemView.findViewById(R.id.editBtn);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);

            pic = itemView.findViewById(R.id.img);
        }
    }
}
