package ru.stanislavkulikov.energoplan;

public class CounterModel {
    private String Name;
    private Integer homesteadId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getHomesteadId() {
        return homesteadId;
    }

    public void setHomesteadId(Integer homesteadId) {
        this.homesteadId = homesteadId;
    }
}
