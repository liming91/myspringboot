package com.ming;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author liming
 * @Date 2023/9/15 16:34
 */
public class CacheTest {
    /**
     * Caffeine的底层数据存储采用ConcurrentHashMap。
     * <p>
     * Caffeine是Spring 5默认支持的Cache，可见Spring对它的看重，Spring抛弃Guava转向了Caffeine。
     * <p>
     * Caffeine可以看作是Guava Cache的增强版，采用了一种结合LRU、LFU优点的算法：W-TinyLFU，在性能上有明显的优越性
     */
    @Test
    public void caffeineTest() throws InterruptedException {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                // 初始容量
                .initialCapacity(100)
                // 最大缓存数
                .maximumSize(1000)
                // 过期时间
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 失效时触发，不会自动触发
                .removalListener((k, v, removeCause) -> {
                    System.out.println("--------------" + k);
                }).recordStats()
                .build();
        cache.put("a", "b");
        System.out.println(cache.getIfPresent("a")); // b
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName());
//        cache.invalidate("a");  // 触发removalListener
        System.out.println(cache.getIfPresent("a")); // null
        System.out.println(cache.get("a", k -> { // 不存在则填充值，之前的过期触发removalListener
            System.out.println("进入get");
            return k;
        }));
    }

    /**
     * Guava Cache
     * Guava是Google团队开源的一款 Java 核心增强库，包含集合、并发原语、缓存、IO、反射等工具箱，性能和稳定性上都有保障，应用十分广泛。Guava Cache支持很多特性：
     * <p>
     * 支持最大容量限制
     * 支持两种过期删除策略（插入时间和访问时间）
     * 支持简单的统计功能
     * 基于LRU算法实现
     */
    @Test
    public void guavaCache() throws InterruptedException, ExecutionException {
        LoadingCache<String, String> loadingCache
                //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
                = CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
//                .concurrencyLevel(8)
                //设置写缓存后8秒钟过期
                .expireAfterWrite(2, TimeUnit.SECONDS)
//                .expireAfterAccess(10, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener((all) -> {
                    System.out.println("移除key：" + all.getKey());
                })
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String s) throws Exception {
                                return "b";
                            }
                        }
                );
        String a = loadingCache.get("a"); // 不存在key为“a”则触发load（） return "b";
//        loadingCache.invalidate("a"); // 手动失效key，触发removalListener， System.out.println("移除key："+all.getKey());
        TimeUnit.SECONDS.sleep(3);
        loadingCache.get("a"); // 过期触发removalListener， System.out.println("移除key："+all.getKey());

    }


    /**
     * Ehcache 特点
     * 其缓存的数据可以放在内存里面,也可以放在硬盘上。
     * <p>
     * ehcache的核心是cacheManager，cacheManager是用来管理cache(缓存)的。
     * <p>
     * 一个应用下可以有多个cacheManager，而一个cacheManager下又可以有多个cache
     * <p>
     * cache内部保存的是一个的element,一个element中保存的是一个key和value的配对。
     * <p>
     * 1、快速
     * <p>
     * 2、简单
     * <p>
     * 3、多种缓存策略
     * <p>
     * 4、缓存数据有两级：内存和磁盘，因此无须担心容量问题
     * <p>
     * 5、缓存数据会在虚拟机重启的过程中写入磁盘
     * <p>
     * 6、可通过RMI、可插入APi等方式进行分布式缓存
     * <p>
     * 7、具有缓存和缓存管理器的监听接口
     * <p>
     * 8、支持多缓存管理器示例，以及一个实例的多个缓存区域
     * <p>
     * 9、提供hibernate的缓存实现
     * <p>
     * 同Caffeine和Guava Cache相比，Encache的功能更加丰富，扩展性更强
     * <p>
     * ehcache也有缓存共享方案，不过是通过RMI或者Jgroup多播方式进行广播缓存通知更新，缓存共享复杂，维护不方便；简单的共享可以，但涉及缓存恢复、大数据缓存，则不适合。
     */
    @Test
    public void ehcache() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("cache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(20))) //声明一个容量为20的堆内缓存
                .build(true);
//        cacheManager.init(); // true
//        Cache<Long, String> cache =
//        cacheManager.getCache("cache", Long.class, String.class);
//        cache.put();
//        cache.get()
        org.ehcache.Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(20)))//设置过期 可自定义过期策略
        );
        myCache.put(1L, "one!");
        String value = myCache.get(1L);
        cacheManager.removeCache("cache");
        cacheManager.close();

    }

}
