package com.ruoyi.common.core.domain;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.SecurityUtils;
import org.nutz.dao.entity.annotation.*;
import org.nutz.lang.random.R;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamming_Yu on 2018/12/29.
 */
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    @Column("create_by")
    @Comment("创建者")
    @Prev(els = @EL("$me.uid()"))
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String createBy;

    /**
     * 创建时间
     */
    @Column("create_time")
    @Prev(els = {@EL("$me.now()")})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @Column("update_by")
    @Comment("更新者")
    @Prev(els = @EL("$me.uid()"))
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String updateBy;

    /**
     * 更新时间
     */
    @Prev(els = @EL("$me.now()"))
    @Column("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Column("remark")
    @Comment("备注")
    private String remark;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    public String uuid() {
        return R.UU32().toLowerCase();
    }

    public Date now() {
        return new Date();
    }

    public String uid() {
        try {
            Long userId = SecurityUtils.getUserId();
            return ObjectUtil.isEmpty(userId) ? "" : userId.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}