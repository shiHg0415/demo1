package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.ruoyi.common.helper.DataBaseHelper;
import com.ruoyi.system.service.ISysDataScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据权限 实现
 * <p>
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author Lion Li
 */
@Service("sdss")
public class SysDataScopeServiceImpl implements ISysDataScopeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getRoleCustom(Long roleId) {
        List<Long> deptIds = jdbcTemplate.queryForList("select dept_id from sys_role_dept srd where srd.role_id = ?", Long.class, roleId);
        if (CollUtil.isNotEmpty(deptIds)) {
            return deptIds.stream().map(Convert::toStr).collect(Collectors.joining(","));
        }
        return null;
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        List<Long> ids = jdbcTemplate.queryForList("select dept_id from sys_dept sd where " + DataBaseHelper.findInSet(deptId, "ancestors"), Long.class);
        ids.add(deptId);

        Map<String, Object> params = new HashMap<>();
        params.put("deptId", ids);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Long> deptIds = template.queryForList("select dept_id from sys_dept sd where sd.dept_id in (:deptId)", params, Long.class);
        if (CollUtil.isNotEmpty(deptIds)) {
            return deptIds.stream().map(Convert::toStr).collect(Collectors.joining(","));
        }
        return null;
    }

}
