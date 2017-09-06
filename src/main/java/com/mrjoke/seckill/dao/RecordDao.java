package com.mrjoke.seckill.dao;

import com.mrjoke.seckill.entities.Record;
import org.apache.ibatis.annotations.Param;

public interface RecordDao {
    /**
     * 插入一条用户秒杀记录，可过滤重复
     * @param seckillId
     * @param recordPhone
     * @return 返回记录修改的条数，为0插入失败
     */
    int insertRecord(@Param("seckillId") int seckillId,@Param("recordPhone") String recordPhone);

    /**
     * 根据联合主键查询Record并携带Seckill秒杀商品对象
     * @param seckillId
     * @return
     */
    Record selectByIdWithSeckill(@Param("seckillId") int seckillId,@Param("recordPhone") String recordPhone);
}
