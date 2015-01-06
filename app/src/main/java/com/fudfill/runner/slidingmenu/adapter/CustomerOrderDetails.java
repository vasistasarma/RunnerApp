package com.fudfill.runner.slidingmenu.adapter;
import java.io.Serializable;
/**
 * Created by Sowmya on 1/4/2015.
 */
public class CustomerOrderDetails implements Serializable {
    private String itemName;
    private int quantity;
    private int price;
    public CustomerOrderDetails(String item, int quantity, int price){
        this.itemName = item;
        this.quantity=quantity;
        this.price=price;
    }
    public String getItemName(){
        return this.itemName;
    }
    public void setItemName(String itemName){
        this.itemName=itemName;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
    public int getPrice(){
        return this.price;
    }
    public void setPrice(int price){
        this.price=price;
    }

}
