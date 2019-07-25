package com.ranger.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 描述:
 *
 * @author chaiwei
 * @create 2018-11-03 上午10:26
 */
@Component
public class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 从JedisPool中获取jedis对象
     */
    public Jedis getJedis(){
        if(jedisPool == null){
            throw new NullPointerException();
        }
        return jedisPool.getResource();
    }

    /**
     * 回收jedis
     */
    public void closeJedis(Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }

    /**
     * 获取ID
     */
    public long getNextID(String key, Long newkey){
        Jedis jedis = getJedis();
        long nextID = jedis.incrBy(key, newkey);
        System.out.println(nextID);
        closeJedis(jedis);
        return nextID;
    }

}