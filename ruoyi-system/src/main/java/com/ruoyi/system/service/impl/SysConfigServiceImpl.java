package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.page.TableData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.service.BaseServiceImpl;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig> implements ISysConfigService {

    @Autowired
    private RedisCache redisCache;

    public Cnd queryWrapper(SysConfig sysConfig) {
        Cnd cnd = Cnd.NEW();
        if (Lang.isNotEmpty(sysConfig)) {
            if (Lang.isNotEmpty(sysConfig.getConfigName())) {
                cnd.and("config_name" , "like" , "%" + sysConfig.getConfigName() + "%");
            }
            if (Lang.isNotEmpty(sysConfig.getConfigKey())) {
                cnd.and("config_key" , "=" , sysConfig.getConfigKey());
            }
            if (Lang.isNotEmpty(sysConfig.getConfigValue())) {
                cnd.and("config_value" , "=" , sysConfig.getConfigValue());
            }
            if (Lang.isNotEmpty(sysConfig.getConfigType())) {
                cnd.and("config_type" , "=" , sysConfig.getConfigType());
            }
        }
        return cnd;
    }

    @Override
    public List<SysConfig> query(SysConfig sysConfig) {
        return this.query(queryWrapper(sysConfig));
    }

    @Override
    public TableData<SysConfig> query(SysConfig sysConfig, int pageNumber, int pageSize) {
        return this.queryTable(queryWrapper(sysConfig), pageNumber, pageSize);
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId) {
        return this.fetch(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig retConfig = this.fetch(Cnd.where("config_key" , "=" , configKey));
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaOnOff() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaOnOff)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return query(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        try{
            this.insert(config);
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        int row = this.update(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 " , config.getConfigKey()));
            }
            this.delete(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = this.query();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
    public void clearConfigCache() {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info =this.fetch(Cnd.where("config_key","=",config.getConfigKey()));
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
