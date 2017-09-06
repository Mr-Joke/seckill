package com.mrjoke.seckill.entities;

import java.util.Date;

public class Seckill {
    private int seckillId;
    private String seckillName;
    private int seckillQuantity;
    private Date seckillStartTime;
    private Date seckillEndTime;
    private Date seckillCreateTime;

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", seckillName='" + seckillName + '\'' +
                ", seckillQuantity=" + seckillQuantity +
                ", seckillStartTime=" + seckillStartTime +
                ", seckillEndTime=" + seckillEndTime +
                ", seckillCreateTime=" + seckillCreateTime +
                '}';
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public String getSeckillName() {
        return seckillName;
    }

    public void setSeckillName(String seckillName) {
        this.seckillName = seckillName;
    }

    public int getSeckillQuantity() {
        return seckillQuantity;
    }

    public void setSeckillQuantity(int seckillQuantity) {
        this.seckillQuantity = seckillQuantity;
    }

    public Date getSeckillStartTime() {
        return seckillStartTime;
    }

    public void setSeckillStartTime(Date seckillStartTime) {
        this.seckillStartTime = seckillStartTime;
    }

    public Date getSeckillEndTime() {
        return seckillEndTime;
    }

    public void setSeckillEndTime(Date seckillEndTime) {
        this.seckillEndTime = seckillEndTime;
    }

    public Date getSeckillCreateTime() {
        return seckillCreateTime;
    }

    public void setSeckillCreateTime(Date seckillCreateTime) {
        this.seckillCreateTime = seckillCreateTime;
    }
}
