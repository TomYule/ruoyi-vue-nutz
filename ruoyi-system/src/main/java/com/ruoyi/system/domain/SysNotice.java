package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.xss.Xss;
import org.nutz.dao.entity.annotation.*;

/**
 * 通知公告表 sys_notice
 * 
 * @author ruoyi
 */
@Table("sys_notice")
public class SysNotice extends BaseModel {
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    @Id
    @ColDefine(type = ColType.INT, width = 32)
    @Column("notice_id")
    @Comment("公告ID")
    private Long noticeId;

    /** 公告标题 */
    @Column("notice_title")
    @Comment("公告标题")
    @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @Column("notice_type")
    @Comment("公告类型（1通知 2公告）")
    @Excel(name = "公告类型", readConverterExp = "1=通知,2=公告")
    private String noticeType;

    /** 公告内容 */
    @Column("notice_content")
    @Comment("公告内容")
    @Excel(name = "公告内容")
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    @Column("status")
    @Comment("公告状态（0正常 1关闭）")
    @Excel(name = "公告状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    public String getNoticeTitle()
    {
        return noticeTitle;
    }

    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType()
    {
        return noticeType;
    }

    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent()
    {
        return noticeContent;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
