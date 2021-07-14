package com.ming.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

/**
 * 使用jpa注解配置映射关系
 */
@Entity //标注这是一个实体类、和数据库表映射类
@TableName(value = "tbl_user")//指定和数据库那个表对应，;如果省略默认表名就是user
@Data
public class User {

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Integer id;

    private Integer logic_flag;

    @Column//省略默认列名就是属性名
    private String email;

    @TableField(value = "last_name")//这是和数据表对应的列
    private String lastName;


}


