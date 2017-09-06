package com.mrjoke.seckill.exception;

/**
 * 秒杀业务相关异常
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 21:06
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
