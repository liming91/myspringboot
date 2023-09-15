package com.ming;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Test;

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
//        cache.invalidate("a");  // 触发removalListener
        System.out.println(cache.getIfPresent("a")); // null
        System.out.println(cache.get("a", k -> { // 不存在则填充值，之前的过期触发removalListener
            System.out.println("进入get");
            return k;
        }));
    }
}
