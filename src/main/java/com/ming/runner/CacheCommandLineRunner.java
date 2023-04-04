package com.ming.runner;

import com.ming.constant.Constants;
import com.ming.service.IUserCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created By Ranger on 2023/2/24.
 */
@Slf4j
@Component
@AllArgsConstructor
public class CacheCommandLineRunner implements CommandLineRunner {
    
    private final IUserCache userCache;
    
    @Override
    public void run(String... args) throws Exception {
        userCache.refreshCache(Constants.CACHE_USERS);
    }
}
