package com.example.food2.TestFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.food2.R;
import com.example.food2.databinding.FragmentCartBinding;
import com.example.food2.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    //TODO: Terminar de hacer el diseño antes de añadir funcionalidad

    String userId;
    FragmentProfileBinding binding;
    Context context;
    ImageView pic;
    DatabaseReference userRef;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        context = getContext();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        pic = binding.userPic;

        userRef.child("nivel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int lvl = (snapshot.getValue(Integer.class));
                if (lvl > 0) {
                    binding.stockControl.setVisibility(View.VISIBLE);
                } else {
                    binding.stockControl.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child("userPicPath").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageURL = dataSnapshot.getValue(String.class);
                if (imageURL != null) {
                    Glide.with(context)
                            .load(imageURL)
                            .transform(new CircleCrop(), new FitCenter())
                            .override(700, 520)
                            .into(pic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return binding.getRoot();
    }
}
