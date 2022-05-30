package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.nutz.dao.entity.annotation.*;
import com.ruoyi.common.core.domain.BaseModel;

/**
 * 岗位信息对象 sys_post
 *
 * @author ruoyi
 * @date 2022-05-23
 */
@Table("sys_post")
public class SysPost extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @Id
    @ColDefine(type = ColType.INT, width = 10)
    @Column("post_id")
    @Comment("岗位ID")
    private Long postId;

    /**
     * 岗位编码
     */
    @Column("post_code")
    @Comment("岗位编码")
    @Excel(name = "岗位编码")
    private String postCode;

    /**
     * 岗位名称
     */
    @Column("post_name")
    @Comment("岗位名称")
    @Excel(name = "岗位名称")
    private String postName;

    /**
     * 显示顺序
     */
    @Column("post_sort")
    @Comment("显示顺序")
    @Excel(name = "显示顺序")
    private Long postSort;

    /**
     * 状态（0正常 1停用）
     */
    @Column("status")
    @Comment("状态")
    @Excel(name = "状态" , readConverterExp = "0=正常,1=停用")
    private String status;

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostSort(Long postSort) {
        this.postSort = postSort;
    }

    public Long getPostSort() {
        return postSort;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("postId" , getPostId())
                .append("postCode" , getPostCode())
                .append("postName" , getPostName())
                .append("postSort" , getPostSort())
                .append("status" , getStatus())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateBy" , getUpdateBy())
                .append("updateTime" , getUpdateTime())
                .append("remark" , getRemark())
                .toString();
    }
}
