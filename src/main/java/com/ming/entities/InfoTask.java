package com.ming.entities;

import com.ming.entities.VO.InFoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author liming
 * @Date 2022/10/8 16:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InfoTask {


    private String id;

    /**
     * 动态任务名
     */
    //private String name;

    /**
     * 设定动态任务开始时间
     */
    private LocalDateTime start;

    /**
     * 任务详情
     */
    private InFoVO taskInfo;
}
