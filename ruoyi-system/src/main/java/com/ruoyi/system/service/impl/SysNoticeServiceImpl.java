package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl extends BaseServiceImpl<SysNotice> implements ISysNoticeService {
    public Cnd queryWrapper(SysNotice sysNotice) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysNotice)) {
            if (Lang.isNotEmpty(sysNotice.getNoticeTitle())) {
                cnd.and("notice_title" , "=" , sysNotice.getNoticeTitle());
            }
            if (Lang.isNotEmpty(sysNotice.getNoticeType())) {
                cnd.and("notice_type" , "=" , sysNotice.getNoticeType());
            }
            if (Lang.isNotEmpty(sysNotice.getNoticeContent())) {
                cnd.and("notice_content" , "=" , sysNotice.getNoticeContent());
            }
            if (Lang.isNotEmpty(sysNotice.getStatus())) {
                cnd.and("status" , "=" , sysNotice.getStatus());
            }
        }
        return cnd;
    }

    @Override
    public List<SysNotice> query(SysNotice sysNotice) {
        return this.query(queryWrapper(sysNotice));
    }

    @Override
    public List<SysNotice> query(SysNotice sysNotice, int pageNumber, int pageSize) {
        return this.query(queryWrapper(sysNotice), pageNumber, pageSize);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return fetch(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        return query(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice) {
        return Lang.isNotEmpty(insert(notice).getNoticeId()) ? 1 : 0;
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice) {
        return update(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId) {
        return delete(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return delete(noticeIds);
    }
}
