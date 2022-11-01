package fr.unice.polytech.store;

import java.util.Comparator;

public class TimeSlotComparator implements Comparator<TimeSlot> {
    public int compare(TimeSlot t1, TimeSlot t2) {
        return t1.getBegin().compareTo(t2.getBegin());
    }
}

