package com.ming.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JsonConfig {

    public static List<Map<String,Object>> getJsonConfig(String filePath) throws FileNotFoundException {
        File jsonFile = ResourceUtils.getFile(filePath);
        //String json = FileUtils.readFileToString(jsonFile);
        return  null;
    }


    public static JSONObject getHkJSON(String fileName) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            byte[] byteData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String jsonStr = new String(byteData, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            return jsonObject;
        } catch (Exception e) {
            e.getMessage();
            log.error("读取配置信息异常： {}", e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {
        JSONObject json = JsonConfig.getHkJSON("json/hk.json");
        Map<String, Object> resMap = new LinkedHashMap<>();
        System.out.println(json);
        if (json.getString("code").equals("0")) {
            JSONObject data = json.getJSONObject("data");
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("rows");
            if (CollectionUtils.isNotEmpty(list)) {
                List<Map<String, Object>> online1 = list.stream().filter(x -> x.get("online") == null).collect(Collectors.toList());
                System.out.println(online1);
                //在线
                long online = list.stream().filter(x -> x.get("online") != null && (int) x.get("online") == 1).count();
                //离线
                long offline = list.stream().filter(x -> x.get("online") != null && (int) x.get("online") == 0).count();
                //故障 部件状态3异常状态、和离线状态
                long fault = list.stream().filter(x -> x.get("online") == null&&  (int) x.get("unitStatus") == 0).count();
                resMap.put("online", online);
                resMap.put("offline", offline);
                resMap.put("fault", fault);
            } else {
                resMap.put("online", 0);
                resMap.put("offline", 0);
                resMap.put("fault", 0);
            }
            resMap.put("count", list.size());
        }
        log.info("map:{}",resMap);
    }
}
