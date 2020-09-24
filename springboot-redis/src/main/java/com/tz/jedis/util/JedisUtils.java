package com.tz.jedis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author tz
 * @Classname JedisUtils
 * @Description jedis util
 * @Date 2019-11-21 16:48
 */
@Component
@Slf4j
public class JedisUtils {
    @Autowired
    private JedisPool jedisPool;
    private static JedisUtils jedisUtils;

    /**
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void JedisUtil() {
        jedisUtils = this;
        jedisUtils.jedisPool = jedisPool;
    }

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
    public static String get(String key, int indexDb) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            value = jedis.get(key);
        } catch (Exception e) {

            log.error("jedis get error [{}]", e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 普通的set值
     *
     * @param key
     * @param val
     * @param indexDb
     */
    public static void set(String key, String val, int indexDb) {
        Jedis jedis = null;
        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            jedis.set(key, val);
        } catch (Exception e) {

            log.error("jedis set error [{}]", e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key删除 可以穿入多个key
     *
     * @param key
     * @param indexDb
     * @return
     */
    public static int delete(String[] key, int indexDb) {
        Jedis jedis = null;
        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            jedis.del(key);
            log.info("redis del key=[{}]", key);
            return 1;
        } catch (Exception e) {
            log.error("jedis del error [{}]", e.getMessage());
            return 0;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * hash set 设置值
     *
     * @param key
     * @param hashKey
     * @param value
     * @param indexDb
     * @return
     */
    public static Long hset(String key, String hashKey, String value, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            Long hset = jedis.hset(key, hashKey, value);
            return hset;
        } catch (Exception e) {
            log.error("jedis hset error [{}]", e.getMessage());
            return 0L;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key获取所有value
     *
     * @param key
     * @param indexDb
     * @return
     */
    public static Map<String, String> hgetAll(String key, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            Map<String, String> stringStringMap = jedis.hgetAll(key);
            return stringStringMap;
        } catch (Exception e) {
            log.error("jedis hget error [{}]", e.getMessage());
            return new HashMap<>(16);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key和hashkey获取
     *
     * @param key
     * @param hashKey
     * @param indexDb
     * @return
     */
    public static String hget(String key, String hashKey, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            String hget = jedis.hget(key, hashKey);
            return hget;
        } catch (Exception e) {
            log.error("jedis hget error [{}]", e.getMessage());
            return "";
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 判断hashkey是否存在
     *
     * @param key
     * @param hashKey
     * @param indexDb
     * @return
     */
    public static Boolean hexists(String key, String hashKey, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            Boolean hexists = jedis.hexists(key, hashKey);
            return hexists;
        } catch (Exception e) {
            log.error("jedis hexists error [{}]", e.getMessage());
            return false;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据排序获取分页数据，修改min，max可以在某一个区间选择
     *
     * @param key
     * @param offset
     * @param count
     * @param indexDb
     * @return
     */
    public static Set<String> zrangeByScore(String key, final int offset, final int count, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisUtils.jedisPool.getResource();
            jedis.select(indexDb);
            // min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
            Set<String> set = jedis.zrangeByScore(key, "-inf", "+inf", offset, count);
            return set;
        } catch (Exception e) {
            log.error("jedis zrangeByScore error [{}]", e.getMessage());
            return null;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 释放资源
     *
     * @param resource
     */
    protected static void returnResource(Jedis resource) {
        resource.resetState();
    }
}
