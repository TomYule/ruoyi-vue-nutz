package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLog> implements ISysOperLogService {
    public Cnd queryWrapper(SysOperLog sysOperLog) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysOperLog)) {
            if (Lang.isNotEmpty(sysOperLog.getTitle())) {
                cnd.and("title" , "=" , sysOperLog.getTitle());
            }
            if (Lang.isNotEmpty(sysOperLog.getBusinessType())) {
                cnd.and("business_type" , "=" , sysOperLog.getBusinessType());
            }
            if (Lang.isNotEmpty(sysOperLog.getMethod())) {
                cnd.and("method" , "=" , sysOperLog.getMethod());
            }
            if (Lang.isNotEmpty(sysOperLog.getRequestMethod())) {
                cnd.and("request_method" , "=" , sysOperLog.getRequestMethod());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperatorType())) {
                cnd.and("operator_type" , "=" , sysOperLog.getOperatorType());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperName())) {
                cnd.and("oper_name" , "like" , "%" + sysOperLog.getOperName() + "%");
            }
            if (Lang.isNotEmpty(sysOperLog.getDeptName())) {
                cnd.and("dept_name" , "like" , "%" + sysOperLog.getDeptName() + "%");
            }
            if (Lang.isNotEmpty(sysOperLog.getOperUrl())) {
                cnd.and("oper_url" , "=" , sysOperLog.getOperUrl());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperIp())) {
                cnd.and("oper_ip" , "=" , sysOperLog.getOperIp());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperLocation())) {
                cnd.and("oper_location" , "=" , sysOperLog.getOperLocation());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperParam())) {
                cnd.and("oper_param" , "=" , sysOperLog.getOperParam());
            }
            if (Lang.isNotEmpty(sysOperLog.getJsonResult())) {
                cnd.and("json_result" , "=" , sysOperLog.getJsonResult());
            }
            if (Lang.isNotEmpty(sysOperLog.getStatus())) {
                cnd.and("status" , "=" , sysOperLog.getStatus());
            }
            if (Lang.isNotEmpty(sysOperLog.getErrorMsg())) {
                cnd.and("error_msg" , "=" , sysOperLog.getErrorMsg());
            }
            if (Lang.isNotEmpty(sysOperLog.getOperTime())) {
                cnd.and("oper_time" , "=" , sysOperLog.getOperTime());
            }
        }
        return cnd;
    }

    @Override
    public List<SysOperLog> query(SysOperLog sysOperLog) {
        return this.query(queryWrapper(sysOperLog));
    }

    @Override
    public TableData<SysOperLog> query(SysOperLog sysOperLog, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysOperLog), pageNumber, pageSize);
    }


    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog) {
        insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return query(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return delete(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return fetch(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        clear();
    }
}
