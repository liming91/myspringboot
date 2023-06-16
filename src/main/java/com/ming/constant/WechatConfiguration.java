package com.ming.constant;

/**
 * 小程序配置
 *
 * @Author liming
 * @Date 2023/6/16 14:20
 */
public class WechatConfiguration {

    /**
     * 小程序appId
     */
    public static final String AppID = "wx740bf30d3a832693";

    /**
     * 小程序秘钥
     */
    public static final String AppSecret = "529becaccd235e316cf6f9a4f9272a78";

    /**
     * 获取unionId、openid
     */
    public static final String jsCode2session = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 模板id
     */
    public static final String templateId = "MxOvFiU3w7EzNfnG5FESM8yjyYzSx2xQK_HB_PrjoBg";



    /**
     * 获取小程序token地址 有效期7200秒
     */
    public static final String getAccessToken = "https://api.weixin.qq.com/cgi-bin/token";


}
