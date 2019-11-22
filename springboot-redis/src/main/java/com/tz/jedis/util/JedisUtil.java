package com.tz.jedis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * @author tz
 * @Classname JedisUtils
 * @Description jedis util
 * @Date 2019-11-21 16:48
 */
@Component
@Slf4j
public class JedisUtil {
    @Autowired
    private static JedisPool jedisPool;

    /**
     * <p>
     * 通过key获取储存在redis中的value
     * </p>
     * <p>
     * 并释放连接
     * </p>
     *
     * @param key
     * @param indexDb 选择redis库 0-15
     * @return 成功返回value 失败返回null
     */
    public String get(String key,int indexDb) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            value = jedis.get(key);
            log.info("jedis get key = [{}],value = [{}]",key,value);
        } catch (Exception e) {

            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public void set(String key,String val,int indexDb){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            jedis.set(key,val);
            log.info("redis 存入key=[{}],val=[{}]",key,val);
        } catch (Exception e) {

            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }
    public int delete(String[] key,int indexDb){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            jedis.del(key);
            log.info("redis del key=[{}]",key);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        } finally {
            returnResource(jedis);
        }
    }


    /**
     * 释放资源
     * @param resource
     */
    protected void returnResource(Jedis resource) {
        log.info("return redis resource ");
        resource.resetState();
    }
}
