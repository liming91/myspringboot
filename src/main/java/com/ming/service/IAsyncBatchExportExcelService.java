package com.ming.service;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author liming
 * @Date 2024/8/9 11:06
 */
public interface IAsyncBatchExportExcelService {
    /**
     * 分批次异步导出数据
     * @param map
     * @param countDownLatch
     */
    void executeAsyncTask(Map<String, Object> map, CountDownLatch countDownLatch);
}
