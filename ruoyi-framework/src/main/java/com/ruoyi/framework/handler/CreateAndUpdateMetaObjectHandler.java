package com.ruoyi.framework.handler;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.StrictFill;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.helper.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * MP注入处理器
 *
 * @author Lion Li
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            TableInfo tableInfo = findTableInfo(metaObject);
            List<StrictFill<?, ?>> fieldFills = new ArrayList<>();
            fieldFills.add(StrictFill.of("createTime", Date::new, Date.class));
            fieldFills.add(StrictFill.of("updateTime", Date::new, Date.class));
            fieldFills.add(StrictFill.of("createBy", this::getLoginUsername, String.class));
            fieldFills.add(StrictFill.of("createBy", this::getLoginId, Long.class));
            fieldFills.add(StrictFill.of("updateBy", this::getLoginUsername, String.class));
            fieldFills.add(StrictFill.of("updateBy", this::getLoginId, Long.class));

            fillStrategy(true, metaObject, tableInfo, fieldFills);
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            TableInfo tableInfo = findTableInfo(metaObject);
            List<StrictFill<?, ?>> fieldFills = new ArrayList<>();
            fieldFills.add(StrictFill.of("updateTime", Date::new, Date.class));
            fieldFills.add(StrictFill.of("updateBy", this::getLoginUsername, String.class));
            fieldFills.add(StrictFill.of("updateBy", this::getLoginId, Long.class));

            fillStrategy(false, metaObject, tableInfo, fieldFills);
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    /**
     * 获取登录用户名
     */
    private String getLoginUsername() {
        LoginUser loginUser;
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录");
            return null;
        }
        return loginUser.getUsername();
    }

    /**
     * 获取登录用户id
     *
     * @return
     */
    private Long getLoginId() {
        return LoginHelper.getUserId();
    }

    /**
     * insertFill为true时，需要判断是否为null，当为null时才填充数据
     * insertFill为false时，无需判断是否为null，覆盖数据
     *
     * @param insertFill
     * @param metaObject
     * @param tableInfo
     * @param fieldFills
     */
    private void fillStrategy(boolean insertFill, MetaObject metaObject, TableInfo tableInfo, List<StrictFill<?, ?>> fieldFills) {
        if ((insertFill && tableInfo.isWithInsertFill()) || (!insertFill && tableInfo.isWithUpdateFill())) {
            for (StrictFill<?, ?> fieldFill : fieldFills) {
                for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
                    if (tableFieldInfo.getProperty().equals(fieldFill.getFieldName()) && tableFieldInfo.getPropertyType().equals(fieldFill.getFieldType())) {
                        // 如果是更新操作，则不需要判断是否为null，反之插入时需要判断当值为null时才覆盖
                        if (tableInfo.isWithUpdateFill() || metaObject.getValue(fieldFill.getFieldName()) == null) {
                            Object obj = fieldFill.getFieldVal().get();
                            if (Objects.nonNull(obj)) {
                                metaObject.setValue(fieldFill.getFieldName(), obj);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

}
