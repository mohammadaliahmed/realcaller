package com.appsinventiv.realcaller.Models;

public class SmsModel {
    String phone,name,type,body,date;


    public SmsModel(String phone, String name, String type, String body, String date) {
        this.phone = phone;
        this.name = name;
        this.type = type;
        this.date = date;
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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


}
