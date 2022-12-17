package fr.unice.polytech.entities.store;


import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.recipe.enums.Theme;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Store {
    @Getter
    public final String address;

    @Getter
    private final List<Cook> cooks;
    @Getter
    private LocalTime openingTime;
    @Getter
    private LocalTime closingTime;
    @Getter
    private final List<Occasion> occasionList;
    @Getter
    private final List<Theme> themeList;
    @Getter
    private final UUID id;
    @Getter
    private final Inventory inventory;
    @Getter
    private final List<RegisteredClient> tooGooToGoClients;

    @Getter
    @Setter
    private Double tax;
     public Store(
             List<Cook> cooks,
             String address,
             LocalTime openingTime,
             LocalTime closingTime,
             Inventory inventory,
             double tax,
             List<Occasion> occasions
     ) {
        occasionList = new ArrayList<>();

        themeList=new ArrayList<>();
         tooGooToGoClients =new ArrayList<>();
        this.cooks = new ArrayList<>(cooks);
        for (Cook cook: cooks) {
            List<Theme> themes=cook.getThemeList();
            for (Theme theme: themes) {
                if(!themeList.contains(theme))
                     themeList.add(theme);
            }
        }
        this.address = address;
        this.tax = tax;
        if (openingTime.isBefore(closingTime)) {
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        } else {
            System.out.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.openingTime = closingTime;
            this.closingTime = openingTime;
        }
        this.id = UUID.randomUUID();
        this.inventory = inventory;
        if (occasions != null) {
            occasions.forEach(occasion -> {
                if (!this.occasionList.contains(occasion)) {
                    this.occasionList.add(occasion);
                }
            });
        }



    }

    public void setHours(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + id +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }

}
