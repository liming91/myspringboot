package com.ming.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author liming
 * @Date 2024/2/5 14:55
 */

@Service
public interface TokenUtilService {

     String generateToken(String value);

     boolean validToken(String token, String value);



    }
