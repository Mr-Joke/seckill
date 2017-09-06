package com.mrjoke.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 * spring提供的声明式事务只接受运行期异常回滚策略
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 21:03
 */
public class RepeatSeckillException extends SeckillException{
    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
