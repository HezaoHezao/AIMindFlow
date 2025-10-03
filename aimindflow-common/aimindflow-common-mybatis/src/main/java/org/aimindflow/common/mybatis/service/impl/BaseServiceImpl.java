package org.aimindflow.common.mybatis.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aimindflow.common.mybatis.service.IBaseService;
import java.util.Collection;

/**
 * 基础服务实现类
 *
 * @author HezaoHezao
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {
    // 继承自ServiceImpl，显式实现 saveOrUpdateBatch 以满足接口要求

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        return saveOrUpdateBatch(entityList, 1000);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return true;
        }
        int i = 0;
        for (T entity : entityList) {
            saveOrUpdate(entity);
            i++;
            if (i % batchSize == 0) {
                // 可在此进行刷写或其他批处理逻辑；当前实现为简化处理
            }
        }
        return true;
    }
}