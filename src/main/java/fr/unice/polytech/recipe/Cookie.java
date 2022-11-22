package fr.unice.polytech.recipe;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


public class Cookie {
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
    @Setter
    private Theme theme;
    @Getter
    private final List<Topping> toppings;

    public Cookie(String name,Double price, int cookingTime,Cooking cooking, Mix mix,Dough dough, Flavour flavour,List<Topping> toppings){

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

    public void addTopping(Topping topping) {
        toppings.add(topping);
    }
    public void setSize(CookieSize size){
        switch (size) {
            case L -> setPrice(getBasicPrice() * 4);
            case XL -> setPrice(getBasicPrice() * 5);
            case XXL -> setPrice(getBasicPrice() * 6);
            default -> setPrice(getBasicPrice());
        }
        this.size=size;
    }
    public Double getBasicPrice(){
        Double priceCookie=price;

        if(size==null)
            return priceCookie;
        switch(size){
            case L:
                priceCookie=price/4;
                break;
            case XL:
                priceCookie=price/5;
                break;
            case XXL:
                priceCookie=price/6;
                break;
            default:
                break;
        }
        return priceCookie;
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
