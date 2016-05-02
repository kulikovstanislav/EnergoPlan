package ru.stanislavkulikov.energoplan;

import java.util.Date;

public class PaymentModel {

    private Integer counterId;
    private float payment;
    private Date date;
    private Integer homesteadId;

    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getHomesteadId() {
        return homesteadId;
    }

    public void setHomesteadId(Integer homesteadId) {
        this.homesteadId = homesteadId;
    }
}
