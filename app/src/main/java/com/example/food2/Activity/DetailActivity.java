package com.example.food2.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.food2.Domain.Foods;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.example.food2.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private int num = 1;
    private int nume;
    private Foods object;
    private ManagmentCart managmentCart;
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

        managmentCart = new ManagmentCart(this, uid);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();

    }

    public void setVariable() {

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText((object.getPrice()+"€").replace('.',','));
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
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

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}