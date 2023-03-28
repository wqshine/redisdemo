package com.example.redisdemo.configure;

import com.example.redisdemo.repo.RoleRepository;
import com.example.redisdemo.repo.UserRepository;
import com.example.redisdemo.security.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;


@EntityScan(basePackages = {"com.example.redisdemo.entity"})
@EnableJpaRepositories(basePackages = {"com.example.redisdemo.repo"})
@ComponentScan(basePackages = {"com.example.redisdemo.service", "com.example.redisdemo.controller","com.example.redisdemo.commentgenerate"})
@EnableJpaAuditing
@EnableCaching
@Configuration
@EnableWebSecurity(debug = true)
@EnableTransactionManagement
public class BaseConfig{
    private final UserRepository userRepository;
    public BaseConfig(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    /*密码加密，这里使用security框架提供的BCryptPasswordEncoder加密方式
     *如需自定义，请完善MyPasswordEncoder类
    */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl(userRepository);
    }

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.
                cors()
                .and()
                /*.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()*//*开启跨域保护*/
                .csrf().disable()/*禁用csrf(跨域请求伪造),即关闭跨域请求伪造保护功能，关闭后可能会出现csrf攻击,默认启用*/
                .rememberMe().tokenValiditySeconds(Integer.MAX_VALUE)
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/user/getCurrentUser").permitAll()/*不需要认证*/
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedHandler());

        return http.build();
    }

    @Resource
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//使用JSON的序列化对象，对数据key和value进行序列化转换
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//Jackson的工作类，序列化或反序列化操作
        ObjectMapper om = new ObjectMapper();
//设置序列化的可见性，第一个参数选择序列化哪些属性，第二参数选择修饰符权限属性（private或public等）,any表全部
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//设置错误类型，参数一指验证程序，参数二指该类不能为final属性
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(redisConnectionFactory);

//        template.setDefaultSerializer(jackson2JsonRedisSerializer);
//key序列化方式
        template.setKeySerializer(redisSerializer);
//value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
//value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }




    @Bean
    public CacheManager cacheManager() {
//创建string和JSON对象，分别对key和value数据进行管理
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
// 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();/*对空数据不操作*/

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * 后台对象转前端json
     * 用Hibernate5Module解决延迟加载属性的转换
     * 用Entity上的JsonIdentityInfo解决循环引用问题
     * 在登录getCurrentUser中获取myUserDetails.getUser().getRoles延迟加载报错，
     * 报错类型：JsonMappingException: failed to lazily initialize a collection of role
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        hibernate5Module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        objectMapper.registerModule(hibernate5Module);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }


//自定义redis操作封装工具

        /**
         * 对hash类型的数据操作
         *
         * @param redisTemplate
         * @return
         */
        @Bean
        public HashOperations<String, String, Object> hashOperations (RedisTemplate < String, Object > redisTemplate){
            return redisTemplate.opsForHash();
        }

        /**
         * 对redis字符串类型数据操作
         *
         * @param redisTemplate
         * @return
         */
        @Bean
        public ValueOperations<String, Object> valueOperations (RedisTemplate < String, Object > redisTemplate){
            return redisTemplate.opsForValue();
        }

        /**
         * 对链表类型的数据操作
         *
         * @param redisTemplate
         * @return
         */
        @Bean
        public ListOperations<String, Object> listOperations (RedisTemplate < String, Object > redisTemplate){
            return redisTemplate.opsForList();
        }

        /**
         * 对无序集合类型的数据操作
         *
         * @param redisTemplate
         * @return
         */
        @Bean
        public SetOperations<String, Object> setOperations (RedisTemplate < String, Object > redisTemplate){
            return redisTemplate.opsForSet();
        }

        /**
         * 对有序集合类型的数据操作
         *
         * @param redisTemplate
         * @return
         */
        @Bean
        public ZSetOperations<String, Object> zSetOperations (RedisTemplate < String, Object > redisTemplate){
            return redisTemplate.opsForZSet();
        }

}
