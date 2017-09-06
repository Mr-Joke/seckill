package com.mrjoke.seckill.dto;

import com.mrjoke.seckill.entities.Record;
import com.mrjoke.seckill.enums.SeckillStateEnum;

/**
 * 秒杀执行结果
 *
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 21:00
 */
public class SeckillExecution {
    private int seckillId;
    private int state;
    private String stateInfo;
    private Record record;

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", record=" + record +
                '}';
    }

    public SeckillExecution(int seckillId, SeckillStateEnum stateEnum, Record record) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.record = record;
    }

    public SeckillExecution(int seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
