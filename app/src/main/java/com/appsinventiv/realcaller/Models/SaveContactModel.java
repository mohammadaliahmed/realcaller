package com.appsinventiv.realcaller.Models;

import java.util.ArrayList;
import java.util.List;

public class SaveContactModel {
    public SaveContactModel(List<NameAndPhone> contactList) {
        this.contactList = contactList;
    }

    List<NameAndPhone> contactList=null;

    public List<NameAndPhone> getContactList() {
        return contactList;
    }

    public void setContactList(List<NameAndPhone> contactList) {
        this.contactList = contactList;
    }
}

