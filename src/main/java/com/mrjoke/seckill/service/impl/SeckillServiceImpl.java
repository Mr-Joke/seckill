package com.mrjoke.seckill.service.impl;

import com.mrjoke.seckill.dao.RecordDao;
import com.mrjoke.seckill.dao.SeckillDao;
import com.mrjoke.seckill.dto.Exposer;
import com.mrjoke.seckill.dto.SeckillExecution;
import com.mrjoke.seckill.entities.Record;
import com.mrjoke.seckill.entities.Seckill;
import com.mrjoke.seckill.enums.SeckillStateEnum;
import com.mrjoke.seckill.exception.RepeatSeckillException;
import com.mrjoke.seckill.exception.SeckillCloseException;
import com.mrjoke.seckill.exception.SeckillException;
import com.mrjoke.seckill.service.SeckillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Project: seckill
 * @Author: Mrzhou
 * @Date: 2017/9/6 21:09
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = Logger.getLogger(SeckillServiceImpl.class);
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RecordDao recordDao;
    //MD5的盐值字符串，用于混淆md5
    private final String SLAT = "kjdhsfkshdfkljf123123576^*&^&*!LHLKESHR";

    /**
     * 获取秒杀商品列表
     * @return
     */
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.selectAll(0,4);
    }

    /**
     * 根据id查询秒杀商品
     * @param seckillId
     * @return
     */
    @Override
    public Seckill getSeckillById(int seckillId) {
        return seckillDao.selectById(seckillId);
    }

    /**
     * 暴露秒杀接口地址
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exposeSeckillUrl(int seckillId) {
        //根据id查询秒杀商品
        Seckill seckill = seckillDao.selectById(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        //判断秒杀是否开启
        long start = seckill.getSeckillStartTime().getTime();
        long end = seckill.getSeckillEndTime().getTime();
        long current = new Date().getTime();
        if (current < start || current > end){
            //秒杀未开启
            return new Exposer(false, seckillId,current,start,end);
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId) ;
        return new Exposer(true,md5,seckillId);
    }

    /**
     * 获取md5加密字符串
     * @param seckillId
     * @return
     */
    private String getMD5(int seckillId) {
        String base = seckillId + "/" + SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws RepeatSeckillException
     * @throws SeckillCloseException
     * @throws SeckillException
     */
    @Override
    @Transactional
    /*
    * 使用注解控制事务方法的优点：
    *  1.开发团队达成一致的约定，明确标注事务方法的编程风格。
    *  2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作，RPC/HTTP请求，或者剥离到事务方法外。
    *  3.不是所有方法都需要事务，如只有一条修改操作，只读操作不需要事务。
    * */
    public SeckillExecution executeSeckill(int seckillId, String userPhone, String md5) throws RepeatSeckillException, SeckillCloseException, SeckillException {
        //校验MD5
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            //数据被篡改
            throw new SeckillException("data has been modified !!!");
        }
        //执行秒杀逻辑: 1.减库存 2.增加秒杀记录（记录购买行为）
        Date now = new Date();
        try {
            //减库存
            int affectCount = seckillDao.reduceQuantity(seckillId, now);
            if (affectCount <= 0){
                throw new SeckillCloseException("seckill has closed !!!");
            }else{
                //记录购买行为
                int affectRecord = recordDao.insertRecord(seckillId, userPhone);
                if (affectCount <= 0){
                    throw new RepeatSeckillException("repeated killing !!!");
                }else{
                    //秒杀成功，返回携带秒杀商品的信息
                    Record record = recordDao.selectByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,record);
                }
            }
        } catch (SeckillCloseException e1){
            throw e1;
        } catch (RepeatSeckillException e2){
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //将所有编译器异常转换成运行期异常
            throw new SeckillException("seckill inner error : " + e.getMessage());
        }
    }
}
