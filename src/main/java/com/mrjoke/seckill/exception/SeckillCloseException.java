package com.mrjoke.seckill.exception;

/**
 * 秒杀关闭异常
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 21:06
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
