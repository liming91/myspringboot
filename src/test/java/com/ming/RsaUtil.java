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
    /**
     * 一、RSA加密简介
     *
     * 　　RSA加密是一种非对称加密。可以在不直接传递密钥的情况下，完成解密。这能够确保信息的安全性，避免了直接传递密钥所造成的被破解的风险。是由一对密钥来进行加解密的过程，分别称为公钥和私钥。两者之间有数学相关，该加密算法的原理就是对一极大整数做因数分解的困难性来保证安全性。通常个人保存私钥，公钥是公开的（可能同时多人持有）。
     *
     *
     *
     * 二、RSA加密、签名区别
     *
     * 　　加密和签名都是为了安全性考虑，但略有不同。常有人问加密和签名是用私钥还是公钥？其实都是对加密和签名的作用有所混淆。简单的说，加密是为了防止信息被泄露，而签名是为了防止信息被篡改。这里举2个例子说明。
     *
     * 第一个场景：战场上，B要给A传递一条消息，内容为某一指令。
     *
     * RSA的加密过程如下：
     *
     * （1）A生成一对密钥（公钥和私钥），私钥不公开，A自己保留。公钥为公开的，任何人可以获取。
     *
     * （2）B用A的公钥对消息进行加密，然后传递给A。
     *
     * （3）A接收到B加密的消息，利用A自己的私钥对消息进行解密。
     *
     * 　　在这个过程中，只有2次传递过程，第一次是A传递公钥给B，第二次是B传递加密消息给A，即使都被敌方截获，也没有危险性，因为只有A的私钥才能对消息进行解密，防止了消息内容的泄露。
     *
     *
     *
     * 第二个场景：A收到B发的消息后，需要进行回复“收到”。
     *
     * RSA签名的过程如下：
     *
     * （1）A生成一对密钥（公钥和私钥），私钥不公开，A自己保留。公钥为公开的，任何人可以获取。
     *
     * （2）A用自己的私钥对消息加签，形成签名，并将加签的消息和消息本身一起传递给B。
     *
     * （3）B收到消息后，利用A的公钥进行验签，如果验签出来的内容与消息本身一致，证明消息是A回复的。
     *
     * 　　在这个过程中，只有2次传递过程，第一次是A传递加签的消息和消息本身给B，第二次是B获取A的公钥，即使都被敌方截获，也没有危险性，因为只有A的私钥才能对消息进行签名，即使知道了消息内容，也无法伪造带签名的回复给B，防止了消息内容的篡改。
     *
     *
     *
     * 　　但是，综合两个场景你会发现，第一个场景虽然被截获的消息没有泄露，但是可以利用截获的公钥，将假指令进行加密，然后传递给A。第二个场景虽然截获的消息不能被篡改，但是消息的内容可以利用公钥验签来获得，并不能防止泄露。所以在实际应用中，要根据情况使用，也可以同时使用加密和签名，比如A和B都有一套自己的公钥和私钥，当A要给B发送消息时，先用B的公钥对消息加密，再对加密的消息使用A的私钥加签名，达到既不泄露也不被篡改，更能保证消息的安全性。
     *
     * 　　总结：公钥加密、私钥解密、私钥签名、公钥验签。
     *
     *

     * @throws Exception
     */
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
