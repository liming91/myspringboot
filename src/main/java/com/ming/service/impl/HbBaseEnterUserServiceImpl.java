package com.ming.service.impl;

import com.google.common.collect.Lists;

import com.ming.entities.GrantUser;
import com.ming.entities.HbBaseEnterUser;
import com.ming.mapper.BatchInsertUserMapper;
import com.ming.service.IHbBaseEnterUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HbBaseEnterUserServiceImpl implements IHbBaseEnterUserService {

    @Autowired
    private BatchInsertUserMapper hbBaseEnterUserMapper;

    @Autowired
    private ThreadPoolTaskExecutor  threadPoolTaskExecutor; // 注入线程池



    @Override
    public int addGrantEnter(List<HbBaseEnterUser> hbBaseEnterUser) {
        int flag = 0;
        try {
            String id = null;
            if (hbBaseEnterUser.size() > 0) {
                List<HbBaseEnterUser> enterUserList = new ArrayList<>();
                for (HbBaseEnterUser enterUser : hbBaseEnterUser) {
                    id = enterUser.getId();
                    List<GrantUser> grantUsers = enterUser.getGrantUsers();
                    for (GrantUser grantUser : grantUsers) {
                        HbBaseEnterUser enterUserInfo = new HbBaseEnterUser();
                        enterUserInfo.setId(id);
                        enterUserInfo.setEnterId(enterUser.getEnterId());
                        enterUserInfo.setUserId(grantUser.getUserId());
                        enterUserInfo.setAdder(enterUser.getAdder());
                        enterUserList.add(enterUserInfo);
                    }
                }
                ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(
                        threadPoolTaskExecutor);
                List<List<HbBaseEnterUser>> lists = Lists.partition(enterUserList, 500);
                if (StringUtils.isEmpty(id)) {
                    //删除
                    lists.forEach(x -> {
                        completionService.submit(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return hbBaseEnterUserMapper.batchDeleteEnterUser(x);
                            }
                        });
                    });
                    //新增
                    lists.forEach(x -> {
                        completionService.submit(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return hbBaseEnterUserMapper.batchInsertEnterUser(x);
                            }
                        });
                    });
                    flag = 1;

                } else {
                    //修改
                    lists.forEach(x -> {
                        completionService.submit(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return hbBaseEnterUserMapper.batchUpdateEnterUser(x);
                            }
                        });
                    });

                }
                // 这里是让多线程开始执行
                lists.forEach(item -> {
                    try {
                        completionService.take().get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("异步线程报错：{}", e.getMessage());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("报错：{}", e.getMessage());
        }

        return flag;
    }


}
