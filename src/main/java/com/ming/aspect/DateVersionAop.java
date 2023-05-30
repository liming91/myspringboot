package com.ming.aspect;

import com.ming.annotation.DateVersion;
import com.ming.annotation.TestAnnotation;
import com.ming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Author liming
 * @Date 2023/5/29 17:20
 */
@Aspect
@Component
@Slf4j
public class DateVersionAop {

    /**
     * 手动事务
     */
    @Resource
    DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    TransactionDefinition transactionDefinition;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Pointcut("@annotation(com.ming.annotation.DateVersion)")
    public void point() {
    }
    @Around("point()&&@annotation(dateVersion)")
    public Object doAround(ProceedingJoinPoint pjp, DateVersion dateVersion) throws Throwable {
        System.out.println("环绕通知：");
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            log.info("环绕通知的前置通知执行了...");
            //获取方法的签名
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            DateVersion annotation = method.getAnnotation(DateVersion.class);
            String tableName = annotation.tableName();
            String idName = annotation.idName();
            idName = idName.substring(0,1).toUpperCase()+ idName.substring(1);
            String versionName = annotation.versionName();
            versionName = versionName.substring(0,1).toUpperCase()+ versionName.substring(1);
            //获取参数
            Object[] args = pjp.getArgs();
            Object paramData = args[0];
            Class<?> paramDataClass = paramData.getClass();
            //获取id
            String dataId =  String.valueOf(paramDataClass.getMethod("get"+idName).invoke(paramData));
            //获取版本
            String dataVersion = String.valueOf(paramDataClass.getMethod("get"+versionName).invoke(paramData));
            if(StringUtils.isNotEmpty(dataVersion) && StringUtils.isNotEmpty(dataId) && !StringUtils.equals(dataVersion,"null") && !StringUtils.equals(dataId,"null") && !StringUtils.contains(dataId,",")){
                //更新版本,成功则代表版本号正确
                String newVersion = Integer.parseInt(dataVersion) + 1 + "";
                String updateSql = " UPDATE " + tableName + " SET VERSION = '" + newVersion + "'" + " WHERE USER_ID = '" + dataId + "' AND VERSION = " + dataVersion ;
                System.out.println(updateSql);
                int update = jdbcTemplate.update(updateSql);
                if (update == 0) {
                    throw new ServiceException("数据已发生变更,清刷新页面重试");  //可以改成自己的全局异常
                }
            }
            Object proceed = pjp.proceed();
            dataSourceTransactionManager.commit(transactionStatus);//正常完成手动提交
            return proceed;

        } catch (Exception e) {
            log.info("环绕通知的异常通知执行了...");
            e.printStackTrace();
            dataSourceTransactionManager.rollback(transactionStatus);//出现异常进行回滚
            throw new ServiceException(e.getMessage());

        } finally {
            log.info("环绕通知的最终通知执行了...");
        }

    }
}
