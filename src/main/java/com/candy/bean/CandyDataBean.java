package com.candy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author zhangkai
 * @date 2019年04月14日 10:30
 */
public class CandyDataBean{

    private List<CandyDataItem> data;

    private String money;

    public List<CandyDataItem> getData() {
        return data;
    }

    public void setData(List<CandyDataItem> data) {
        this.data = data;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "CandyDataBean{" +
                "data=" + data +
                '}';
    }
}
