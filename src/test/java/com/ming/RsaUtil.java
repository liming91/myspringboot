package com.ming;

import com.ming.util.RSAUtil;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author liming
 * @Date 2023/8/18 16:15
 */
public class RsaUtil {

    @Test
    public void clientRsaTest() throws Exception {
        //生成公钥和私钥
        RSAUtil.GeneratorKey();
        Map<Integer, String> keyMap = RSAUtil.keyMap;
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        System.out.println("===客户端======");
        //加密的字符串
        String message = "西安";

        //公钥加密message
        String messageEn = RSAUtil.encrypt(message, keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);

        //私钥解密
        String messageDe = RSAUtil.decrypt(messageEn, keyMap.get(1));
        System.out.println("解密后的字符串为:" + messageDe);

        //私钥进行签名
        String sign = RSAUtil.sign(message, keyMap.get(1));
        System.out.println("签名===："+sign);



        //公钥验签
        boolean verify = RSAUtil.verify(message, sign, keyMap.get(0));
        System.out.println(verify);


    }
}
