package com.mrjoke.seckill.entities;

import java.util.Date;

public class Record {
    private int seckillId;
    private String recordPhone;
    private int recordSeckillCount;
    private Date recordSeckillTime;
    private Seckill seckill;

    @Override
    public String toString() {
        return "Record{" +
                "seckillId=" + seckillId +
                ", recordPhone='" + recordPhone + '\'' +
                ", recordSeckillCount=" + recordSeckillCount +
                ", recordSeckillTime=" + recordSeckillTime +
                ", seckill=" + seckill +
                '}';
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public String getRecordPhone() {
        return recordPhone;
    }

    public void setRecordPhone(String recordPhone) {
        this.recordPhone = recordPhone;
    }

    public int getRecordSeckillCount() {
        return recordSeckillCount;
    }

    public void setRecordSeckillCount(int recordSeckillCount) {
        this.recordSeckillCount = recordSeckillCount;
    }

    public Date getRecordSeckillTime() {
        return recordSeckillTime;
    }

    public void setRecordSeckillTime(Date recordSeckillTime) {
        this.recordSeckillTime = recordSeckillTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }
}
