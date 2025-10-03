package org.aimindflow.common.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.aimindflow.common.mybatis.entity.TreeEntity;
import org.aimindflow.common.mybatis.service.ITreeService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 树形服务实现类
 *
 * @author HezaoHezao
 */
public class TreeServiceImpl<M extends BaseMapper<T>, T extends TreeEntity<T>> extends BaseServiceImpl<M, T> implements ITreeService<T> {

    @Override
    public List<T> selectTree() {
        List<T> list = list();
        return buildTree(list);
    }

    @Override
    public List<T> buildTree(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取根节点
        List<T> rootList = list.stream()
                .filter(node -> node.getParentId() == null || node.getParentId() == 0)
                .collect(Collectors.toList());

        // 构建树
        rootList.forEach(root -> buildChildrenTree(root, list));

        return rootList;
    }

    /**
     * 递归构建子树
     *
     * @param parent 父节点
     * @param list   所有节点列表
     */
    private void buildChildrenTree(T parent, List<T> list) {
        // 获取子节点
        List<T> children = list.stream()
                .filter(node -> Objects.equals(node.getParentId(), parent.getId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parent.setChildren(children);
            // 递归构建子节点的子树
            children.forEach(child -> buildChildrenTree(child, list));
        }
    }

    @Override
    public List<T> selectChildrenById(Serializable id) {
        LambdaQueryWrapper<T> wrapper = Wrappers.lambdaQuery();
        wrapper.like(T::getAncestors, id);
        return list(wrapper);
    }

    @Override
    public boolean hasChildren(Serializable id) {
        LambdaQueryWrapper<T> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(T::getParentId, id);
        return count(wrapper) > 0;
    }

    @Override
    public List<T> selectAncestorsById(Serializable id) {
        // 获取当前节点
        T entity = getById(id);
        if (entity == null || entity.getAncestors() == null || entity.getAncestors().isEmpty()) {
            return new ArrayList<>();
        }

        // 获取祖级ID列表
        String[] ancestorIds = entity.getAncestors().split(",");
        List<Serializable> ids = new ArrayList<>();
        for (String ancestorId : ancestorIds) {
            ids.add(Long.parseLong(ancestorId));
        }

        // 查询祖级节点
        return listByIds(ids);
    }
}