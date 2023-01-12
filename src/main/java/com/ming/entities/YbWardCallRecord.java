package com.ming.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhanghui
 * @desc 全视通消息推送接收
 * 设备号码中有房号和床号
 * 挂断/复位的状态下，通话时长为0就代表这个通话是未应答
 * @date 2019/9/10
 */

@Data
@ToString
@TableName(value = "YB_WARD_CALL_RECORD")
public class YbWardCallRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "全时通病区号")
    @TableField("WARD_NO")
    private String WardNo;

    @ApiModelProperty(value = "呼叫状态 1：呼叫 2：接听 3 复位、挂断 100：复位所有的呼叫信息")
    @TableField("CALLSTATE_TYPE")
    private String CallStateType;

    @ApiModelProperty(value = "主叫方设备号码")
    @TableField("CALLER")
    private String Caller;

    @ApiModelProperty(value = "主叫方设备类型")
    @TableField("CALLERDEVICE_TYPE")
    private String CallerDeviceType;

    @ApiModelProperty(value = "主叫方设备描述")
    @TableField("CALLERDEVICEDES")
    private String CallerDeviceDes;

    @ApiModelProperty(value = "呼叫时间（时间换算为秒，从1970到当前秒数，下同）")
    @TableField("CALLTIME")
    private String CallTime;

    @ApiModelProperty(value = "被叫方设备类型")
    @TableField("CALLEEDEVICE_TYPE")
    private String CalleeDeviceType;

    @ApiModelProperty(value = "被叫方设备描述")
    @TableField("CALLEEDEVICEDES")
    private String CalleeDeviceDes;

    @ApiModelProperty(value = "被叫方设备号码")
    @TableField("CALLEE")
    private String Callee;

    @ApiModelProperty(value = "结束时间（处理时间）")
    @TableField("EDNTIME")
    private String EndTime;

    @ApiModelProperty(value = "通话时长")
    @TableField("CALLLENGTH")
    private Integer CallLength;

    @ApiModelProperty(value = "处理方式 处理方式对此次呼叫的处理 1：接听 2：挂断、复位")
    @TableField("DEALT_TYPE")
    private Integer DealtType;

    @ApiModelProperty(value = "处理方号码")
    @TableField("DEALT_NO")
    private String DealtNo;

    @ApiModelProperty(value = "呼叫消息类型")
    @TableField("NOTIFYMSG_TYPE")
    private Integer NotifymsgType;

    @ApiModelProperty(value = "呼叫消息类型描述")
    @TableField("NOTIFYMSG_TYPEDES")
    private String NotifymsgTypeDes;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREAT_DATE")
    private String creatDate;

    @TableField(exist =false)
    @ApiModelProperty(value = "患者姓名")
    private String patientName;

    @ApiModelProperty("病床号")
    @TableField("bed_no")
    private String bedNo;

    @ApiModelProperty("病房号")
    @TableField(exist = false)
    private String roomNo;

    @ApiModelProperty("病区编号")
    @TableField(exist = false)
    private String wardCode;

    @ApiModelProperty("所属医院标识 0-南院 1-北院")
    private String hospitalFlag;

}
