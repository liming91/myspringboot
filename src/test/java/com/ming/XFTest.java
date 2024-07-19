package com.ming;

import com.google.gson.JsonObject;
import com.ming.util.http.OkHttpUtil;
import com.ming.xf.AuthUtil;
import com.ming.xf.XfConstant;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 科大讯飞
 * https://www.xfyun.cn/doc/spark/character_simulation/%E8%A7%92%E8%89%B2%E6%A8%A1%E6%8B%9FWeb%E6%96%87%E6%A1%A3.html#%E4%BA%8C%E3%80%81%E9%89%B4%E6%9D%83%E8%AE%A4%E8%AF%81
 * @Author liming
 * @Date 2024/7/19 17:58
 */
public class XFTest {

    String appId = "fc319695";
    String secret = "ODhiODA5YjFjNTQ1ZjgzNjc4NmE0YmNi";
    Long timestamp = System.currentTimeMillis();

    @Test
    public void register(){
        String signature = AuthUtil.getSignature(XfConstant.appId, XfConstant.secret, timestamp);

        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);
        params.put("playerName","测试");


        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("appId",appId);
        headMap.put("timestamp", String.valueOf(timestamp));
        headMap.put("signature",signature);

        String url ="https://ai-character.xfyun.cn/api/open/player/if-register";
        String s = OkHttpUtil.httpGetAndHead(url, params, headMap);
        System.out.println(s);
    }



    @Test
    public void playerAccount(){
        String signature = AuthUtil.getSignature(XfConstant.appId, XfConstant.secret, timestamp);

        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);
        params.put("playerName","测试2");
        params.put("playerType","玩家类型");
        params.put("description","玩家描述");
        params.put("senderIdentity","玩家职业身份（300字符以内，描述玩家的职业身份、和人格的关系、使命职责等）");


        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("appId",appId);
        headMap.put("timestamp", String.valueOf(timestamp));
        headMap.put("signature",signature);

        String url ="https://ai-character.xfyun.cn/api/open/player/register";

        JsonObject jsonObject = OkHttpUtil.sendPostByJsonAndHeader(url, params, headMap);
        System.out.println(jsonObject);
    }

}
