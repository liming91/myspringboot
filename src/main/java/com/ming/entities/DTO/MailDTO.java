package com.ming.entities.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class MailDTO {
    // 收件人
    private String to;
    // 抄送人
    private String[] cc;
    // 标题
    private String subject;
    // 内容
    private String content;
    // 附件路径
    private String filePath;
    // html 模板名
    private String templateName;
    // html模板对应内容（map对象）
    private Map<String, Object> obj;
}
