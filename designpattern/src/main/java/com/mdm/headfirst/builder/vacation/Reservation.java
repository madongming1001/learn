package com.mdm.headfirst.builder.vacation;

import java.time.LocalDate;

public class Reservation {
    LocalDate arrivalDate;
    int nights;

    public void setArrivalDate(int year, int month, int day) {
        this.arrivalDate = LocalDate.of(year, month, day);
    }

    public LocalDate getArrivalDate() {
        return this.arrivalDate;
    }

    public int getNights() {
        return this.nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }
}