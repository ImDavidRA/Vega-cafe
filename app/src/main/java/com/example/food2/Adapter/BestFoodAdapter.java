package com.example.food2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.food2.Activity.DetailActivity;
import com.example.food2.Domain.Foods;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class BestFoodAdapter extends RecyclerView.Adapter<BestFoodAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;
    private String uid;
    DatabaseReference itemRef;
    private ManagmentCart managmentCart;

    public BestFoodAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BestFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_deal,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestFoodAdapter.viewholder holder, int position) {

        itemRef = FirebaseDatabase.getInstance().getReference().child("Foods");

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

        holder.priceTxt.setText((items.get(position).getPrice() + "€").replace('.',','));

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });

        holder.dealAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                managmentCart = new ManagmentCart(context, uid);

                managmentCart.insertFood(items.get(position), 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView titleTxt, priceTxt, dealAddBtn; // Cambiado a TextView
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.titleTxt);
            priceTxt=itemView.findViewById(R.id.priceTxt);
            pic=itemView.findViewById(R.id.pic);
            dealAddBtn=itemView.findViewById(R.id.dealAddBtn); // Cambiado a TextView
        }
    }
}
