package com.ranger.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 描述:
 *
 * @author chaiwei
 * @create 2018-12-28 下午5:10
 */
@Component
public class RedisStringUtil extends RedisUtil {

    /**
     * 添加记录,如果记录已存在将覆盖原有的value
     * @param key
     * @param value
     * @return 状态码
     * */
    public String set(String key, String value) {
        Jedis jedis = getJedis();
        String status = jedis.set(key, value);
        closeJedis(jedis);
        return status;
    }

    /**
     * 获取字符串
     * @param key
     * @return 值
     * */
    public String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        closeJedis(jedis);
        return value;
    }

    /**
     * 获取整型
     * @param key
     * @return
     */
    public Integer getInt(String key) {
        String value = get(key);
        if(value != null){
            return new Integer(value);
        }
        return 0;
    }

    /**
     * 模糊查询
     * @param key
     * @return
     */
    public Set<String> getLike(String key){
        Jedis jedis = getJedis();
        Set<String> keysList = jedis.keys(key + "*");
        closeJedis(jedis) ;
        return keysList;
    }

    /**
     * 模糊删除
     * @param keys
     */
    public void delLike(String keys){
        Jedis jedis = getJedis();
        Set<String> keysList = getLike(keys);
        for (String key : keysList) {
            jedis.del(key);
        }
        closeJedis(jedis) ;
    }
}
