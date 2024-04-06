package com.example.food2.TestFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food2.Activity.ActivityPrincipal;
import com.example.food2.Adapter.CartAdapter;
import com.example.food2.Adapter.FavAdapter;
import com.example.food2.Domain.Foods;
import com.example.food2.Helper.ChangeNumberItemsListener;
import com.example.food2.Helper.FavItems;
import com.google.firebase.auth.FirebaseAuth;
import com.example.food2.databinding.FragmentFavsBinding;

import java.util.ArrayList;

public class FavsFragment extends Fragment {

    private FragmentFavsBinding binding;
    private String uid;
    private FavItems favItems;
    private RecyclerView.Adapter adapter;
    private ArrayList<Foods> favList;

    public FavsFragment() {
    }

    public static FavsFragment newInstance() {
        FavsFragment fragment = new FavsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favItems = new FavItems(requireContext(), uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavsBinding.inflate(inflater, container, false);
        initList();
        return binding.getRoot();
    }

    private void initList() {
        favList = favItems.getListFav(uid);
        if (favList.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.cardView.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            binding.cardView.setLayoutManager(linearLayoutManager);

            adapter = new FavAdapter(favList, requireContext(), new ChangeNumberItemsListener() {
                @Override
                public void change() {

                }
            });
            binding.cardView.setAdapter(adapter);
        }
    }
}
