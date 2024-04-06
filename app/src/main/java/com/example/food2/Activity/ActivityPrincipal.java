package com.example.food2.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.example.food2.TestFragments.FavsFragment;
import com.example.food2.TestFragments.VerifyFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ActivityPrincipal extends BaseActivity {

    MeowBottomNavigation bottomNavigation;
    RelativeLayout main_layout;
    String uidUser;
    ArrayList<Integer> miArrayList = new ArrayList<>();

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

        FirebaseUser user = mAuth.getCurrentUser();
        main_layout = findViewById(R.id.main_layout);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (!user.isEmailVerified()) {

            replace(new VerifyFragment());
            bottomNavigation.setVisibility(View.GONE);

        } else {
            uidUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            replace(new HomeFragment());
            miArrayList.add(1);

            bottomNavigation.setBackgroundColor(Color.parseColor("#00000000"));
            bottomNavigation.setBackgroundColor(Color.argb(0, 0, 0, 0));

            bottomNavigation.show(1,true);

            bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.baseline_home_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.baseline_shopping_cart_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.baseline_favorite_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.baseline_person_24));

            meowNavigation();
        }
    }

    private void meowNavigation() {

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        replace(new HomeFragment());
                        miArrayList.add(1);
                        break;

                    case 2:
                        replace(new CartFragment());
                        miArrayList.add(2);
                        break;

                    case 3:
                        replace(new ProfileFragment());
                        miArrayList.add(3);
                        break;

                    case 4:
                        replace(new FavsFragment());
                        miArrayList.add(4);
                        break;
                }

                return null;
            }
        });

    } ////////////////////

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            if (!miArrayList.isEmpty()) {
                miArrayList.remove(miArrayList.size() - 1);
            }

            int num = !miArrayList.isEmpty() ? miArrayList.get(miArrayList.size() - 1) : 1;

            setIcono(num);

            getSupportFragmentManager().popBackStack();

        } else {
            startActivity(new Intent(ActivityPrincipal.this, LoginActivity.class));
            finish();
        }
    }

    private void replace(Fragment f) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, f);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void setIcono(int num) {
        bottomNavigation.show(num, true);
    }

}