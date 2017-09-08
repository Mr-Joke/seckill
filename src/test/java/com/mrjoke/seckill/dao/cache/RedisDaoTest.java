package com.mrjoke.seckill.dao.cache;

import com.mrjoke.seckill.dao.SeckillDao;
import com.mrjoke.seckill.entities.Seckill;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring/spring-dao.xml"})
public class RedisDaoTest {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private RedisDao redisDao;
    @Autowired
    SeckillDao seckillDao;

    @Test
    public void getSeckill() throws Exception {
    }

    @Test
    public void putSeckill() throws Exception {
    }

    @Test
    public void seckillTest(){
        int seckillId = 1;
        //1.首先从redis缓存中查询，有则返回
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill != null) logger.info(seckill);
        else{
            //2.若无，从mysql中查询
            Seckill seckill1 = seckillDao.selectById(seckillId);
            if (seckill1 != null){
                //3.将从mysql查询出来的对象保存到redis中
                String result = redisDao.putSeckill(seckill1);
                logger.info(result);
                //4.返回对象
                Seckill seckill2 = redisDao.getSeckill(seckill1.getSeckillId());
                logger.info(seckill2);
            }
        }
    }

}