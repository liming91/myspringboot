package com.ming.entities.VO;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.ming.entities.Info;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author liming
 * @Date 2024/6/26 14:59
 */
@Data
public class TestAddrVO implements Serializable {
    private static final long serialVersionUID = -743614233930001745L;

    private Long id;

    private JSONObject addr;

    private String name;

    private String tel;

}

