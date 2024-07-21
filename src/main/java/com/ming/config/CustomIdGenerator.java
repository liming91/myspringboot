package com.ming.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.context.annotation.Configuration;

/**
 * 重写 mybatisplus 主键id生成策略
 * Created By Ranger on 2022/5/11.
 */
@Configuration
public class CustomIdGenerator implements IdentifierGenerator {
    private final Snowflake snowflake;

    public CustomIdGenerator() {
        this(0L, 1L);
    }

    public CustomIdGenerator(long workerId, long dataCenterId) {
        this.snowflake = new Snowflake(workerId, dataCenterId);
    }

    @Override
    public Number nextId(Object entity) {
        return this.snowflake.nextId();
    }

    @Override
    public String nextUUID(Object entity) {
//        //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
//        Class<?> table = entity.getClass();
//        //根据bizKey调用分布式ID生成
//        String idString = table.cast();
//        String[] sp = idString.split("_");
//        if(sp.length > 1){
//            idString = sp[0].substring(0,1) + sp[1].substring(0,1);
//        }else {
//            idString = sp[0].substring(0,2);
//        }
//        Snowflake snowflake = IdUtil.getSnowflake(0L, 1L);
//         idString = idString + snowflake.nextIdStr();
        return null;
    }
}
