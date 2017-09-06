package com.mrjoke.seckill.dao;

import com.mrjoke.seckill.entities.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
/**
 * 配置spring和junit整合,junit启动时加载spring IOC容器
 * spring-test,junit
 **/
@RunWith(SpringJUnit4ClassRunner.class)//加载spring容器
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:config/spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao实现依赖
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void selectById() throws Exception {
        int seckillId = 1;
        Seckill seckill = seckillDao.selectById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void selectAll() throws Exception {
        List<Seckill> seckills = seckillDao.selectAll(0, 4);
        for (Seckill seckill:seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceQuantity() throws Exception {
        int i = seckillDao.reduceQuantity(1, new Date());
        System.out.println(i);
    }

}