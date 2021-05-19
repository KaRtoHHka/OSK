package com.oleg.oskfin.data;

import java.util.Date;

public class Orders {
    private String item, point;
    private long orderTime;

    Orders() {
        this.orderTime = new Date().getTime();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public long getOrderTime() {
        return orderTime;
    }
}
