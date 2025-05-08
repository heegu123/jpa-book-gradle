package jpabook.model.entity;

import java.time.LocalDateTime;

public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Period() {
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isWork(LocalDateTime now) {
        return now != null && !now.isBefore(startDate) && !now.isAfter(endDate);
    }
}
