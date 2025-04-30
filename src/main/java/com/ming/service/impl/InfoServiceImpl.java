package com.ming.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.Info;
import com.ming.entities.SysUser;
import com.ming.enums.ResultCode;
import com.ming.exception.Asserts;
import com.ming.exception.ServiceException;
import com.ming.mapper.SysUserMapper;
import com.ming.mapper.UserMapper;
import com.ming.service.InfoService;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Y
 * @description 针对表【info】的数据库操作Service实现
 * @createDate 2023-10-11 14:04:53
 */
@Service
@AllArgsConstructor
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>
        implements InfoService {

    private static final Logger logger = LoggerFactory.getLogger(InfoServiceImpl.class);

    private final SysUserMapper sysUserMapper;

    @Override
    public IPage<Info> infoPage(Integer pageNo, Integer pageSize) {
        Page<Info> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<Info>();
        queryWrapper.eq(Info::getDel_fag, 0);
        Page<Info> infoPage = this.baseMapper.selectPage(page, queryWrapper);
        return infoPage;
    }

    /**
     * 解决方法：方案一：给名称不做唯一索引，新增前判断是否有未删除的同一个名称的，前后端同时判断
     * 方案二：名称做编号：名称编号：名称_已删除_N(删除的次数)A_已删除_N 先模糊查询名称_ 获取的总条数加+1的数据同时更新进去
     * 以上存在并发问题加锁解决保持原子性不然会造成数据错乱
     * 方案三：把email和del_fag（删除标记）作为组合唯一索引（索引类型为unique），然后在新增记录的时候del_fag设置‘0’表示未删除，
     * 删除记录的时候把del_flag设置为null（不是空字符串）。有人可能有疑问，如果name=‘a’
     * 和del_flag=null的记录有2条那不是不唯一了吗，就是数据库会报错（Duplicate entry '123-0' for key '索引名称'）
     * ？答案是不会，实测结果不会，具体原因看官网解释。我们只保证name='a' del_flag = 0 的记录只有一条即可。
     * 可以把删除记录移到另一张表删除记录表保证业务数据唯一性
     * 方案4：名称_删除字段_删除时间做联合索引，删除时将删除时间更新，状态改为已删除,新增时时间戳为0
     *
     * @param id
     * @return
     */
    @Override
    public ResultCode updateInfo(String id) {
        int rows = this.baseMapper.update(null,
                new LambdaUpdateWrapper<Info>()
                        .set(Info::getDel_fag, 1)
                        .set(Info::getDel_time, System.currentTimeMillis())
                        .eq(Info::getId, id));
        if (rows > 0) {
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.REQUEST_FAIL;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateInfo(Info info) {
        //方案1添加时做删除名称校验
//        Integer count = new LambdaQueryChainWrapper<>(baseMapper)
//                .eq(Info::getDel,0)
//                .eq(Info::getName, info.getName())
//                .ne(StringUtils.isNotBlank(info.getId()), Info::getId, info.getId()).count();
//        if(count > 0){
//            Asserts.fail("信息名称重复！");
//        }
        boolean flag;
        info.setTime(new Date());
        info.setDel_fag(0);
        info.setDel_time(0L);
        try {
            flag = this.saveOrUpdate(info);
        } catch (Exception e) {
            flag = false;
            log.error("信息添加异常：{}", e);
            throw new ServiceException("信息名称重复" + e.getMessage());
        }
        return flag;
    }


    @Override
    public List<Map<String, Object>> infoList() {
        return this.baseMapper.getInfoMap();
    }

    @Transactional
    @Override
    public void changeInfo() {
        logger.info("changeInfo  start 为了验证锁等待 和死锁 +++++++++++++++++++++++++++++++++");
        Info info = this.getById(1);
        logger.info("changeInfo:{}", info);

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>().set(SysUser::getPhonenumber, "13200002222")
                .eq(SysUser::getUserId, "1");
        sysUserMapper.update(null, updateWrapper);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        //删除info加了锁
        this.removeById("1");
        logger.info("changeInfo  end +++++++++++++++++++++++++++++++++++");
    }
}




