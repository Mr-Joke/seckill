package com.mrjoke.seckill.dto;

import java.util.Date;

/**
 * 暴露秒杀地址DTO
 *
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 20:54
 */
public class Exposer {
    //是否秒杀已经开启
    private boolean exposed;
    //加密措施
    private String md5;
    //秒杀商品id
    private int seckillId;
    //当前系统时间
    private long now;
    //秒杀开启时间
    private long start;
    //秒杀结束时间
    private long end;

    public Exposer(boolean exposed, int seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, String md5, int seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed,int seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.now = now;
        this.seckillId = seckillId;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
