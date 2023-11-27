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
 *  * 1.其他用户已经改变了数据状态,但是当前用户在改变数据之后未刷新页面,数据状态还是之前的状态,出现重复操作(比如工单数据,管理员一已经进行派单,但是管理员二未刷新页面,再次进行了派单)
 *  * 2.多个用户统一时间对一条数据操作,造成重复操作
 *  * 首先对于问题1可以采用乐观锁的方式,在每次用户更新数据的时候,判断他携带的数据版本和当前库里的版本是否一样,如果不一样则通知用户刷新页面获取最新状态,如果一样,则进行版本+1,
 *  * 但是该办法不能解决同一时间用户一同操作(用户一版本更新成功,但是代码还未执行完毕,用户二拿到新的版本再次来进行更新,数据多次操作)
 *  * 问题二可以再进行加锁的方式,更新版本和更新数据同一时间只能又一个用户完成,或者使用mysql的事务来解决,更新版本和业务操作在同一个事务中,同时成功才可以进行下一步的版本更新
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
