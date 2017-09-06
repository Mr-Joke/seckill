package com.mrjoke.seckill.dao;

import com.mrjoke.seckill.entities.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring/spring-dao.xml"})
public class RecordDaoTest {
    @Autowired
    private RecordDao recordDao;
    @Test
    public void insertRecord() throws Exception {
        int seckillId = 1;
        String recordPhone = "13544494187";
        int i = recordDao.insertRecord(seckillId, recordPhone);
        System.out.println(i);
    }

    @Test
    public void selectByIdWithSeckill() throws Exception {
        int seckillId = 1;
        String recordPhone = "13544494187";
        Record record = recordDao.selectByIdWithSeckill(seckillId, recordPhone);
        System.out.println(record);
    }

}