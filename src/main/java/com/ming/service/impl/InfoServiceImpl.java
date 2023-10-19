package com.ming.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.Info;
import com.ming.enums.ResultCode;
import com.ming.exception.Asserts;
import com.ming.exception.ServiceException;
import com.ming.service.InfoService;
import com.ming.mapper.InfoMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * @author Y
 * @description 针对表【info】的数据库操作Service实现
 * @createDate 2023-10-11 14:04:53
 */
@Service
@AllArgsConstructor
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>
        implements InfoService {

    @Override
    public IPage<Info> infoPage(Integer pageNo, Integer pageSize) {
        Page<Info> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<Info>();
        queryWrapper.eq(Info::getDel, 0);
        Page<Info> infoPage = this.baseMapper.selectPage(page, queryWrapper);
        return infoPage;
    }

    /**
     *
     * 解决方法：方案一：给名称不做唯一索引，新增前判断是否有未删除的同一个名称的，前后端同时判断
     * 方案二：名称做编号：名称编号：名称_已删除_N(删除的次数)A_已删除_N 先模糊查询名称_ 获取的总条数加+1的数据同时更新进去
     * 以上存在并发问题加锁解决保持原子性不然会造成数据错乱
     * @param id
     * @return
     */
    @Override
    public ResultCode updateInfo(String id) {
        int rows = this.baseMapper.update(null,
                new LambdaUpdateWrapper<Info>()
                        .set(Info::getDel, 1)
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
        Integer count = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Info::getDel,0)
                .eq(Info::getName, info.getName())
                .ne(StringUtils.isNotBlank(info.getId()), Info::getId, info.getId()).count();
        if(count > 0){
            Asserts.fail("信息名称重复！");
        }
        boolean flag;
        info.setTime(new Date());
        info.setDel(0);
        try {
            flag = this.saveOrUpdate(info);
        } catch (Exception e) {
            flag = false;
            log.error("信息添加异常：{}", e);
            throw new ServiceException("信息添加异常"+e.getMessage());
        }
        return flag;
    }
}




