package fr.unice.polytech.entities.recipe;

import fr.unice.polytech.exception.CookieException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


public class Cookie  {
    @Getter
    private CookieSize size;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Double price;
    @Getter
    @Setter
    private int cookingTime;
    @Getter
    @Setter
    private Cooking cooking;
    @Getter
    @Setter
    private Mix mix;
    @Getter
    @Setter
    private Dough dough;
    @Getter
    @Setter
    private Flavour flavour;

    @Getter
    private final List<Topping> toppings;

     Cookie(String name,double price,int cookingTime,Cooking cooking, Mix mix,Dough dough, Flavour flavour,List<Topping> toppings) throws CookieException {

        this.name=name;
        this.price=price;
        this.cookingTime=cookingTime;
        this.cooking=cooking;
        this.mix=mix;
        this.dough=dough;
        this.flavour=flavour;
        this.toppings=new ArrayList<>();
        if(toppings!=null){
            for (Topping topping : toppings) {
                addTopping(topping);
            }
        }
    }

    public void addTopping(Topping topping) throws CookieException {
        if(toppings.size()<3){
            toppings.add(topping);
        }
        else throw new CookieException("Too many toppings, you can't have more than 3 in one recipe");
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
