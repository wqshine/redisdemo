package com.example.redisdemo.service;

import com.example.redisdemo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {
    /**
     * cacheNames 设置缓存的值
     * key：指定缓存的key，这是指参数id值。key可以使用spEl表达式
     * @return
     */
    @Cacheable(value = "userCache", key = "'getUserById'", unless="#result==null")
    public User getUserById(String id) {
        log.info("获取用户start...");
        User user =new User();
        user.setId(1);
        user.setUserName("李逍遥");
        user.setPassword("123456");
        return user;
    }
    /**
     * 创建用户，同时使用新的返回值的替换缓存中的值
     * 如果存在其他缓存包含改更新数据，则需更改或删除对应缓存
     */
    @CachePut(value = "userCache", key = "'getUserById'")
    public User createUser(User user) {
        return user;
    }
    /**
     * 对符合key条件的记录从缓存中移除
     */
    @Caching(evict = {@CacheEvict(value = "userCache", key = "#id")})
    public void deleteById(String id) {
        log.info("删除用户start...");
        /*这里调用方法删除数据库对应数据*/
    }
}
