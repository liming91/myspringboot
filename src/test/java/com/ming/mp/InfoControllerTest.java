package com.ming.mp;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.ming.entities.Info;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private final Info entity = new Info();

    @Before
    public void setUp()  {
        System.out.println("---------------start---------------");
        entity.setId(IdUtil.simpleUUID());
        entity.setName("单元测试");
        entity.setTel("12333333333");
        entity.setTime(new Date());
        //BeanUtil.copyProperties(dto, entity);


        System.out.println("================end================");
    }

    @Test
    public void list() throws Exception {
        System.out.println("---------------start---------------");
//        String json = "{\n" +
//                "\"pageNo\":1,\n" +
//                "\"pageSize\":10\n" +
//                "\n" +
//                "}";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/info/list")
                //.content(json.getBytes()) //传json参数

                //.header(Constant.TOKEN, SwaggerConfig.token)
        );
        resultActions.andReturn()
                .getResponse()
                .setCharacterEncoding("UTF-8"); //防止中文乱码;

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        System.out.println("================end================");
    }


    @Test
    public void add() throws Exception {
        System.out.println("---------------start---------------");
        byte[] jsonBytes = JSON.toJSONBytes(entity);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/info/save")
                .content(jsonBytes) //传json参数
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                //.header(Constant.TOKEN, SwaggerConfig.token)
        );
        resultActions.andReturn()
                .getResponse()
                .setCharacterEncoding("UTF-8"); //防止中文乱码;
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        System.out.println("================end================");
    }


    @Test
    public void update() throws Exception {
        System.out.println("---------------start---------------");
        entity.setId("1930459336221097986");
        byte[] jsonBytes = JSON.toJSONBytes(entity);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/info/update")
                .content(jsonBytes) //传json参数
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                //.header(Constant.TOKEN, SwaggerConfig.token)
        );
        resultActions.andReturn()
                .getResponse()
                .setCharacterEncoding("UTF-8"); //防止中文乱码;
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        System.out.println("================end================");
    }

}