package com.appsinventiv.realcaller.Models;

public class CallLogsModel {
    String phone,name,type,duration,simNumber;


    public CallLogsModel(String phone,String name, String type, String duration,String simNumber) {
        this.phone = phone;
        this.name = name;
        this.type = type;
        this.simNumber = simNumber;
        this.duration = duration;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
