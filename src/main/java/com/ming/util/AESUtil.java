package com.ming.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密AES算法
 * @Author liming
 * @Date 2024/2/18 16:33
 */
@Slf4j
public class AESUtil {
    private static final String SKEY = "AES2021082300123";

    /**
     * <summary>
     * 加密
     * </summary>
     * <param name="sSrc">加密前明文</param>
     * <param name="sKey">16位密钥<</param>
     * <returns></returns>
     */
    public static String Encrypt(String sSrc) {

        byte[] encrypted = new byte[0];
        try {
            byte[] raw = SKEY.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] ii = sSrc.getBytes("UTF-8");
            sSrc = new String(ii);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("AES加密错误{}", e.getMessage());
        }
        String str = new Base64().encodeToString(encrypted);
        str = str.replace("/","xiegang");
        str = str.replace("+","jiahao");
        return str;
    }

    /**
     * <summary>
     * 解密
     * </summary>
     * <param name="sSrc">密文</param>
     * <param name="sKey">16位密钥<</param>
     * <returns></returns>
     */
    public static String Decrypt(String sSrc) {
        sSrc = sSrc.replace("xiegang","/");
        sSrc = sSrc.replace("jiahao","+");
        try {
            byte[] raw = SKEY.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "UTF-8");
                return originalString;
            } catch (Exception e) {
                log.error("AES解密密错误{}", e.getMessage());
            }
        } catch (Exception ex) {
            log.error("AES解密密错误{}", ex.getMessage());

        }
        return null;
    }

    public static void main(String[] args) {
        String srt = "admin:115948f91598265ad83b03db3e499a9c";
        String encrypt = AESUtil.Encrypt(srt);
        System.out.println(encrypt);
        String LL = Encrypt(srt);
        System.out.println("加密" + LL);
        String kk = Decrypt("We2iExiegangtq3yaiB5oxieganguIxHwKGK0OB4WCKjiahaohWXzsUQzYB48jiahaooBcCZtejd7FIRoeqwcW");
        System.out.println("解密" + kk);
    }
}
