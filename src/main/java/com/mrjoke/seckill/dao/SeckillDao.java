package com.mrjoke.seckill.dao;

import com.mrjoke.seckill.entities.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     * 根据id查询秒杀商品记录
     * @param seckillId
     * @return 返回指定id的商品
     */
    Seckill selectById(@Param("seckillId") int seckillId);

    /**
     * 查询offset偏移量开始的limit条记录
     * @param offset
     * @param limit
     * @return 返回秒杀商品列表
     */
    List<Seckill> selectAll(@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 减少指定商品的库存
     * @param seckillId
     * @param killTime
     * @return 返回记录修改条数，为0则表示修改失败
     */
    int reduceQuantity(@Param("seckillId") int seckillId,@Param("killTime") Date killTime);
}
