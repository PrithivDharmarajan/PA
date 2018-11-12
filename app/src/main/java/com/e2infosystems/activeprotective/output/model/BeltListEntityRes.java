package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BeltListEntityRes implements Serializable {

    private int Count = 0;
    private int ScannedCount = 0;
    private ArrayList<BeltItemListEntityRes> Items = new ArrayList<>();

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getScannedCount() {
        return ScannedCount;
    }

    public void setScannedCount(int scannedCount) {
        ScannedCount = scannedCount;
    }

    public ArrayList<BeltItemListEntityRes> getItems() {
        return Items;
    }

    public void setItems(ArrayList<BeltItemListEntityRes> items) {
        Items = items;
    }

}
