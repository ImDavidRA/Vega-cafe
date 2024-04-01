package com.example.food2.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food2.Adapter.CartAdapter;
import com.example.food2.Helper.ChangeNumberItemsListener;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.example.food2.databinding.ActivityCartBinding;

import java.text.DecimalFormat;

public class CartActivity extends BaseActivity {






    // ESTE ES EL ACTIVITY, EN PRINCIPIO NO SE USARÁ MÁS, YA QUE TENEMOS EL FRAGMENT











    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double impuesto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityCartBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //managmentCart = new ManagmentCart(this);

        //setVariable();
        //calcularCarrito();
        //initList();

    }

    /**
    private void initList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calcularCarrito());
        binding.cardView.setAdapter(adapter);
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

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
     */
}