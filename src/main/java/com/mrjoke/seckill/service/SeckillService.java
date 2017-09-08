package com.mrjoke.seckill.service;

import com.mrjoke.seckill.dto.Exposer;
import com.mrjoke.seckill.dto.SeckillExecution;
import com.mrjoke.seckill.entities.Seckill;
import com.mrjoke.seckill.exception.RepeatSeckillException;
import com.mrjoke.seckill.exception.SeckillCloseException;
import com.mrjoke.seckill.exception.SeckillException;

import java.util.List;

/**
 * 秒杀业务接口：接口设计应该站在“使用者”的角度思考接口如何设计
 * 三个方面：1.方法定义的粒度 2.参数 3.返回类型
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 20:44
 */
public interface SeckillService {

    /**
     * 查询秒杀商品列表
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询秒杀商品
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(int seckillId);

    /**
     * 秒杀开启返回秒杀地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exposeSeckillUrl(int seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(int seckillId, String userPhone, String md5)
    throws RepeatSeckillException,SeckillCloseException,SeckillException;

    /**
     * 执行秒杀，使用存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws RepeatSeckillException
     * @throws SeckillCloseException
     * @throws SeckillException
     */
    SeckillExecution executeSeckillProcedure(int seckillId, String userPhone, String md5)
            throws RepeatSeckillException,SeckillCloseException,SeckillException;
}
