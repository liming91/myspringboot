package com.ming.aspect;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.annotation.DecryptField;
import com.ming.entities.Info;
import com.ming.util.http.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解密切面
 *
 * @Author liming
 * @Date 2023/11/3 10:26
 */
@Aspect
@Component
@Slf4j
public class DecryptAop {

    public final static Map<String, Object> map = new HashMap<>();

    @Pointcut("@annotation(com.ming.annotation.NeedDecrypt)")
    public void point() {
    }


    @Around("point()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("===============解密环绕通知开始===============");
        Object result = null;
        try {
            //执行目标方法
            result = proceedingJoinPoint.proceed();
            //判断目标方法的返回值类型
            if (result instanceof List) {
                for (Object tmp : ((List) result)) {
                    //数据脱敏处理逻辑
                    this.deepProcess(tmp);
                }
            } else {
                this.deepProcess(result);
            }

        } catch (Exception e) {
            log.error("解密失败{}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("===============解密环绕通知结束===============");
        return result;
    }

    private void deepProcess(Object obj) throws Exception {
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (("data").equals(declaredField.getName())) {
                    declaredField.setAccessible(true);
                    if (declaredField.get(obj) instanceof Page) {
                        Page page = (Page) declaredField.get(obj);
                        List list = page.getRecords();
                        for (Object o : list) {
                            Class<?> entityClazz = o.getClass();
                            Field[] fields = entityClazz.getDeclaredFields();
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(DecryptField.class)) {
                                    field.setAccessible(true);
                                    //加密数据的解密处理
                                    String value = this.decrpyt(field.get(o).toString());
                                    //把解密后的字段属性值重新赋值
                                    field.set(o, value);
                                }
                            }

                        }
                    }

                    if (declaredField.get(obj) instanceof List) {
                        List list = (List) declaredField.get(obj);
                        for (Object o : list) {
                            Class<?> resultClazz = o.getClass();
                            Field[] fields = resultClazz.getDeclaredFields();
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(DecryptField.class)) {
                                    field.setAccessible(true);
                                    //加密数据的解密处理
                                    String value = this.decrpyt(field.get(o).toString());
                                    //把解密后的字段属性值重新赋值
                                    field.set(o, value);
                                }
                            }
                        }
                    }

                }

            }
        }
    }


    /**
     * 解密
     *
     * @param arg
     * @return
     * @throws Exception
     */
    public String decrpyt(String arg) throws Exception {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(), "my_aop_test".getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        String decryptStr = aes.decryptStr(arg);
        return decryptStr;
    }

}
