package com.mrjoke.seckill.enums;

/**
 * 秒杀状态信息枚举
 *
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 22:00
 */
public enum SeckillStateEnum {
    SUCCESS(0,"秒杀成功"),
    END(1,"秒杀结束"),
    REPEAT(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;
    private String stateInfo;

    private SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for (SeckillStateEnum stateEnum:values()){
            if (stateEnum.getState() == index) return stateEnum;
        }
        return null;
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
}
