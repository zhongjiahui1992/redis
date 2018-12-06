package com.zjh.redis.client;

import com.zjh.redis.util.SerializableUtil;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if(!StringUtils.isEmpty(bytes)) {
                return SerializableUtil.toObject(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 缓存对象类型
     * @param key
     * @param obj
     */
    public void setObject(String key, Object obj){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key.getBytes(), SerializableUtil.toByteArray(obj));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取string类型
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 缓存string类型
     * @param key
     * @param obj
     */
    public void set(String key, String obj){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, obj);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return boolean
     */
    public boolean existsKey(String key) {
        Jedis jedis = null;
        boolean exis = false;
        try {
            jedis = jedisPool.getResource();
            exis = jedis.exists(key);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return exis;
    }

    /**
     * 清空key
     * @throws Exception
     */
    public void flushAll() throws Exception {
        Jedis jedis = jedisPool.getResource();
        jedis.flushAll();
        jedis.close();
    }

    /**
     * 根据key删除缓存，返回数量
     * @param keys
     * @return
     * @throws Exception
     */
    public long del(String... keys) throws Exception {
        Jedis jedis = jedisPool.getResource();
        long count = jedis.del(keys);
        jedis.close();
        return count;
    }

    /**
     * 根据key删除缓存，返回数量
     * @param keys
     * @return
     * @throws Exception
     */
    public long del(byte[]... keys) throws Exception {
        Jedis jedis = jedisPool.getResource();
        long count = jedis.del(keys);
        jedis.close();
        return count;
    }


}
