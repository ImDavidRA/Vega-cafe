package com.example.food2.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.food2.Domain.Foods;

import java.io.Serializable;
import java.util.ArrayList;

public class FavItems implements Serializable {

    //TODO: MODIFICAR TODO LO REFERENTE AL CARRITO, CONVERTIR EN LISTA DE FAVORITOS

    private Context context;
    private TinyDBFav tinyDBFav;
    String userId;
    ArrayList<Foods> favList;

    public FavItems(Context context, String userId) {
        tinyDBFav = new TinyDBFav(context);

        this.context = context;

        this.userId = userId;
    }

    public void insertFav(Foods food) {
        ArrayList<Foods> favList = getListFav(userId);

        if (existAlready(food)) {
            favList.remove(food);
            Toast.makeText(context, "Eliminado de Favoritos", Toast.LENGTH_SHORT).show();
        } else {
            favList.add(food);
            Toast.makeText(context, "Añadido a Favoritos", Toast.LENGTH_SHORT).show();
        }
        tinyDBFav.putListObject(userId + "_fav", favList);
    }

    public boolean existAlready(Foods food) {
        ArrayList<Foods> favList = getListFav(userId);
        for (int i = 0; i < favList.size(); i++) {
            if (favList.get(i).getTitle().equals(food.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Foods> getListFav(String userId) {
        return tinyDBFav.getListObject(userId + "_fav");
    }

}
