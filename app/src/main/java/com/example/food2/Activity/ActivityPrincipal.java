package com.example.food2.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.food2.R;
import com.example.food2.TestFragments.CartFragment;
import com.example.food2.TestFragments.HomeFragment;
import com.example.food2.TestFragments.ProfileFragment;
import com.example.food2.TestFragments.SettingsFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ActivityPrincipal extends BaseActivity {

    MeowBottomNavigation bottomNavigation;
    RelativeLayout main_layout;
    HomeFragment InicioFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String idUser = getIntent().getStringExtra("clave_string");

        HomeFragment InicioFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("clave_string", idUser);
        InicioFragment.setArguments(bundle);

        replace(new HomeFragment());

        main_layout = findViewById(R.id.main_layout);

        // Creamos objeto con el id bottomNavigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setBackgroundColor(Color.parseColor("#00000000"));
        bottomNavigation.setBackgroundColor(Color.argb(0, 0, 0, 0));

        // Default en el que iniciará
        bottomNavigation.show(1,true);

        // Añadimos la cantidad de elementos que queremos que tenga la barra inferior
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.baseline_shopping_cart_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.baseline_person_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.baseline_settings_24));

        meowNavigation();

    }


    private void meowNavigation() {

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        replace(new HomeFragment());
                        break;

                    case 2:
                        replace(new CartFragment());
                        break;

                    case 3:
                        replace(new ProfileFragment());
                        break;

                    case 4:
                        replace(new SettingsFragment());
                        break;
                }
                return null;
            }
        });

    } ////////////////////

    private void replace(Fragment f) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, f);
        transaction.commit();
    }



}