package com.motion.laundryq.utils;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItem {
    public static List<String> itemFilter() {
        List<String> item = new ArrayList<>();
        item.add(0, "Pakaian");
        item.add(1, "Sepatu");
        item.add(2, "Jas");
        item.add(3, "Boneka");
        item.add(4, "Bed Cover");

        return item;
    }

    public static List<String> itemSort() {
        List<String> item = new ArrayList<>();
        item.add(0, "Terdekat");
        item.add(1, "Harga Terendah");
        item.add(2, "Harga Tertinggi");

        return item;
    }
}
