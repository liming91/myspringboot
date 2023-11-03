package com.ming.aspect;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.ming.annotation.EncryptField;
import com.ming.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加密切面
 *
 * @Author liming
 * @Date 2023/11/3 10:26
 */
@Aspect
@Component
@Slf4j
public class EncryptAop {

    public final static Map<String, Object> map = new HashMap<>();

    @Pointcut("@annotation(com.ming.annotation.NeedEncrypt)")
    public void point() {
    }


    @Before(value = "point()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("============加密切入点=============");
        log.info("目标方法名为: {}", joinPoint.getSignature().getName());
        log.info("目标方法所属类的简单类名: {}", joinPoint.getSignature().getDeclaringType().getSimpleName());
        log.info("目标方法所属类的类名: {}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("目标方法声明类型: {}", Modifier.toString(joinPoint.getSignature().getModifiers()));
        log.info("===============================参数========================");
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("===============加密环绕通知开始===============");
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            if (args.length > 0) {
                for (Object arg : args) {
                    if (args != null) {
                        if (arg instanceof List) {
                            for (Object tmp : ((List) arg)) {
                                //加密处理
                                this.deepProcess(tmp);
                            }
                        } else {
                            this.deepProcess(arg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("加密失败{}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("===============加密环绕通知结束===============");
        return proceedingJoinPoint.proceed();
    }

    private void deepProcess(Object obj) throws Exception {
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //判断字段属性上是否标记了@EncryptField注解
                if(declaredField.isAnnotationPresent(EncryptField.class)){
                    //如果判断结果为真，则取出字段属性值，进行加密、重新赋值
                    declaredField.setAccessible(true);
                    Object valObj = declaredField.get(obj);
                    if(valObj!=null){
                        String value = valObj.toString();
                        //开始敏感字段属性值加密
                        String decrypt = this.encrypt(value);
                        //把加密后的字段属性值重新赋值
                        declaredField.set(obj, decrypt);

                    }
                }
            }
        }
    }


    /**
     * 加密
     * @param arg
     * @return
     * @throws Exception
     */
    public String encrypt(String arg) throws Exception {
        //这里特别注意一下，对称加密是根据密钥进行加密和解密的，加密和解密的密钥是相同的，一旦泄漏，就无秘密可言，
        //“my_aop_test”就是我自定义的密钥，这里仅作演示使用，实际业务中，这个密钥要以安全的方式存储；
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(), "my_aop_test".getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        String encryptValue = aes.encryptBase64(arg);
        return encryptValue;
    }
}
