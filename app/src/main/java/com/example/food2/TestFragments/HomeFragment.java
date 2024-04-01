package com.example.food2.TestFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food2.Activity.CartActivity;
import com.example.food2.Activity.ListFoodsActivity;
import com.example.food2.Activity.LoginActivity;
import com.example.food2.Adapter.BestFoodAdapter;
import com.example.food2.Adapter.CategoryAdapter;
import com.example.food2.Domain.Category;
import com.example.food2.Domain.Foods;
import com.example.food2.Domain.Price;
import com.example.food2.Domain.Users;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.example.food2.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    private ManagmentCart managmentCart;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        setVariable();
        initPrice();
        initBestFood();
        initCategory();
    }

    private void setVariable() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Obtén el ID del usuario actualmente autenticado
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtén una referencia al nodo del usuario actual con la clave específica generada previamente
        DatabaseReference currentUserRef = usersRef.child(userId);

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtén el nombre del usuario
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String lastNameC = dataSnapshot.child("lastName").getValue(String.class);

                    String nombre = userName + " " + lastNameC;

                    // Establece el nombre del usuario en el TextView userName
                    binding.userName.setText(nombre);
                } else {
                    binding.userName.setText("Fallo");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores si los hubiera
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                HomeFragment.this.startActivity(new Intent(HomeFragment.this.getActivity(), LoginActivity.class));
                HomeFragment.this.requireActivity().finish();
            }
        });
        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchEdt.getText().toString();
            if (!text.isEmpty()) {
                Intent intent = new Intent(getActivity(), ListFoodsActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });
    }

    private void initBestFood() {

        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    if(list.size() > 0) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                        binding.bestFoodView.setLayoutManager(layoutManager);
                        RecyclerView.Adapter adapter = new BestFoodAdapter(list);
                        binding.bestFoodView.setAdapter(adapter);
                    }
                    binding.progressBarBestFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {

        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if(list.size() > 0) {
                        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
                        binding.categoryView.setLayoutManager(layoutManager);
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.categoryView.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initPrice() {

        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(requireContext(), R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
