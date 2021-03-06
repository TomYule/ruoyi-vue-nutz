package com.ruoyi.generator.service;

import java.util.List;

import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.common.utils.QueryUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.generator.domain.GenTableColumn;

/**
 * 业务字段 服务层实现
 *
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl extends BaseServiceImpl<GenTableColumn> implements IGenTableColumnService {

    public Cnd queryWrapper(GenTableColumn genTableColumn) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(genTableColumn)){
            if (Lang.isNotEmpty(genTableColumn.getTableId())){
                cnd.and("table_id" , "=" , genTableColumn.getTableId());
            }
            if (Lang.isNotEmpty(genTableColumn.getColumnName())){
                cnd.and("column_name" , "like" , "%" + genTableColumn.getColumnName() + "%");
            }
            if (Lang.isNotEmpty(genTableColumn.getColumnComment())){
                cnd.and("column_comment" , "like" , "%" + genTableColumn.getColumnComment() + "%");
            }
        }
        return cnd;
    }

    @Override
    public List<GenTableColumn> query(GenTableColumn genTableColumn) {
        return this.query(queryWrapper(genTableColumn));
    }

    @Override
    public List<GenTableColumn> query(GenTableColumn genTableColumn, int pageNumber, int pageSize) {
        return this.query(queryWrapper(genTableColumn), pageNumber, pageSize);
    }

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
        return query(Cnd.where("table_id", "=", tableId));
    }

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int insertGenTableColumn(GenTableColumn genTableColumn) {
        return Lang.isEmpty(insert(genTableColumn).getColumnId()) ? 0 : 1;
    }

    /**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int updateGenTableColumn(GenTableColumn genTableColumn) {
        return update(genTableColumn);
    }

    /**
     * 删除业务字段对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGenTableColumnByIds(String ids) {
        return delete(Convert.toLongArray(ids));
    }

	@Override
	public List<GenTableColumn> selectDbTableColumnsByName(String tableName) {
        Sql sql = QueryUtils.getDbQuery(this.dao().meta().getType()).tableColumnsByName(tableName);
		Entity<GenTableColumn> entity = dao().getEntity(GenTableColumn.class);
		sql.setEntity(entity);
		dao().execute(sql);
		return sql.getList(GenTableColumn.class);
	}
}
