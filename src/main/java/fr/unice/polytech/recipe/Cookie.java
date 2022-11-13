package fr.unice.polytech.recipe;

import lombok.*;

import java.util.List;


public class Cookie extends Recipe{
    @Getter
    private Size size;
    public Cookie(String name,Double price, int cookingTime,Cooking cooking, Mix mix,Dough dough, Flavour flavour,List<Topping> toppings){
        super( name, price,  cookingTime, cooking,  mix, dough,  flavour, toppings);
    }
    public void setSize(Size size){
        this.size=size;
        switch(size){
            case L:
                setPrice(getPrice()*4);
                break;

            case XL:
                setPrice(getPrice()*5);
                break;

            case XXL:
                setPrice(getPrice()*6);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "Cookie {" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", cookingTime=" + cookingTime +
                ", cooking=" + cooking +
                ", mix=" + mix +
                ", dough=" + dough +
                ", flavour=" + flavour +
                ", toppings=" + toppings +
                '}';
    }
}
