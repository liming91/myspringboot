package com.ming.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.bean.Test;
import com.ming.entities.DTO.MailDTO;
import com.ming.exception.ServiceException;
import com.ming.mapper.TestMapper;
import com.ming.service.ExcelService;
import com.ming.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private TestMapper testMapper;

    @Value("${mail.flag:false}")
    private boolean mailFlag;

    @Value("${mail.url:192.168.1.1}")
    private String mailUrl;

    @Value("${mail.to:522724933@qq.com}")
    private String mailTo;

    @Value("${minio.url}")
    private String minIoUrl;

    @Value("${spring.profiles.active}")
    private String activeEnv;


    @Override
    public void export(HttpServletResponse response) {
        // 模拟从数据库获取需要导出的数据
        List<Test> personList = testMapper.select();
        //导出操作
        Workbook workbook = ExcelUtil.exportExcel(personList, "用户列表", "用户", Test.class, "用户信息" + System.currentTimeMillis() + ".xls", response);
        makeExcel(workbook);
    }


    @Override
    public void exportSheet(HttpServletResponse response) {
        String startTime = DateUtils.getNowDate("yyyy-MM-dd");
        String endTime = DateUtils.getYesterday("yyyy-MM-dd");
        if (!"prod".equals(activeEnv)) {
            startTime = "2021-06-01";
            endTime = "2021-06-02";
        }
        // 模拟从数据库获取需要导出的数据
        List<Test> personList1 = testMapper.select();
        List<Test> personList2 = testMapper.select();
        // 创建参数对象
        ExportParams exportParams1 = new ExportParams();
        exportParams1.setStyle(ExcelStyleUtil.class);
        // 设置sheet得名称
        exportParams1.setSheetName("人员信息");
        ExportParams exportParams2 = new ExportParams();
        exportParams2.setSheetName("用户信息");

        // 创建sheet1使用得map
        Map<String, Object> deptDataMap = new HashMap<>(4);
        // title的参数为ExportParams类型
        deptDataMap.put("title", exportParams1);
        // 模版导出对应得实体类型
        deptDataMap.put("entity", Test.class);
        // sheet中要填充得数据
        deptDataMap.put("data", personList1);

        // 创建sheet2使用得map
        Map<String, Object> userDataMap = new HashMap<>(4);
        userDataMap.put("title", exportParams2);
        userDataMap.put("entity", Test.class);
        userDataMap.put("data", personList2);
        // 将sheet1和sheet2使用得map进行包装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(deptDataMap);
        sheetsList.add(userDataMap);
        // 执行方法
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        ExcelUtil.downLoadExcel("用户信息" + ".xls", response, workbook);
        makeExcel(workbook);
    }

    @Override
    public String importTest(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        //params.setNeedSave(true);
        params.setTitleRows(1);
        List<Test> result = ExcelImportUtil.importExcel(file.getInputStream(), Test.class, params);

        if (CollectionUtils.isEmpty(result)) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Test test : result) {
            try {
                // 验证是否存在这个用户
                List<Test> tests = testMapper.selectList(new LambdaQueryWrapper<Test>().eq(Test::getName, test.getName()));
                if (CollectionUtils.isEmpty(tests)) {
                    test.setId(IdUtil.simpleUUID());
                    testMapper.insert(test);
                    successNum++;
                    successMsg.append(successNum).append("账号: ").append(test.getName()).append(" 导入成功");
                } else {
                    failureNum++;
                    failureMsg.append(failureNum + "账号: " + test.getName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = failureNum + "账号: " + test.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public String importTemplate(HttpServletResponse response) {
        List<Test> personList = new ArrayList<>();
        //导出操作
        Workbook workbook = ExcelUtil.exportExcel(personList, "用户列表", "用户", Test.class, "用户信息" + System.currentTimeMillis() + ".xls", response);
        String url = makeExcel(workbook);
        return url;
    }

    /**
     * 生成excel
     *
     * @param workbook
     * @return
     */
    public String makeExcel(Workbook workbook) {
        String minIoPath = null;
        try {
            //生成在指定目录
            String fileName = "数据统计" + System.currentTimeMillis() + ".xls";
            FileOutputStream os = new FileOutputStream(fileName);
            workbook.write(os);
            os.flush();
            os.close();
            //上传到miniIo
            minIoPath = minIoUpload("excel", fileName);
            log.info("文件生成成功,minIoPath文件路径：" + minIoPath);
        } catch (IOException e) {
            log.error("生成excel错误", e);
        }
        return minIoPath;
    }

    public String minIoUpload(String folderName, String fileName) {
        String minIoUploadUrl = null;
        try {
            File file = new File(fileName);
            String minIOPath = MinIoUtil.upload(folderName, file);
            log.info("数据文件生成成功,minIOPath路径：" + minIOPath);
            minIoUploadUrl = minIOPath;
            //删除文件
            file.delete();
        } catch (Exception e) {
            log.error("上传到minIo错误", e);
        }
        return minIoUploadUrl;
    }


    /**
     * 发送邮件
     */
    public void sendEmail(Map<String, Object> map) {
        if (mailFlag) {
            log.info("--------------------- 开始发送邮件(携带附件) ------------------");
            MailDTO mailDTO = new MailDTO();
            mailDTO.setObj(map);
            mailDTO.setSubject("异常表数据统计");
            mailDTO.setTemplateName("energyWaterElectricityList.html");
            mailDTO.setTo("522724933@qq.com");
            String res = "";
            try {
                String mailData = JSONUtil.toJsonStr(mailDTO);
                //mailUrl = " http://127.0.0.1:7713/mail/sendTemplateMail";
                res = HttpRequest.post(mailUrl)
                        .header("Content-Type", "application/json;charset=utf-8")
                        .body(mailData)
                        .execute()
                        .body();
                log.info("表数据统计发送邮件返回数据: {}", res);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("异常表数据统计发送邮件返回数据异常信息: {}", res);
            }
        }
    }
}
