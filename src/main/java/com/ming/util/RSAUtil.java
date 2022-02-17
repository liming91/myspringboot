package com.ming.util;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.core.codec.Base64;
import sun.misc.BASE64Decoder;


/**
 *  RSA加密解密，加签解签工具类
 */
public class RSAUtil {

        /*
         * 常见异常对照
         * NoSuchAlgorithmException 无此算法，一般是环境问题，编译环境或者运行环境
         * InvalidKeyException 秘钥非法，秘钥数据或者格式有问题，---begin--- 这种不需要
         * IllegalBlockSizeException 明文长度非法，秘钥大小决定了可加密明文的长度，长度不符合会报出该异常
         * BadPaddingException，NoSuchPaddingException 明文数据非法
         * UnsupportedEncodingException 不支持的编码方式
         * */

        /**
         * @Fields RSA : RSA算法
         */
        public static final String RSA = "RSA";
        /**
         * @Fields SIGN_ALGORITHMS : 签名算法类型
         */
        public static final String SIGN_ALGORITHMS = "SHA256WithRSA";

        /**
         * @Fields KEY_SIZE : 密钥长度 于原文长度对应 以及越长速度越慢
         */
        public static final int KEY_SIZE = 1024;

        /**
         * @Fields keyMap : 随机产生的公钥与私钥 0-公钥 1-私钥
         */
        public static Map<Integer, String> keyMap = new HashMap<Integer, String>();

        /**
         * @Title: genKeyPair
         * @Description: Generator KeyPair
         * @param --getPrivate() 得到私钥 getPublic() 得到公钥
         * @return KeyPair
         * @throws NoSuchAlgorithmException 无此算法
         */
        public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = null;
            keyPairGen = KeyPairGenerator.getInstance(RSA);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return keyPair;
        }

        /**
         * @Title: GeneratorKey
         * @Description: 随机生成密钥对
         * @return Map<Integer, String> keyMap 秘钥对 0-公钥 1-私钥
         * @throws Exception
         */
        public static void GeneratorKey() throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 生成秘钥对
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
            // base64转码
            String publicKeyString = new String(org.apache.commons.codec.binary.Base64.encodeBase64(publicKey.getEncoded()));
            String privateKeyString = new String(org.apache.commons.codec.binary.Base64.encodeBase64((privateKey.getEncoded())));
            keyMap.put(0, publicKeyString); // 0表示公钥
            keyMap.put(1, privateKeyString); // 1表示私钥
        }

        /**
         * @Title: encrypt
         * @Description: RSA公钥加密
         * @param encryptData 待加密数据
         * @param publicKey base64编码的公钥
         * @return String 加密后的数据
         * @throws
         */
        public static String encrypt(String encryptData, String publicKey) throws Exception {
            try {
                // base64编码的公钥，需要先decode
                byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
                RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSA)
                        .generatePublic(new X509EncodedKeySpec(decoded));
                // RSA加密
                Cipher cipher = Cipher.getInstance(RSA);
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                String outStr = org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(encryptData.getBytes("UTF-8")));
                return outStr;
            } catch (NoSuchAlgorithmException e) {
                throw new NoSuchAlgorithmException("无此加密算法，请检查环境");
            } catch (NoSuchPaddingException e) {
                throw new NoSuchPaddingException("明文数据未找到");
            } catch (InvalidKeyException e) {
                throw new InvalidKeyException("加密秘钥非法，请检查");
            } catch (IllegalBlockSizeException e) {
                throw new IllegalBlockSizeException("明文长度非法");
            } catch (BadPaddingException e) {
                throw new BadPaddingException("明文数据已损坏");
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("其他错误:", e);
            }
        }

        /**
         * @Title: decrypt
         * @Description: RSA私钥解密
         * @param privateKey base64编码的私钥
         * @return String
         * @throws Exception 解密过程中的异常信息
         */
        public static String decrypt(String decryptData, String privateKey) throws Exception {
            // 64位解码 加密后的字符串
            byte[] inputByte = org.apache.commons.codec.binary.Base64.decodeBase64(decryptData.getBytes("utf-8"));
            byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSA)
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            String decryptStr = new String(cipher.doFinal(inputByte));
            return decryptStr;
        }



    /**
     * @Title: sign
     * @Description: RSA签名
     * @param -signData 待签名数据
     * @param privateKey 私钥字符串
     * @return String 签名域
     * @throws
     */
    public static String sign(String content, String privateKey) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyf = KeyFactory.getInstance(RSA);
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes());
        byte[] signed = signature.sign();
        return Base64.encode(signed);
    }

    /**
     * @Title: verify
     * @Description: RSA验签名检查
     * @param content   待签名数据
     * @param sign      签名域
     * @param publicKey base64后的公钥字符串
     * @return boolean 验签结果
     * @throws
     */
    public static boolean verify(String content, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        byte[] encodedKey = Base64.decode(publicKey);
        if (encodedKey == null) {
            return false;
        }
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(pubKey);
        signature.update(content.getBytes());
        // 验证方法 返回true则为比对成功
        boolean bverify = signature.verify(Base64.decode(sign));
        return bverify;
    }



}
