package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * OSS对象存储分页查询对象 sys_oss
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOssBo extends BaseEntity {

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 原名
     */
    private String originalName;
    /**
     * 文件后缀名
     */
    private String fileSuffix;
    /**
     * URL地址
     */
    private String url;
    /**
     * 服务商
     */
    private String service;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
