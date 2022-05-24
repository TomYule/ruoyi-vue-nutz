package com.ruoyi.quartz.service.impl;

import java.util.List;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.stereotype.Service;
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.service.ISysJobLogService;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobLogServiceImpl extends BaseServiceImpl<SysJobLog> implements ISysJobLogService {
    public Cnd queryWrapper(SysJobLog sysJobLog) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysJobLog)) {
            if (Lang.isNotEmpty(sysJobLog.getJobName())) {
                cnd.and("job_name", "like", "%" + sysJobLog.getJobName() + "%");
            }
            if (Lang.isNotEmpty(sysJobLog.getJobGroup())) {
                cnd.and("job_group", "=", sysJobLog.getJobGroup());
            }
            if (Lang.isNotEmpty(sysJobLog.getInvokeTarget())) {
                cnd.and("invoke_target", "=", sysJobLog.getInvokeTarget());
            }
            if (Lang.isNotEmpty(sysJobLog.getJobMessage())) {
                cnd.and("job_message", "=", sysJobLog.getJobMessage());
            }
            if (Lang.isNotEmpty(sysJobLog.getStatus())) {
                cnd.and("status", "=", sysJobLog.getStatus());
            }
            if (Lang.isNotEmpty(sysJobLog.getExceptionInfo())) {
                cnd.and("exception_info", "=", sysJobLog.getExceptionInfo());
            }
        }
        return cnd;
    }

    @Override
    public List<SysJobLog> query(SysJobLog sysJobLog) {
        return this.query(queryWrapper(sysJobLog));
    }

    @Override
    public TableData<SysJobLog> query(SysJobLog sysJobLog, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysJobLog), pageNumber, pageSize);
    }

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
        return query(jobLog);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        return fetch(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog) {
        insert(jobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds) {
        return delete(logIds);
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId) {
        return delete(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        clear();
    }
}
