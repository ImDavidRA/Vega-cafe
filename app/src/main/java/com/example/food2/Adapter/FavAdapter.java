package com.example.food2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food2.Domain.Foods;
import com.example.food2.Helper.ChangeNumberItemsListener;
import com.example.food2.Helper.FavItems;
import com.example.food2.Helper.ManagmentCart;
import com.example.food2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.viewholder> {

    ArrayList<Foods> list;
    private FavItems favItems;
    int num = 1;
    ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    private String uid;

    public FavAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        managmentCart = new ManagmentCart(context, uid);

        favItems = new FavItems(context, uid);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public FavAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_fav, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getTitle());

        holder.totalEachItem.setText(((num+" x "+(list.get(position).getPrice())+"€")).replace('.',','));
        holder.numItemTxt.setText(num+"");

        holder.feeEachItem.setText(((num*list.get(position).getPrice())+"€").replace('.',','));

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);


        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;
                holder.numItemTxt.setText(num + "");

                holder.feeEachItem.setText(String.format("%.2f€", num * list.get(position).getPrice()).replace('.', ','));

            }
        });

        holder.minusItem.setOnClickListener(v -> {
            if(num>1) {
                num=num-1;
                holder.numItemTxt.setText(num+"");

                holder.feeEachItem.setText(String.format("%.2f€", num * list.get(position).getPrice()).replace('.', ','));
            }
        });

        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favItems.insertFav(list.get(position));
            }
        });

        holder.dealAddBtn.setOnClickListener(v -> {

            managmentCart.insertFood(list.get(position), num);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,feeEachItem, plusItem, minusItem, dealAddBtn;
        ImageView pic, favBtn;
        TextView totalEachItem, numItemTxt;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            dealAddBtn = itemView.findViewById(R.id.dealAddBtn);
            favBtn = itemView.findViewById(R.id.favBtn);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            numItemTxt = itemView.findViewById(R.id.numItemTxt);
        }
    }
}
