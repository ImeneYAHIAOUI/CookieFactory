package fr.unice.polytech.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeSlot implements Comparable<TimeSlot> {
    private LocalTime begin;
    private LocalTime end;

    public TimeSlot(LocalTime begin, Duration duration) {
        this.begin = begin;
        this.end = begin.plus(duration);
    }

    public void slideBy(Duration duration) {
        begin = begin.plus(duration);
        end = end.plus(duration);
    }

    public boolean overlaps(TimeSlot other) {
        return begin.isBefore(other.end) && end.isAfter(other.begin)
                || begin.equals(other.begin) || end.equals(other.end)
                || begin.isAfter(other.end) && end.isBefore(other.begin);
    }

    @Override
    public int compareTo(TimeSlot o) {
        return begin.compareTo(o.begin);
    }
}
