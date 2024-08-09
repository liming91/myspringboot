package com.ming.util.excel;

import com.ming.service.IAsyncBatchExportExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.ming.util.ZipUtils;

import static com.ming.service.impl.AsyncBatchExportExcelServiceImpl.DATA_TOTAL_COUNT;


/**
 * 多线程批量导出excel工具类
 *
 * @Author liming
 * @Date 2024/8/9 11:28
 */
@Slf4j
@Component
public class AsyncExcelExportUtil {

    // 定义导出的excel文件保存的路径
    private String filePath = "C:\\Desktop\\export\\";
    @Resource
    private IAsyncBatchExportExcelService asynExportExcelService;
    /**
     * 每批次处理的数据量
     */
    private static final int LIMIT = 40000;

    // 保证线程安全：每个线程设置本地变量值
    private static ThreadLocal<Queue<Map<String, Object>>> queueThreadLocal = new ThreadLocal<>();

    /**
     * 多线程批量导出 excel
     *
     * @param response 用于浏览器下载
     * @throws InterruptedException
     */
    public void threadExcel(HttpServletResponse response) {
        long start = System.currentTimeMillis();
        initQueue();
        //异步转同步，等待所有线程都执行完毕返回 主线程才会结束
        try {
            Queue<Map<String, Object>> queue = queueThreadLocal.get();
            CountDownLatch cdl = new CountDownLatch(queue.size());
            while (queue.size() > 0) {
                asynExportExcelService.executeAsyncTask(queue.poll(), cdl);
            }
            cdl.await();
            log.info("excel导出完成·······················");
            //压缩文件
            File zipFile = new File(filePath.substring(0, filePath.length() - 1) + ".zip");
            FileOutputStream fos1 = new FileOutputStream(zipFile);
            //压缩文件目录
            ZipUtils.toZip(filePath, fos1, true);
            //发送zip包
            ZipUtils.sendZip(response, zipFile);
        } catch (Exception e) {
            log.error("excel导出异常", e);
        } finally {
            // 使用完ThreadLocal对象之后清除数据，防止内存泄露
            queueThreadLocal.remove();
        }
        long end = System.currentTimeMillis();
        log.info("任务执行完毕共消耗：  " + (end - start) + "ms");
    }

    /**
     * 初始化队列
     */
    private void initQueue() {
        // Queue是java自己的队列，是同步安全的
        // 一个基于链接节点的无界线程安全的队列
        Queue<Map<String, Object>> queue = new ConcurrentLinkedQueue<>();
        long dataTotalCount = DATA_TOTAL_COUNT;// 数据的总数
        int listCount = (int) dataTotalCount;
        // 计算出多少页，即循环次数
        int count = listCount / LIMIT + (listCount % LIMIT > 0 ? 1 : 0);
        for (int i = 1; i <= count; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("page", i);
            map.put("limit", LIMIT);
            map.put("path", filePath);
            //添加元素
            queue.offer(map);
        }
        queueThreadLocal.set(queue);
    }

}
