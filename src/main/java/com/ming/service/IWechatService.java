package com.ming.service;

import com.ming.entities.query.AppletLoginQuery;
import com.ming.util.http.Result;

/**
 * @Author liming
 * @Date 2023/6/16 14:43
 */
public interface IWechatService {
    String getToken();

    Result codeGetUser(AppletLoginQuery appletLoginQuery);
}
