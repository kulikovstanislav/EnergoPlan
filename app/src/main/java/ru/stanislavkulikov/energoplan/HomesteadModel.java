package ru.stanislavkulikov.energoplan;

public class HomesteadModel {

    private String homesteadNumberColumn;
    private String fioColumn;
    private String phoneColumn;
    private String feederColumn;

    public String getHomesteadNumberColumn() {
        return homesteadNumberColumn;
    }

    public void setHomesteadNumberColumn(String homesteadNumberColumn) {
        this.homesteadNumberColumn = homesteadNumberColumn;
    }

    public String getFioColumn() {
        return fioColumn;
    }

    public void setFioColumn(String fioColumn) {
        this.fioColumn = fioColumn;
    }

    public String getPhoneColumn() {
        return phoneColumn;
    }

    public void setPhoneColumn(String phoneColumn) {
        this.phoneColumn = phoneColumn;
    }

    public String getFeederColumn() {
        return feederColumn;
    }

    public void setFeederColumn(String feederColumn) {
        this.feederColumn = feederColumn;
    }
}
