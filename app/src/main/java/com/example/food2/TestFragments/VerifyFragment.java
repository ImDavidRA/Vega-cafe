package com.example.food2.TestFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food2.Activity.LoginActivity;
import com.example.food2.databinding.FragmentVerifyBinding;

public class VerifyFragment extends Fragment {

    FragmentVerifyBinding binding;
    Context context;

    public VerifyFragment() {
    }

    public static VerifyFragment newInstance(String param1, String param2) {
        VerifyFragment fragment = new VerifyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVerifyBinding.inflate(inflater, container, false);

        binding.exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyFragment.this.startActivity(new Intent(VerifyFragment.this.getActivity(), LoginActivity.class));
                VerifyFragment.this.requireActivity().finish();
            }
        });

        return binding.getRoot();
    }
}