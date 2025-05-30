package com.ming.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.entities.Info;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.enums.ResultCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Y
 * @description 针对表【info】的数据库操作Service
 * @createDate 2023-10-11 14:04:53
 */
public interface InfoService extends IService<Info>,IPolymorphicService {

    IPage<Info> infoPage (Integer pageNo, Integer pageSize);

    ResultCode updateInfo(String id);

    boolean saveOrUpdateInfo(Info info);

    List<Map<String, Object>> infoList();

    void changeInfo();
}
