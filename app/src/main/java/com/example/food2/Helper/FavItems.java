package com.example.food2.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.food2.Domain.Foods;

import java.io.Serializable;
import java.util.ArrayList;

public class FavItems implements Serializable {

    //TODO: MODIFICAR TODO LO REFERENTE AL CARRITO, CONVERTIR EN LISTA DE FAVORITOS

    private Context context;
    private TinyDB tinyDB;
    String userId;

    public FavItems(Context context, String userId) {
        tinyDB = new TinyDB(context);

        this.context = context;

        this.userId = userId;
    }

    //////
    public void insertFood(Foods food, int quantity) {
        ArrayList<Foods> currentCart = getListCart(userId);
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < currentCart.size(); i++) {
            if (currentCart.get(i).getTitle().equals(food.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            int newQuantity = currentCart.get(n).getNumberInCart() + quantity;
            currentCart.get(n).setNumberInCart(newQuantity);
            Toast.makeText(context, "Añadido otro", Toast.LENGTH_SHORT).show();
        } else {
            food.setNumberInCart(quantity); // Establecer la cantidad directamente
            currentCart.add(food);
            Toast.makeText(context, "Añadido al Carrito", Toast.LENGTH_SHORT).show();
        }
        tinyDB.putListObject(userId + "_cart", currentCart);
    }

    //////

    public ArrayList<Foods> getListCart(String userId) {
        return tinyDB.getListObject(userId + "_cart");
    }

    public Double getTotalFee(){
        ArrayList<Foods> listItem = getListCart(userId);
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){

        if(listItem.get(position).getNumberInCart()==1){

            listItem.remove(position);

        }else{

            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);

        }

        tinyDB.putListObject(userId + "_cart",listItem);
        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){

        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject(userId + "_cart",listItem);
        changeNumberItemsListener.change();

    }
}
