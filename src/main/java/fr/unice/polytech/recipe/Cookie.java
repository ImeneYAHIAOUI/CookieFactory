package fr.unice.polytech.recipe;

import lombok.*;

import java.util.List;


public class Cookie extends Recipe{
    @Getter
    private CookieSize size;
    @Getter
    private Theme theme;

    public Cookie(String name,Double price, int cookingTime,Cooking cooking, Mix mix,Dough dough, Flavour flavour,List<Topping> toppings){
        super( name, price,  cookingTime, cooking,  mix, dough,  flavour, toppings);
        this.size=CookieSize.BASIC;
    }
    public void setSize(CookieSize size){
        switch(size){
            case L:
                setPrice(getBasicPrice()*4);
                break;
            case XL:
                setPrice(getBasicPrice()*5);
                break;
            case XXL:
                setPrice(getBasicPrice()*6);
                break;
            default:
                break;
        }
        this.size=size;

    }
    public Double getBasicPrice(){
        Double priceCookie;
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
                priceCookie=price;
                ;
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
