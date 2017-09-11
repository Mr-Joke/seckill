package com.mrjoke.seckill.service.impl;

import com.mrjoke.seckill.dao.RecordDao;
import com.mrjoke.seckill.dao.SeckillDao;
import com.mrjoke.seckill.dao.cache.RedisDao;
import com.mrjoke.seckill.dto.Exposer;
import com.mrjoke.seckill.dto.SeckillExecution;
import com.mrjoke.seckill.entities.Record;
import com.mrjoke.seckill.entities.Seckill;
import com.mrjoke.seckill.enums.SeckillStateEnum;
import com.mrjoke.seckill.exception.RepeatSeckillException;
import com.mrjoke.seckill.exception.SeckillCloseException;
import com.mrjoke.seckill.exception.SeckillException;
import com.mrjoke.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisDao redisDao;
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
        //优化点：缓存优化，超时的基础上维护一致性
        //1.从redis缓存中拿对象
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //2.从mysql中拿对象
            seckill = seckillDao.selectById(seckillId);
            if (seckill == null){
                return new Exposer(false,seckillId);
            }
            //3.放入到redis中
            redisDao.putSeckill(seckill);
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
        /*
        * 简单优化：减少行级锁的持有时间
        * 调换insert插入明细和update减库存的操作位置，由于有事务回滚机制，调换位置并不影响业务；
        * 因为update操作会加行级锁，先update再insert会在加锁期间发送insert的sql语句给mysql，期间
        * 多一次网络延迟的时间；
        * 先insert再update，由于update操作加锁，insert不用，所以在加锁前的
        * insert语句可以说是网络并行的，到update的时候才加锁，所以行级锁的持有时间减少了一半。
        * */
        Date now = new Date();
        try {
            //记录购买行为
            int affectRecord = recordDao.insertRecord(seckillId, userPhone);
            if (affectRecord <= 0){
                throw new RepeatSeckillException("repeated killing !!!");
            }else{
                //减库存，热点商品竞争
                int affectCount = seckillDao.reduceQuantity(seckillId, now);
                if (affectCount <= 0){
                    //rollback
                    throw new SeckillCloseException("seckill has closed !!!");
                }else{
                    //秒杀成功，返回携带秒杀商品的信息.commit
                    Record record = recordDao.selectByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,record);
                }
            }
        } catch (SeckillCloseException | RepeatSeckillException e1){
            throw e1;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //将所有编译器异常转换成运行期异常
            throw new SeckillException("seckill inner error : " + e.getMessage());
        }
    }

    /**
     * 使用存储过程执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @Override
    public SeckillExecution executeSeckillProcedure(int seckillId, String userPhone, String md5)throws SeckillException{
        //校验MD5
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            //数据被篡改
            throw new SeckillException("data has been modified !!!");
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put("killPhone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            //执行存储过程，result被赋值
            seckillDao.killByProcedure(map);
            Integer result = MapUtils.getInteger(map, "result", -2);
            if (result == 1){
                Record record = recordDao.selectByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,record);
            }else{
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }
}
