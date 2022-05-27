package com.ruoyi.quartz.domain;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ruoyi.common.core.domain.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.quartz.util.CronUtils;
import org.nutz.dao.entity.annotation.*;

/**
 * 定时任务调度表 sys_job
 *
 * @author ruoyi
 */
@Table("sys_job")
public class SysJob extends BaseModel {

    /**
     * 任务ID
     */
    @Id
    @ColDefine(type = ColType.INT, width = 10)
    @Column("job_id")
    @Comment("任务ID")
    private Long jobId;

    /**
     * 任务名称
     */
    @Column("job_name")
    @Comment("任务名称")
    private String jobName;

    /**
     * 任务组名
     */
    @Column("job_group")
    @Comment("任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @Column("invoke_target")
    @Comment("调用目标字符串")
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    @Column("cron_expression")
    @Comment("cron执行表达式")
    @Excel(name = "cron执行表达式")
    private String cronExpression;

    /**
     * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    @Column("misfire_policy")
    @Comment("计划执行错误策略（1立即执行 2执行一次 3放弃执行）")
    @Excel(name = "计划执行错误策略", readConverterExp = "1=立即执行,2=执行一次,3=放弃执行")
    private String misfirePolicy;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    @Column("concurrent")
    @Comment("是否并发执行（0允许 1禁止）")
    @Excel(name = "是否并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /**
     * 状态（0正常 1暂停）
     */
    @Column("status")
    @Comment("状态（0正常 1暂停）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;


    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("jobId", getJobId())
                .append("jobName", getJobName())
                .append("jobGroup", getJobGroup())
                .append("cronExpression", getCronExpression())
                .append("nextValidTime", getNextValidTime())
                .append("misfirePolicy", getMisfirePolicy())
                .append("concurrent", getConcurrent())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
