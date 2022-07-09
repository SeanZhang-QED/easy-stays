package com.easy.easystays.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable // indicate it will be used as a composed Key of StayReservedDate table
public class StayReservedDateKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "stay_id")
    private int stayId;
    private LocalDate date;

    public StayReservedDateKey() {
    }

    public StayReservedDateKey(int stay_id, LocalDate date) {
        this.stayId = stay_id;
        this.date = date;
    }

    // equals and hashCode function are needed to be a Key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StayReservedDateKey that = (StayReservedDateKey) o;
        return Objects.equals(stayId, that.stayId) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stayId, date);
    }

    public int getStay_id() {
        return stayId;
    }

    public void setStay_id(int stay_id) {
        this.stayId = stay_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
