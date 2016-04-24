package ru.stanislavkulikov.energoplan;

import java.util.Date;

public class IndicationModel {

    private Integer counterId;
    private float indication;
    private Date date;

    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public float getIndication() {
        return indication;
    }

    public void setIndication(float indication) {
        this.indication = indication;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
