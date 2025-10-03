package org.aimindflow.common.mybatis.service;

import org.aimindflow.common.mybatis.entity.TreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 树形服务接口
 *
 * @author HezaoHezao
 */
public interface ITreeService<T extends TreeEntity<T>> extends IBaseService<T> {

    /**
     * 查询树形结构
     *
     * @return 树形结构列表
     */
    List<T> selectTree();

    /**
     * 构建树形结构
     *
     * @param list 列表
     * @return 树形结构列表
     */
    List<T> buildTree(List<T> list);

    /**
     * 根据ID查询所有子节点
     *
     * @param id 主键ID
     * @return 子节点列表
     */
    List<T> selectChildrenById(Serializable id);

    /**
     * 是否存在子节点
     *
     * @param id 主键ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildren(Serializable id);

    /**
     * 查询所有祖级节点
     *
     * @param id 主键ID
     * @return 祖级节点列表
     */
    List<T> selectAncestorsById(Serializable id);
}