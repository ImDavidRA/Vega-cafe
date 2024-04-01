package com.example.food2.TestFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food2.Adapter.CartAdapter;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.example.food2.databinding.FragmentCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private FirebaseDatabase database;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double impuesto;
    private String uid;

    public CartFragment() {
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        managmentCart = new ManagmentCart(getActivity(), uid);
        initList();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        calcularCarrito();
    }

    private void initList() {
        if(managmentCart.getListCart(uid).isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);

        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            binding.cardView.setLayoutManager(linearLayoutManager);
            adapter = new CartAdapter(managmentCart.getListCart(uid), getActivity(), this::calcularCarrito);
            binding.cardView.setAdapter(adapter);
        }
    }

    private void calcularCarrito() {
        double porcentImpuesto = 0.10; // Porcentaje 10% Impuestos

        impuesto = Math.round(managmentCart.getTotalFee() * porcentImpuesto * 100.0) / 100;

        double total = managmentCart.getTotalFee() + impuesto;

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        String itemTotalFormatted = df.format(managmentCart.getTotalFee());
        String impuestoFormatted = df.format(impuesto);
        String totalFormatted = df.format(total);

        binding.totalFeeTxt.setText(itemTotalFormatted + "€");
        binding.impuestoTxt.setText(impuestoFormatted + "€");
        binding.totalTxt.setText(totalFormatted + "€");
    }


}
