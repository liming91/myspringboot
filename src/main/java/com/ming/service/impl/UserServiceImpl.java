package com.ming.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.SysUser;
import com.ming.mapper.InfoMapper;
import com.ming.mapper.SysUserMapper;
import com.ming.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author liming
 * @Date 2025/4/30 11:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private InfoMapper infoMapper;

    /**
     * bug: 删除info加了锁 更新user加了锁 造成死锁
     *
     * 死锁 必要条件 多线程的情况下 持有对方的
     * <p>
     * 锁等待
     * <p>
     * <p>
     * 错误方案：
     * 并发情况下，多个线程拿到了同一条记录的快照，在可复读的隔离级别下，只有一个进程会删除成功，其他线程会因为各自的间隙锁死锁
     * -delete操作+不存在的记录+REPEATABLE READ 可重复度-会触发间隙锁
     * -update操作+范围查询+REPEATABLE READ会触发间隙锁
     * 这里是精准删除.改成主键软删除就可以解决了
     * <p>
     * 正确解读：
     * 锁等待和死锁不能混淆了，这里只能叫锁等待，其他线程等待持有锁的线程释放锁，正常
     * 情况下代码执行毫秒级就能执行完就能释放锁，其他线程就能获得锁继续执行。死锁，是
     * 互相等待，用于等不到释放，直至系统超时
     *
     *
     * 查找问题：代码日志 和数据库SHOW ENGINE INNODB STATUS  DEADLOCK 死锁位置
     * 解决方案：两边保持一直 先删除info 再更新user
     *
     */
    @Transactional
    @Override
    public void lockUser() {
        //多线程情况下业务操作形成交叉 造成死锁或者叫闭环
        logger.info("lockUser  start 为了验证锁等待 和死锁+++++++++++++++++++++++++++++++++");


        infoMapper.deleteById("1");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        //更新user加了锁
        this.lambdaUpdate().set(SysUser::getPhonenumber,"13200001111").eq(SysUser::getUserId,"1").update();

        logger.info("lockUser  end +++++++++++++++++++++++++++++++++++");


    }
}
