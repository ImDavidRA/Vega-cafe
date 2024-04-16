package com.example.food2.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.food2.Domain.Foods;
import com.example.food2.Helper.FavItems;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.example.food2.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private int num = 1;
    private Foods object;
    private ManagmentCart managmentCart;
    DatabaseReference itemRef;
    private FavItems favList;
    private String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        favList = new FavItems(this, uid);
        managmentCart = new ManagmentCart(this, uid);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();

    }

    public void setVariable() {

        // TODO: HACER ESTO EN LOS DEMÁS ACTIVITY QUE SE VEAN TÍTULOS Y OTRAS COSAS ASÍ

        itemRef = FirebaseDatabase.getInstance().getReference().child("Foods");

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot foodSnapshot = snapshot.child(String.valueOf(object.getId()));
                    String description;
                    String titulo;

                    String language = Locale.getDefault().getLanguage();
                    if (language.equals("es") && foodSnapshot.child("texts").child("ESP").exists()) {

                        description = foodSnapshot.child("texts").child("ESP").child("desc").getValue(String.class);
                        titulo = foodSnapshot.child("texts").child("ESP").child("name").getValue(String.class);

                    } else {

                        description = foodSnapshot.child("texts").child("ENG").child("desc").getValue(String.class);
                        titulo = foodSnapshot.child("texts").child("ENG").child("name").getValue(String.class);

                    }

                    binding.descriptionTxt.setText(description);
                    binding.titleTxt.setText(titulo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText((object.getPrice()+"€").replace('.',','));
        binding.totalTxt.setText((num*object.getPrice()+"€").replace('.',','));

        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText(((num * object.getPrice()) + "€").replace('.',','));
            }
        });

        binding.minusBtn.setOnClickListener(v -> {
            if(num>1) {
                num=num-1;
                binding.numTxt.setText(num+"");
                binding.totalTxt.setText(((num*object.getPrice())+"€").replace('.',','));
            }
        });

        binding.addBtn.setOnClickListener(v -> {

            managmentCart.insertFood(object, num);

        });

        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favList.insertFav(object);
            }
        });

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}