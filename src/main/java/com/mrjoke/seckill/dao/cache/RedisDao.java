package com.mrjoke.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.mrjoke.seckill.entities.Seckill;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private Logger logger = Logger.getLogger(this.getClass());
    private final JedisPool jedisPool;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port) {
        this.jedisPool = new JedisPool(ip,port);
    }

    /**
     * 访问reids缓存拿到seckill对象
     *
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(int seckillId){
        //redis操作逻辑
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            //redis并没有实现内部序列化操作
            // get-> byte[] -> 反序列化 -> Object (Seckill)
            //采用自定义的序列化
            //protostuff: pojo.
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null){
                //从缓存中获取到
                Seckill seckill = schema.newMessage();//空对象
                ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                //seckill 被反序列化
                return seckill;
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 将对象缓存到redis中
     *
     * @param seckill
     * @return
     */
    public String putSeckill(Seckill seckill){
        //set Object(Seckill) -> 序列化 -> byte[]
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //超时缓存
            int timeout = 60 * 60;
            String result = jedis.setex(key.getBytes(), timeout, bytes);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
