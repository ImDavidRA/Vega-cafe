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

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

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

        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();

    }

    private void setVariable() {

        managmentCart = new ManagmentCart(this);

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText(object.getPrice()+"€");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.totalTxt.setText(num*object.getPrice()+"€");

        binding.plusBtn.setOnClickListener(v -> {
            num = num+1;
            binding.numTxt.setText(num+"");
            binding.totalTxt.setText((num*object.getPrice())+"€");
        });

        binding.minusBtn.setOnClickListener(v -> {
            if(num>1) {
                num=num-1;
                binding.numTxt.setText(num+"");
                binding.totalTxt.setText((num*object.getPrice())+"€");
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}