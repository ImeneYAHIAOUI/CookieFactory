package fr.unice.polytech.entities.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.*;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
public class TimeSlot implements Comparable<TimeSlot> {
    private LocalDateTime begin;
    private LocalDateTime end;


    public TimeSlot(LocalTime begin, Duration duration, Clock clock) {
        LocalDateTime beginDate = begin.atDate(LocalDate.now(clock));
        this.begin = beginDate.truncatedTo(ChronoUnit.MINUTES);
        this.end = beginDate.plus(duration).truncatedTo(ChronoUnit.MINUTES);
    }

    public TimeSlot(LocalTime begin, Duration duration, LocalDate date) {
        LocalDateTime beginDate = begin.atDate(date);
        this.begin = beginDate.truncatedTo(ChronoUnit.MINUTES);
        this.end = beginDate.plus(duration).truncatedTo(ChronoUnit.MINUTES);
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
