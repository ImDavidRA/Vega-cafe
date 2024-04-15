package com.example.food2.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food2.Adapter.FoodListAdapter;
import com.example.food2.Domain.Foods;
import com.example.food2.R;
import com.example.food2.databinding.ActivityAlmacenBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlmacenActivity extends BaseActivity {

    ActivityAlmacenBinding binding;
    private boolean isSearch;
    String text;
    private String searchText;
    private RecyclerView.Adapter adapterListFood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                text = binding.searchEdt.getText().toString();
                searchText = text;

                initList();
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.emptyTxt.setVisibility(View.GONE);
                binding.cardView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.emptyTxt.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        Query query;
        if(isSearch) {
            String sub = searchText.substring(0,1).toUpperCase();
            searchText = searchText.substring(1);

            searchText = sub+searchText;

            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+'\uf8ff');
        } else {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+'\uf8ff');
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    if(list.size()>0) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(AlmacenActivity.this, 1));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.emptyTxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}