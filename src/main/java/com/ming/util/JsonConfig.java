package com.ming.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class JsonConfig {

    public static List<Map<String,Object>> getJsonConfig(String filePath) throws FileNotFoundException {
        File jsonFile = ResourceUtils.getFile(filePath);
        //String json = FileUtils.readFileToString(jsonFile);
        return  null;
    }
}
