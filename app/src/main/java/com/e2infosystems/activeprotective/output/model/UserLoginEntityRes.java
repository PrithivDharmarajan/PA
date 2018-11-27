package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserLoginEntityRes implements Serializable {

    private ArrayList<UserLoginDetailsEntityRes> Items=new ArrayList<>();

    public ArrayList<UserLoginDetailsEntityRes> getItems() {
        return Items;
    }

    public void setItems(ArrayList<UserLoginDetailsEntityRes> items) {
        Items = items;
    }


}
