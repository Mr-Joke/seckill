package com.mrjoke.seckill.service;

import com.mrjoke.seckill.dto.Exposer;
import com.mrjoke.seckill.dto.SeckillExecution;
import com.mrjoke.seckill.entities.Seckill;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 22:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring/spring-dao.xml",
"classpath:config/spring/spring-service.xml"})
public class SeckillServiceTest {
    private Logger logger = Logger.getLogger(SeckillServiceTest.class);
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        /*
        * 2017-09-06 22:43:13 INFO
         * SeckillServiceTest:30 -
         * [Seckill{seckillId=1000, seckillName='100元秒杀大冬瓜', seckillQuantity=100, seckillStartTime=Wed Sep 06 00:00:00 CST 2017, seckillEndTime=Thu Sep 07 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017},
          * Seckill{seckillId=1001, seckillName='1000元秒iPad', seckillQuantity=200, seckillStartTime=Thu Sep 07 00:00:00 CST 2017, seckillEndTime=Fri Sep 08 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017},
          * Seckill{seckillId=1002, seckillName='2000元秒杀iPhone8', seckillQuantity=300, seckillStartTime=Fri Sep 08 00:00:00 CST 2017, seckillEndTime=Sat Sep 09 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017},
          * Seckill{seckillId=1003, seckillName='0元秒杀小冬瓜', seckillQuantity=1000, seckillStartTime=Sat Sep 09 00:00:00 CST 2017, seckillEndTime=Sun Sep 10 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017}]
        * */
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info(seckillList);
    }

    @Test
    public void getSeckillById() throws Exception {
        /*
        * 2017-09-06 22:49:04 INFO
         * SeckillServiceTest:45 -
         * Seckill{seckillId=1002, seckillName='2000元秒杀iPhone8', seckillQuantity=300, seckillStartTime=Fri Sep 08 00:00:00 CST 2017, seckillEndTime=Sat Sep 09 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017}
        * */
        int seckillId = 1002;
        Seckill seckill = seckillService.getSeckillById(seckillId);
        logger.info(seckill);
    }

    @Test
    public void exposeSeckillUrl() throws Exception {
        int seckillId = 1000;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        logger.info(exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        /*
        * 2017-09-06 23:16:10 INFO
        * SeckillServiceTest:67 -
        * SeckillExecution{seckillId=1000, state=0, stateInfo='秒杀成功',
        * record=Record{seckillId=1000, recordPhone='13544494187', recordSeckillCount=1, recordSeckillTime=Wed Sep 06 23:16:10 CST 2017,
        * seckill=Seckill{seckillId=1000, seckillName='100元秒杀大冬瓜', seckillQuantity=98, seckillStartTime=Wed Sep 06 00:00:00 CST 2017, seckillEndTime=Thu Sep 07 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017}}}
        * */
        int seckillId = 1000;
        String phone = "13544494187";
        SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, "6f6c16a9316ed92aa8da8260caba0221");
        logger.info(seckillExecution);
    }

    @Test
    public void seckill(){
        /*
        * 2017-09-07 09:15:07 INFO
        * SeckillServiceTest:86 -
        * SeckillExecution{seckillId=2, state=0, stateInfo='秒杀成功',
        * record=Record{seckillId=2, recordPhone='13544494187', recordSeckillCount=1, recordSeckillTime=Thu Sep 07 09:15:07 CST 2017,
        * seckill=Seckill{seckillId=2, seckillName='1000元秒iPad', seckillQuantity=199, seckillStartTime=Thu Sep 07 00:00:00 CST 2017, seckillEndTime=Fri Sep 08 00:00:00 CST 2017, seckillCreateTime=Tue Sep 05 00:00:00 CST 2017}}}
        * */
        int seckillId = 2;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        logger.info(exposer);
        try {
            if (exposer.isExposed()){
                String phone = "13544494187";
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, exposer.getMd5());
                logger.info(seckillExecution);
            }else{
                logger.info("秒杀尚未开始");
            }
        }catch (Exception e){
            logger.error(e);
        }

    }

}