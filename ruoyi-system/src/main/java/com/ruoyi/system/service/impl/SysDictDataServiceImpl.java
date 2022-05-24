package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.service.ISysDictDataService;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictData> implements ISysDictDataService {
    public Cnd queryWrapper(SysDictData sysDictData) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysDictData)) {
            if (Lang.isNotEmpty(sysDictData.getDictSort())) {
                cnd.and("dict_sort", "=", sysDictData.getDictSort());
            }
            if (Lang.isNotEmpty(sysDictData.getDictLabel())) {
                cnd.and("dict_label", "=", sysDictData.getDictLabel());
            }
            if (Lang.isNotEmpty(sysDictData.getDictValue())) {
                cnd.and("dict_value", "=", sysDictData.getDictValue());
            }
            if (Lang.isNotEmpty(sysDictData.getDictType())) {
                cnd.and("dict_type", "=", sysDictData.getDictType());
            }
            if (Lang.isNotEmpty(sysDictData.getCssClass())) {
                cnd.and("css_class", "=", sysDictData.getCssClass());
            }
            if (Lang.isNotEmpty(sysDictData.getListClass())) {
                cnd.and("list_class", "=", sysDictData.getListClass());
            }
            if (Lang.isNotEmpty(sysDictData.getIsDefault())) {
                cnd.and("is_default", "=", sysDictData.getIsDefault());
            }
            if (Lang.isNotEmpty(sysDictData.getStatus())) {
                cnd.and("status", "=", sysDictData.getStatus());
            }
        }
        return cnd;
    }

    @Override
    public List<SysDictData> query(SysDictData sysDictData) {
        return this.query(queryWrapper(sysDictData));
    }

    @Override
    public TableData<SysDictData> query(SysDictData sysDictData, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysDictData), pageNumber, pageSize);
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return query(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        SysDictData data = fetch(Cnd.where("dict_type", "=", dictType).and("dict_value", "=", dictValue));
        return Lang.isEmpty(data) ? "" : data.getDictLabel();
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return fetch(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            delete(dictCode);
            List<SysDictData> dictDatas = query(Cnd.where("dict_type", "=", data.getDictType()));
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        int row = Lang.isEmpty(insert(data).getDictCode()) ? 0 : 1;
        if (row > 0) {
            List<SysDictData> dictDatas = query(Cnd.where("dict_type", "=", data.getDictType()));
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data) {
        int row = update(data);
        if (row > 0) {
            List<SysDictData> dictDatas = query(Cnd.where("dict_type", "=", data.getDictType()));
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
