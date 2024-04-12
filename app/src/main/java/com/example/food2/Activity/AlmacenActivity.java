package com.example.food2.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food2.R;
import com.example.food2.databinding.ActivityAlmacenBinding;
import com.example.food2.databinding.ActivityDetailBinding;

public class AlmacenActivity extends AppCompatActivity {

    ActivityAlmacenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAlmacenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setVariable();

    }

    private void setVariable() {

        // TODO: CREAR UN AJAX PARA EL BUSCADOR

        binding.emptyTxt.setVisibility(View.VISIBLE);
        binding.emptyTxt.setText("ANTES");
        binding.cardView.setVisibility(View.GONE);

        binding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.emptyTxt.setVisibility(View.VISIBLE);
                binding.emptyTxt.setText("ANTES");
                binding.cardView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.emptyTxt.setText("CAMBIADO");
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.emptyTxt.setVisibility(View.GONE);
                binding.cardView.setVisibility(View.VISIBLE);
            }
        });

    }

}