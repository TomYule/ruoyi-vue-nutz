package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.mapper.SysLogininforMapper;
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl extends BaseServiceImpl<SysLogininfor> implements ISysLogininforService {
    public Cnd queryWrapper(SysLogininfor sysLogininfor) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysLogininfor)) {
            if (Lang.isNotEmpty(sysLogininfor.getUserName())) {
                cnd.and("user_name" , "like" , "%" + sysLogininfor.getUserName() + "%");
            }
            if (Lang.isNotEmpty(sysLogininfor.getIpaddr())) {
                cnd.and("ipaddr" , "=" , sysLogininfor.getIpaddr());
            }
            if (Lang.isNotEmpty(sysLogininfor.getLoginLocation())) {
                cnd.and("login_location" , "=" , sysLogininfor.getLoginLocation());
            }
            if (Lang.isNotEmpty(sysLogininfor.getBrowser())) {
                cnd.and("browser" , "=" , sysLogininfor.getBrowser());
            }
            if (Lang.isNotEmpty(sysLogininfor.getOs())) {
                cnd.and("os" , "=" , sysLogininfor.getOs());
            }
            if (Lang.isNotEmpty(sysLogininfor.getStatus())) {
                cnd.and("status" , "=" , sysLogininfor.getStatus());
            }
            if (Lang.isNotEmpty(sysLogininfor.getMsg())) {
                cnd.and("msg" , "=" , sysLogininfor.getMsg());
            }
            if (Lang.isNotEmpty(sysLogininfor.getLoginTime())) {
                cnd.and("login_time" , "=" , sysLogininfor.getLoginTime());
            }
        }
        return cnd;
    }

    @Override
    public List<SysLogininfor> query(SysLogininfor sysLogininfor) {
        return this.query(queryWrapper(sysLogininfor));
    }

    @Override
    public List<SysLogininfor> query(SysLogininfor sysLogininfor, int pageNumber, int pageSize) {
        return this.query(queryWrapper(sysLogininfor), pageNumber, pageSize);
    }


    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        this.insert(logininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        return this.query(logininfor);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return this.delete(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        this.clear();
    }
}
